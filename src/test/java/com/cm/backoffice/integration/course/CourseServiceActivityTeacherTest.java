package com.cm.backoffice.integration.course;


import com.cm.common.exception.SystemException;
import com.cm.common.model.dto.AppUserDTO;
import com.cm.common.model.dto.CourseDTO;
import com.cm.common.model.enumeration.CourseAuthorities;
import com.cm.common.model.enumeration.UserRole;
import com.cm.common.security.AppUserDetails;
import com.cm.common.security.management.access.UserAccessValidation;
import com.cm.common.service.course.CourseService;
import com.cm.common.util.AuthorizationUtil;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.List;

import static com.cm.backoffice.AuthenticationTestUtil.authenticateUserWithRole;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class CourseServiceActivityTeacherTest {
    @Autowired
    private UserAccessValidation userAccessValidation;
    @Autowired
    private CourseService courseService;

    @PostConstruct
    private void postConstruct() {
        authenticateUserWithRole(UserRole.TEACHER, 4L);
    }

    @Test
    public void create_course_by_teacher_for_itself_as_principle() {
        final AppUserDetails currentUser = (AppUserDetails) AuthorizationUtil.getCurrentUser();
        final CourseDTO newCourse = new CourseDTO()
                .setSubject("TEST SUBJECT")
                .setDescription("TEST DESCRIPTION");
        final CourseDTO course = courseService.createCourse(newCourse);
        Assert.assertEquals(newCourse.getSubject(), course.getSubject());
        Assert.assertEquals(newCourse.getDescription(), course.getDescription());
        Assert.assertEquals(course.getCoursePrinciple().getId(), currentUser.getUserId());
    }

    @Test
    public void create_course_by_teacher_for_another_teacher_as_principle() {
        final CourseDTO newCourse = new CourseDTO()
                .setCoursePrinciple(new AppUserDTO().setId(3L))
                .setSubject("TEST SUBJECT")
                .setDescription("TEST DESCRIPTION");
        final SystemException systemException = Assert.assertThrows(SystemException.class, () -> courseService.createCourse(newCourse));
        Assert.assertEquals("Teacher is only capable of creating courses for himself. Ask system administrator to create create course for somebody else", systemException.getMessage());
        Assert.assertEquals(HttpStatus.BAD_REQUEST, systemException.getServiceCode());
    }

    @Test
    public void add_teacher_to_course_with_authorities_by_not_course_principle() {
        Assert.assertThrows(AccessDeniedException.class, () -> courseService.addTeacherToCourseWithAuthorities(3L, 2L, List.of(CourseAuthorities.UPDATE_COURSE)));
    }


    @Test
    public void add_teacher_to_course_by_principle() {
        authenticateUserWithRole(UserRole.TEACHER, 3L);
        courseService.addTeacherToCourseWithAuthorities(4L, 2L, List.of(CourseAuthorities.UPDATE_COURSE));
        authenticateUserWithRole(UserRole.TEACHER, 4L);
        Assert.assertTrue(userAccessValidation.isCourseTeacher(2L));
    }

}