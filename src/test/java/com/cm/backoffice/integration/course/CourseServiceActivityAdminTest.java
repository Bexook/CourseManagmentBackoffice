package com.cm.backoffice.integration.course;

import com.cm.common.exception.SystemException;
import com.cm.common.model.domain.CourseEntity;
import com.cm.common.model.dto.AppUserDTO;
import com.cm.common.model.dto.CourseDTO;
import com.cm.common.model.dto.CourseOverviewDTO;
import com.cm.common.model.enumeration.CourseAuthorities;
import com.cm.common.model.enumeration.UserRole;
import com.cm.common.repository.CourseRepository;
import com.cm.common.security.management.access.UserAccessValidation;
import com.cm.common.service.course.CourseService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.List;

import static com.cm.backoffice.AuthenticationTestUtil.authenticateUserWithRole;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class CourseServiceActivityAdminTest {

    @Autowired
    private UserAccessValidation userAccessValidation;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private CourseService courseService;

    @PostConstruct
    private void postConstruct() {
        authenticateUserWithRole(UserRole.ADMIN, 1L);
    }

    @Test
    public void get_course_by_id_id_not_found() {
        SystemException systemException = Assert.assertThrows(SystemException.class, () -> courseService.getCourseById(100000L));
        Assert.assertEquals("Course does not exist", systemException.getMessage());
        Assert.assertEquals(HttpStatus.BAD_REQUEST, systemException.getServiceCode());
    }


    @Test
    public void get_course_by_id() {
        final CourseDTO course = courseService.getCourseById(2L);
        Assert.assertEquals("TEST", course.getSubject());
        Assert.assertEquals("TEST", course.getDescription());
    }

    @Test
    public void get_courses_overview_available() {
        final List<CourseOverviewDTO> coursesOverview = courseService.getCoursesOverview(true);
        final CourseOverviewDTO courseOverview = coursesOverview.stream().findFirst().get();
        Assert.assertEquals(coursesOverview.size(), courseRepository.findAll(Example.of(new CourseEntity().setAvailable(true))).size());
        Assert.assertNotNull(courseOverview);
    }

    @Test
    public void get_courses_overview_unavailable() {
        final List<CourseOverviewDTO> coursesOverview = courseService.getCoursesOverview(false);
        final CourseOverviewDTO courseOverview = coursesOverview.stream().findFirst().get();
        Assert.assertEquals(coursesOverview.size(), courseRepository.findAll(Example.of(new CourseEntity().setAvailable(false))).size());
        Assert.assertNotNull(courseOverview);
    }

    @Test
    public void create_course_by_admin_without_principle_id() {
        final CourseDTO newCourse = new CourseDTO()
                .setSubject("TEST SUBJECT")
                .setDescription("TEST SUBJECT")
                .setCoursePrinciple(new AppUserDTO()
                        .setFirstName("Test")
                        .setLastName("Test")
                        .setEmail("test@email.com")
                        .setUserRole(UserRole.TEACHER));
        final SystemException systemException = Assert.assertThrows(SystemException.class, () -> courseService.createCourse(newCourse));
        Assert.assertEquals("Please specify course principle id", systemException.getMessage());
        Assert.assertEquals(HttpStatus.BAD_REQUEST, systemException.getServiceCode());
    }

    @Test
    public void create_course_by_admin_for_teacher() {
        final CourseDTO newCourse = new CourseDTO()
                .setSubject("TEST SUBJECT")
                .setDescription("TEST DESCRIPTION")
                .setCoursePrinciple(new AppUserDTO()
                        .setId(3L));
        final CourseDTO course = courseService.createCourse(newCourse);
        Assert.assertEquals(newCourse.getSubject(), course.getSubject());
        Assert.assertEquals(newCourse.getDescription(), course.getDescription());
        Assert.assertEquals(newCourse.getCoursePrinciple().getId(), course.getCoursePrinciple().getId());
    }

    @Test
    public void create_course_by_admin_for_principle_with_role_admin() {
        final CourseDTO newCourse = new CourseDTO()
                .setSubject("TEST SUBJECT")
                .setDescription("TEST SUBJECT")
                .setCoursePrinciple(new AppUserDTO()
                        .setId(1L));
        final SystemException systemException = Assert.assertThrows(SystemException.class, () -> courseService.createCourse(newCourse));
        Assert.assertEquals("Admin can not be course principle, please specify course principle", systemException.getMessage());
        Assert.assertEquals(HttpStatus.BAD_REQUEST, systemException.getServiceCode());
    }

    @Test
    public void add_student_as_a_teacher_to_course_with_authorities() {
        final SystemException systemException = Assert.assertThrows(SystemException.class, () -> courseService.addTeacherToCourseWithAuthorities(2L, 2L, List.of(CourseAuthorities.UPDATE_COURSE)));
        Assert.assertEquals("User does not contain required role", systemException.getMessage());
        Assert.assertEquals(HttpStatus.BAD_REQUEST, systemException.getServiceCode());
    }

    @Test
    public void add_admin_as_a_teacher_to_course_with_authorities() {
        final SystemException systemException = Assert.assertThrows(SystemException.class, () -> courseService.addTeacherToCourseWithAuthorities(5L, 2L, List.of(CourseAuthorities.UPDATE_COURSE)));
        Assert.assertEquals("User does not contain required role", systemException.getMessage());
        Assert.assertEquals(HttpStatus.BAD_REQUEST, systemException.getServiceCode());
    }

    @Test
    public void add_teacher_to_course_with_authorities() {
        courseService.addTeacherToCourseWithAuthorities(3L, 2L, List.of(CourseAuthorities.UPDATE_COURSE));
        authenticateUserWithRole(UserRole.TEACHER, 3L);
        Assert.assertTrue(userAccessValidation.isCourseTeacher(2L));
        authenticateUserWithRole(UserRole.ADMIN, 1L);
    }


    @Test
    public void register_user_to_course_by_admin() {
        courseService.registerStudentUserToCourseByAdmin(7L, 2L);
        authenticateUserWithRole(UserRole.STUDENT, 7L);
        final CourseOverviewDTO courseOverview = courseService.getCourseOverviewById(2L);
        Assert.assertNotNull(courseOverview);
        Assert.assertEquals(Long.valueOf(2), courseOverview.getId());
        authenticateUserWithRole(UserRole.ADMIN, 1L);
    }


}
