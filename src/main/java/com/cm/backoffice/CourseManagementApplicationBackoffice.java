package com.cm.backoffice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication(scanBasePackages = {"com.cm.common.*", "com.cm.backoffice.*"})
@EnableJpaRepositories({"com.cm.common.*"})
@EntityScan("com.cm.common.*")
public class CourseManagementApplicationBackoffice {

    public static void main(String[] args) {
        SpringApplication.run(CourseManagementApplicationBackoffice.class, args);

    }

}
