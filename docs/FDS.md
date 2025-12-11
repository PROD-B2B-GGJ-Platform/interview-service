# Interview Service - Functional Design Specification (FDS)

## Document Information

| Field | Value |
|-------|-------|
| Version | 10.0.0.1 |
| Last Updated | 2025-12-11 |
| Status | Approved |

---

## 1. Functional Requirements

### FR-001: Schedule Interview

Schedule an interview for a job application.

**Input:**
- applicationId (required)
- interviewType (PHONE, VIDEO, ONSITE, PANEL)
- scheduledAt (datetime)
- durationMinutes
- location / meetingLink
- interviewerIds[]

**Business Rules:**
- Check interviewer availability
- Create calendar events
- Send invitations to candidate and interviewers

---

### FR-002: Assign Interviewers

Add interviewers to scheduled interview.

**Roles:**
- LEAD: Primary interviewer
- PANELIST: Panel member
- OBSERVER: Observing only

---

### FR-003: Submit Feedback

Interviewer submits feedback after interview.

**Input:**
- rating (1-5)
- recommendation (STRONG_HIRE, HIRE, NO_HIRE, STRONG_NO_HIRE)
- strengths, concerns, notes

**Business Rules:**
- Only assigned interviewers can submit
- Feedback required within 48 hours
- One feedback per interviewer per interview

---

### FR-004: Cancel Interview

Cancel a scheduled interview.

**Business Rules:**
- Update calendar events
- Notify all participants
- Record cancellation reason

---

## 2. Interview Workflow

```
SCHEDULED → IN_PROGRESS → COMPLETED
     │
     └→ CANCELLED
```

---

## 3. Acceptance Criteria

- [ ] Interviews scheduled with calendar integration
- [ ] Interviewers assigned with roles
- [ ] Feedback collected and stored
- [ ] Events published on status changes
- [ ] Notifications sent to participants

