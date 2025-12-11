package com.platform.talent.interview.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "interviews")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Interview {
    
    @Id
    private String interviewId;
    
    @Column(nullable = false)
    private String organizationId;
    
    @Column(nullable = false)
    private String applicationId;
    
    @Column(nullable = false)
    private String candidateId;
    
    private String candidateName;
    private String candidateEmail;
    
    @Column(nullable = false)
    private String jobId;
    
    private String jobTitle;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InterviewType type;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InterviewStatus status;
    
    @Column(nullable = false)
    private LocalDateTime scheduledAt;
    
    private Integer durationMinutes;
    
    private String location;
    private String meetingUrl;
    private String meetingId;
    
    @Column(nullable = false)
    private String interviewerIds; // Comma-separated
    
    private String interviewerNames;
    
    private Integer overallScore;
    
    @Column(length = 4000)
    private String feedback;
    
    @Enumerated(EnumType.STRING)
    private InterviewRecommendation recommendation;
    
    @Column(length = 2000)
    private String notes;
    
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
    
    private LocalDate createdDate;
    private LocalDate lastModifiedDate;
    private String createdBy;
}

