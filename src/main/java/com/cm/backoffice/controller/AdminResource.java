package com.cm.backoffice.controller;

import com.cm.common.model.dto.AppUserDTO;
import com.cm.common.model.enumeration.UserSearchCriteria;
import com.cm.common.service.course.CourseService;
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

    @PostMapping("/user/search")
    public ResponseEntity<Set<AppUserDTO>> searchForUserByCriteria(@RequestBody(required = false) Map<UserSearchCriteria, Object> searchCriteriaKeyMap) {
        return ResponseEntity.ok().body(appUserService.searchUserByCriteria(searchCriteriaKeyMap));
    }

    @PostMapping("/course/register")
    public void registerUserToCourse(@RequestParam("accountId") final Long accountId, @RequestParam("courseId") final Long courseId) {
        courseService.registerStudentUserToCourseByAdmin(accountId, courseId);
    }
}
