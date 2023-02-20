package com.cm.backoffice;

import com.cm.common.model.domain.AppUserEntity;
import com.cm.common.model.enumeration.UserRole;
import com.cm.common.security.AppUserDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;

public class AuthenticationTestUtil {

    public static void authenticateUserWithRole(final UserRole userRole, final Long id) {
        final UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(getAppUserDetails(userRole,id ), null, null);
        SecurityContextHolder.getContext().setAuthentication(token);
    }

    private static AppUserDetails getAppUserDetails(final UserRole userRole, final Long id) {
        return new AppUserDetails(new AppUserEntity()
                .setUserRole(userRole)
                .setId(id)
                .setFirstName("USER")
                .setLastName("USER")
                .setEmailVerified(true)
                .setActive(true)
                .setEmail("user@gmail.com")
                .setCreatedDate(LocalDateTime.now())
                .setPassword("$2a$12$ok0yBpD4F44qy5a0CTL9xewXj.JEKkKahd9t5PffAKvgLfPpDr3Yu")//test
        );
    }

}
