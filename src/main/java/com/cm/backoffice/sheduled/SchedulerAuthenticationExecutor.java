package com.cm.backoffice.sheduled;

@FunctionalInterface
public interface SchedulerAuthenticationExecutor {
    void runAsJob();

}
