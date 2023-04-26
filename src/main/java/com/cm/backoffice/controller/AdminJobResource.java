package com.cm.backoffice.controller;

import com.cm.common.model.dto.ScheduledJobReportDTO;
import com.cm.common.service.job.ScheduledJobService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/admin/scheduled")
@RequiredArgsConstructor
public class AdminJobResource {

    private final ScheduledJobService scheduledJobService;

    @PatchMapping("/run/{jobName}")
    public ResponseEntity<ScheduledJobReportDTO> runJobByName(@PathVariable("jobName") final String jobName) {
        return ResponseEntity.ok().body(scheduledJobService.runJobByName(jobName));
    }

    @GetMapping("/names")
    public ResponseEntity<Set<String>> getJobsName() {
        return ResponseEntity.ok().body(scheduledJobService.getJobNames());
    }

    @GetMapping("/report/{jobName}")
    public ResponseEntity<Set<ScheduledJobReportDTO>> getReportByJobName(@PathVariable("jobName") final String jobName) {
        return ResponseEntity.ok().body(scheduledJobService.getAllReportsByName(jobName));
    }

}
