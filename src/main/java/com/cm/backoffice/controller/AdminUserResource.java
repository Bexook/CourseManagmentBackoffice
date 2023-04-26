package com.cm.backoffice.controller;

import com.cm.common.model.dto.AppUserDTO;
import com.cm.common.model.enumeration.UserSearchCriteria;
import com.cm.common.service.user.AppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminUserResource {

    private final AppUserService appUserService;

    @DeleteMapping("/user/delete")
    public void deleteAccountById(@RequestParam("accountId") final Long appUserId) {
        appUserService.deleteById(appUserId);
    }

    @PatchMapping("/user/deactivate")
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

}
