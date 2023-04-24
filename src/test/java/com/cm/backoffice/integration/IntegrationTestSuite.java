package com.cm.backoffice.integration;

import com.cm.backoffice.integration.course.CourseServiceActivityAdminTest;
import com.cm.backoffice.integration.course.CourseServiceActivityStudentTest;
import com.cm.backoffice.integration.course.CourseServiceActivityTeacherTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@RunWith(Suite.class)
@Suite.SuiteClasses({
        CourseServiceActivityAdminTest.class,
        CourseServiceActivityStudentTest.class,
        CourseServiceActivityTeacherTest.class
})
@ActiveProfiles("test")
public class IntegrationTestSuite {

}
