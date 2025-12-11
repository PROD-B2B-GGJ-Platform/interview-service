package com.platform.talent.interview.service;

import com.platform.talent.interview.domain.model.*;
import com.platform.talent.interview.repository.InterviewRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
public class InterviewService {

    private final InterviewRepository interviewRepository;
    
    @Autowired(required = false)
    private KafkaTemplate<String, Object> kafkaTemplate;
    
    public InterviewService(InterviewRepository interviewRepository) {
        this.interviewRepository = interviewRepository;
    }

    @Transactional
    public Interview scheduleInterview(Interview interview, String userId) {
        log.info("Scheduling interview for application: {}", interview.getApplicationId());

        interview.setInterviewId(UUID.randomUUID().toString());
        interview.setStatus(InterviewStatus.SCHEDULED);
        interview.setCreatedDate(LocalDate.now());
        interview.setCreatedBy(userId);

        // Generate meeting URL if not provided
        if (interview.getMeetingUrl() == null) {
            interview.setMeetingUrl("https://meet.platform.com/" + interview.getInterviewId());
        }

        Interview saved = interviewRepository.save(interview);

        // Publish event for calendar integration
        if (kafkaTemplate != null) {
            kafkaTemplate.send("talent.interview.scheduled", Map.of(
                "interviewId", saved.getInterviewId(),
                "applicationId", saved.getApplicationId(),
                "candidateId", saved.getCandidateId(),
                "candidateName", saved.getCandidateName(),
                "candidateEmail", saved.getCandidateEmail(),
                "jobTitle", saved.getJobTitle(),
                "startTime", saved.getScheduledAt().toString(),
                "endTime", saved.getScheduledAt().plusMinutes(saved.getDurationMinutes()).toString(),
                "meetingUrl", saved.getMeetingUrl(),
                "interviewerId", saved.getInterviewerIds(),
                "organizationId", saved.getOrganizationId()
            ));
        }

        return saved;
    }

    @Transactional
    public Interview rescheduleInterview(String organizationId, String interviewId, 
                                         LocalDateTime newTime, String reason) {
        Interview interview = getInterview(organizationId, interviewId);
        
        interview.setScheduledAt(newTime);
        interview.setStatus(InterviewStatus.RESCHEDULED);
        interview.setNotes(reason);
        interview.setLastModifiedDate(LocalDate.now());

        Interview saved = interviewRepository.save(interview);

        if (kafkaTemplate != null) {
            kafkaTemplate.send("talent.interview.rescheduled", Map.of(
                "interviewId", interviewId,
                "candidateEmail", interview.getCandidateEmail(),
                "newTime", newTime,
                "reason", reason
            ));
        }

        return saved;
    }

    @Transactional
    public Interview confirmInterview(String organizationId, String interviewId) {
        Interview interview = getInterview(organizationId, interviewId);
        interview.setStatus(InterviewStatus.CONFIRMED);
        interview.setLastModifiedDate(LocalDate.now());
        return interviewRepository.save(interview);
    }

    @Transactional
    public Interview startInterview(String organizationId, String interviewId) {
        Interview interview = getInterview(organizationId, interviewId);
        interview.setStatus(InterviewStatus.IN_PROGRESS);
        interview.setStartedAt(LocalDateTime.now());
        interview.setLastModifiedDate(LocalDate.now());
        return interviewRepository.save(interview);
    }

    @Transactional
    public Interview completeInterview(String organizationId, String interviewId,
                                       Integer score, String feedback, 
                                       InterviewRecommendation recommendation) {
        Interview interview = getInterview(organizationId, interviewId);
        
        interview.setStatus(InterviewStatus.COMPLETED);
        interview.setCompletedAt(LocalDateTime.now());
        interview.setOverallScore(score);
        interview.setFeedback(feedback);
        interview.setRecommendation(recommendation);
        interview.setLastModifiedDate(LocalDate.now());

        Interview saved = interviewRepository.save(interview);

        if (kafkaTemplate != null) {
            kafkaTemplate.send("talent.interview.completed", Map.of(
                "interviewId", interviewId,
                "applicationId", saved.getApplicationId(),
                "candidateId", saved.getCandidateId(),
                "score", score,
                "recommendation", recommendation,
                "organizationId", organizationId
            ));
        }

        return saved;
    }

    @Transactional
    public Interview cancelInterview(String organizationId, String interviewId, String reason) {
        Interview interview = getInterview(organizationId, interviewId);
        
        interview.setStatus(InterviewStatus.CANCELLED);
        interview.setNotes(reason);
        interview.setLastModifiedDate(LocalDate.now());

        Interview saved = interviewRepository.save(interview);

        if (kafkaTemplate != null) {
            kafkaTemplate.send("talent.interview.cancelled", Map.of(
                "interviewId", interviewId,
                "candidateEmail", interview.getCandidateEmail(),
                "reason", reason
            ));
        }

        return saved;
    }

    @Transactional
    public Interview markNoShow(String organizationId, String interviewId) {
        Interview interview = getInterview(organizationId, interviewId);
        interview.setStatus(InterviewStatus.NO_SHOW);
        interview.setLastModifiedDate(LocalDate.now());
        return interviewRepository.save(interview);
    }

    @Transactional(readOnly = true)
    public Interview getInterview(String organizationId, String interviewId) {
        return interviewRepository.findById(interviewId)
            .filter(i -> i.getOrganizationId().equals(organizationId))
            .orElseThrow(() -> new RuntimeException("Interview not found"));
    }

    @Transactional(readOnly = true)
    public List<Interview> getAllInterviews(String organizationId) {
        return interviewRepository.findByOrganizationId(organizationId);
    }

    @Transactional(readOnly = true)
    public List<Interview> getInterviewsByApplication(String applicationId) {
        return interviewRepository.findByApplicationId(applicationId);
    }

    @Transactional(readOnly = true)
    public List<Interview> getUpcomingInterviews(String organizationId) {
        return interviewRepository.findUpcoming(organizationId, LocalDateTime.now());
    }

    @Transactional(readOnly = true)
    public List<Interview> getMyInterviews(String interviewerId) {
        return interviewRepository.findByInterviewer(interviewerId, LocalDateTime.now());
    }

    @Transactional(readOnly = true)
    public List<Interview> getInterviewsByDateRange(String organizationId, 
                                                     LocalDateTime start, LocalDateTime end) {
        return interviewRepository.findByDateRange(organizationId, start, end);
    }
}

