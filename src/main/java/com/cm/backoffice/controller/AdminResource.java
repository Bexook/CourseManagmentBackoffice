package com.cm.backoffice.controller;

import com.cm.common.model.dto.AppUserDTO;
import com.cm.common.model.dto.ScheduledJobReportDTO;
import com.cm.common.model.enumeration.UserSearchCriteria;
import com.cm.common.service.course.CourseService;
import com.cm.common.service.job.ScheduledJobService;
import com.cm.common.service.user.AppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminResource {

    private final AppUserService appUserService;
    private final CourseService courseService;

    private final ScheduledJobService scheduledJobService;

    @PostMapping("/user/delete")
    public void deleteAccountById(@RequestParam("accountId") final Long appUserId) {
        appUserService.deleteById(appUserId);
    }

    @PostMapping("/user/deactivate")
    public void deactivateUserAccount(@RequestParam("accountId") final Long appUserId) {
        appUserService.deactivateUserAccount(appUserId);
    }

    @PostMapping("/user/create")
    public void createUserByAdmin(@RequestBody final AppUserDTO appUser) {
        appUserService.createUserByAdmin(appUser);
    }

    @GetMapping("/user/search")
    public ResponseEntity<Set<AppUserDTO>> searchForUserByCriteria(@RequestBody(required = false) Map<UserSearchCriteria, Object> searchCriteriaKeyMap) {
        return ResponseEntity.ok().body(appUserService.searchUserByCriteria(searchCriteriaKeyMap));
    }

    @PostMapping("/course/register")
    public void registerUserToCourse(@RequestParam("accountId") final Long accountId, @RequestParam("courseId") final Long courseId) {
        courseService.registerStudentUserToCourseByAdmin(accountId, courseId);
    }


    @PostMapping("/course/delete")
    public void deleteCourse(@RequestParam("courseId") final Long courseId) {
        courseService.deleteCourseById(courseId);
    }

    @PostMapping("/course/{courseId}")
    public void changeCourseAvailabilityStatus(@PathVariable("courseId") final Long courseId, @RequestParam("available") final boolean available) {
        courseService.updateCourseAvailabilityStatus(courseId, available);
    }

    @PostMapping("/scheduled/run/{jobName}")
    public ResponseEntity<ScheduledJobReportDTO> runJobByName(@PathVariable("jobName") final String jobName) {
        return ResponseEntity.ok().body(scheduledJobService.runJobByName(jobName));
    }

    @GetMapping("/scheduled/names")
    public ResponseEntity<Set<String>> getJobsName() {
        return ResponseEntity.ok().body(scheduledJobService.getJobNames());
    }

    @GetMapping("/scheduled/report/{jobName}")
    public ResponseEntity<Set<ScheduledJobReportDTO>> getReportByJobName(@PathVariable("jobName") final String jobName) {
        return ResponseEntity.ok().body(scheduledJobService.getAllReportsByName(jobName));
    }
}
