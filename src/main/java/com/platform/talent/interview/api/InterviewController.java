package com.platform.talent.interview.api;

import com.platform.talent.interview.domain.model.Interview;
import com.platform.talent.interview.domain.model.InterviewRecommendation;
import com.platform.talent.interview.service.InterviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/interviews")
@RequiredArgsConstructor
public class InterviewController {

    private final InterviewService interviewService;

    @PostMapping
    public ResponseEntity<Interview> scheduleInterview(
        @RequestHeader("X-Organization-Id") String organizationId,
        @RequestHeader("X-User-Id") String userId,
        @RequestBody Interview interview
    ) {
        interview.setOrganizationId(organizationId);
        return ResponseEntity.ok(interviewService.scheduleInterview(interview, userId));
    }

    @GetMapping
    public ResponseEntity<List<Interview>> getAllInterviews(
        @RequestHeader("X-Organization-Id") String organizationId
    ) {
        return ResponseEntity.ok(interviewService.getAllInterviews(organizationId));
    }

    @GetMapping("/{interviewId}")
    public ResponseEntity<Interview> getInterview(
        @RequestHeader("X-Organization-Id") String organizationId,
        @PathVariable String interviewId
    ) {
        return ResponseEntity.ok(interviewService.getInterview(organizationId, interviewId));
    }

    @GetMapping("/application/{applicationId}")
    public ResponseEntity<List<Interview>> getByApplication(@PathVariable String applicationId) {
        return ResponseEntity.ok(interviewService.getInterviewsByApplication(applicationId));
    }

    @GetMapping("/upcoming")
    public ResponseEntity<List<Interview>> getUpcoming(
        @RequestHeader("X-Organization-Id") String organizationId
    ) {
        return ResponseEntity.ok(interviewService.getUpcomingInterviews(organizationId));
    }

    @GetMapping("/my-schedule")
    public ResponseEntity<List<Interview>> getMyInterviews(
        @RequestHeader("X-User-Id") String userId
    ) {
        return ResponseEntity.ok(interviewService.getMyInterviews(userId));
    }

    @GetMapping("/range")
    public ResponseEntity<List<Interview>> getByDateRange(
        @RequestHeader("X-Organization-Id") String organizationId,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end
    ) {
        return ResponseEntity.ok(interviewService.getInterviewsByDateRange(organizationId, start, end));
    }

    @PostMapping("/{interviewId}/reschedule")
    public ResponseEntity<Interview> reschedule(
        @RequestHeader("X-Organization-Id") String organizationId,
        @PathVariable String interviewId,
        @RequestBody Map<String, String> body
    ) {
        LocalDateTime newTime = LocalDateTime.parse(body.get("scheduledAt"));
        String reason = body.get("reason");
        return ResponseEntity.ok(interviewService.rescheduleInterview(organizationId, interviewId, newTime, reason));
    }

    @PostMapping("/{interviewId}/confirm")
    public ResponseEntity<Interview> confirm(
        @RequestHeader("X-Organization-Id") String organizationId,
        @PathVariable String interviewId
    ) {
        return ResponseEntity.ok(interviewService.confirmInterview(organizationId, interviewId));
    }

    @PostMapping("/{interviewId}/start")
    public ResponseEntity<Interview> start(
        @RequestHeader("X-Organization-Id") String organizationId,
        @PathVariable String interviewId
    ) {
        return ResponseEntity.ok(interviewService.startInterview(organizationId, interviewId));
    }

    @PostMapping("/{interviewId}/complete")
    public ResponseEntity<Interview> complete(
        @RequestHeader("X-Organization-Id") String organizationId,
        @PathVariable String interviewId,
        @RequestBody Map<String, Object> body
    ) {
        Integer score = (Integer) body.get("score");
        String feedback = (String) body.get("feedback");
        InterviewRecommendation recommendation = InterviewRecommendation.valueOf((String) body.get("recommendation"));
        return ResponseEntity.ok(interviewService.completeInterview(
            organizationId, interviewId, score, feedback, recommendation));
    }

    @PostMapping("/{interviewId}/cancel")
    public ResponseEntity<Interview> cancel(
        @RequestHeader("X-Organization-Id") String organizationId,
        @PathVariable String interviewId,
        @RequestBody Map<String, String> body
    ) {
        return ResponseEntity.ok(interviewService.cancelInterview(
            organizationId, interviewId, body.get("reason")));
    }

    @PostMapping("/{interviewId}/no-show")
    public ResponseEntity<Interview> markNoShow(
        @RequestHeader("X-Organization-Id") String organizationId,
        @PathVariable String interviewId
    ) {
        return ResponseEntity.ok(interviewService.markNoShow(organizationId, interviewId));
    }
}

