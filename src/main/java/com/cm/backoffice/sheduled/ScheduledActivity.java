package com.cm.backoffice.sheduled;

import com.cm.common.model.domain.AppUserEntity;
import com.cm.common.model.dto.ScheduledJobReportDTO;
import com.cm.common.model.enumeration.UserRole;
import com.cm.common.security.AppUserDetails;
import com.cm.common.service.SchedulerAuthenticationExecutor;
import com.cm.common.service.course.CourseService;
import com.cm.common.service.job.ScheduledJobService;
import com.cm.common.service.user.AppUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@EnableScheduling
@RequiredArgsConstructor
public class ScheduledActivity {
    private final CourseService courseService;
    private final AppUserService appUserService;
    private final ScheduledJobService scheduledJobService;


    @Scheduled(cron = "0 0 0 ? * SAT", zone = "UTC") // Every Saturday at 00:00
    public void scheduledLessonProgress() {
        log.info("=================== Start lesson progress scheduled activity ===================");
        scheduledJobService.saveReport(runAsScheduledJob(courseService::progressCourseLessonIndex));
        log.info("=================== End lesson progress scheduled activity ===================");
    }


    @Scheduled(cron = "0 0 12 LW * ?", zone = "UTC") // Every month on the last weekday, at noon
    public void scheduledAccountCleanUp() {
        log.info("=================== Start scheduled not verified account deletion ===================");
        scheduledJobService.saveReport(runAsScheduledJob(appUserService::dropNotVerifiedUsers));
        log.info("=================== End not verified account deletion ===================");
    }

    @Scheduled(cron = "0 0 0 * * ?", zone = "UTC") // Every day at midnight
    public void scheduledAccountDeletionWarning() {
        log.info("=================== Start scheduled account deletion warning notification===================");
        scheduledJobService.saveReport(runAsScheduledJob(appUserService::sendAccountDeletionWarningNotification));
        log.info("=================== End scheduled account deletion warning notification===================");
    }


    private ScheduledJobReportDTO runAsScheduledJob(final SchedulerAuthenticationExecutor func) {
        final Authentication originalAuthentication = SecurityContextHolder.getContext().getAuthentication();
        final AnonymousAuthenticationToken token = new AnonymousAuthenticationToken("SYSTEM", getUserDetailsForJob(),
                List.of(new SimpleGrantedAuthority("SCHEDULED_JOB")));
        SecurityContextHolder.getContext().setAuthentication(token);
        final ScheduledJobReportDTO report = func.runAsJob();
        SecurityContextHolder.getContext().setAuthentication(originalAuthentication);
        return report;
    }


    private AppUserDetails getUserDetailsForJob() {
        return new AppUserDetails(new AppUserEntity()
                .setUserRole(UserRole.SCHEDULED_JOB)
                .setEmail("SCHEDULED_JOB"));
    }
}

