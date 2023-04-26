package com.cm.backoffice.controller;

import com.cm.common.service.course.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/course")
@RequiredArgsConstructor
public class AdminCourseResource {

    private final CourseService courseService;

    @PostMapping("/register")
    public void registerUserToCourse(@RequestParam("accountId") final Long accountId, @RequestParam("courseId") final Long courseId) {
        courseService.registerStudentUserToCourseByAdmin(accountId, courseId);
    }


    @DeleteMapping("/delete")
    public void deleteCourse(@RequestParam("courseId") final Long courseId) {
        courseService.deleteCourseById(courseId);
    }

    @PutMapping("/{courseId}")
    public void changeCourseAvailabilityStatus(@PathVariable("courseId") final Long courseId, @RequestParam("available") final boolean available) {
        courseService.updateCourseAvailabilityStatus(courseId, available);
    }

}
