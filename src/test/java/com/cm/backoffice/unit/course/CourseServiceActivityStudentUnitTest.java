package com.cm.backoffice.unit.course;

import com.cm.common.exception.SystemException;
import com.cm.common.model.dto.CourseOverviewDTO;
import com.cm.common.model.enumeration.UserRole;
import com.cm.common.service.course.CourseService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.PostConstruct;

import static com.cm.backoffice.AuthenticationTestUtil.authenticateUserWithRole;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
public class CourseServiceActivityStudentUnitTest {
    @Autowired
    private CourseService courseService;

    @PostConstruct
    private void postConstruct() {
        authenticateUserWithRole(UserRole.STUDENT, 2L);
    }


    @Test
    public void register_user_to_course() {
        courseService.registerStudentUserToCourse(2L);
        final CourseOverviewDTO courseOverview = courseService.getCourseOverviewById(2L);
        Assert.assertNotNull(courseOverview);
        Assert.assertEquals(Long.valueOf(2), courseOverview.getId());
    }

    @Test
    public void register_already_registered_user_to_course() {
        authenticateUserWithRole(UserRole.STUDENT, 6L);
        final SystemException systemException = Assert.assertThrows(SystemException.class, () -> courseService.registerStudentUserToCourse(2L));
        Assert.assertEquals("User already registered", systemException.getMessage());
        Assert.assertEquals(HttpStatus.BAD_REQUEST, systemException.getServiceCode());
        authenticateUserWithRole(UserRole.STUDENT, 2L);
    }

    @Test
    public void register_user_to_non_existing_course() {
        final SystemException systemException = Assert.assertThrows(SystemException.class, () -> courseService.registerStudentUserToCourse(98482L));
        Assert.assertEquals("Course does not exist", systemException.getMessage());
        Assert.assertEquals(HttpStatus.BAD_REQUEST, systemException.getServiceCode());
    }


}
