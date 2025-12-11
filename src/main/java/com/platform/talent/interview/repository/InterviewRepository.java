package com.platform.talent.interview.repository;

import com.platform.talent.interview.domain.model.Interview;
import com.platform.talent.interview.domain.model.InterviewStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface InterviewRepository extends JpaRepository<Interview, String> {
    List<Interview> findByOrganizationId(String organizationId);
    List<Interview> findByApplicationId(String applicationId);
    List<Interview> findByCandidateId(String candidateId);
    List<Interview> findByOrganizationIdAndStatus(String organizationId, InterviewStatus status);
    
    @Query("SELECT i FROM Interview i WHERE i.interviewerIds LIKE %:interviewerId% AND i.scheduledAt >= :from ORDER BY i.scheduledAt")
    List<Interview> findByInterviewer(String interviewerId, LocalDateTime from);
    
    @Query("SELECT i FROM Interview i WHERE i.organizationId = :orgId AND i.scheduledAt BETWEEN :start AND :end ORDER BY i.scheduledAt")
    List<Interview> findByDateRange(String orgId, LocalDateTime start, LocalDateTime end);
    
    @Query("SELECT i FROM Interview i WHERE i.organizationId = :orgId AND i.status = 'SCHEDULED' AND i.scheduledAt >= :now ORDER BY i.scheduledAt")
    List<Interview> findUpcoming(String orgId, LocalDateTime now);
}

