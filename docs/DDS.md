# Interview Service - Design Document Specification (DDS)

## Document Information

| Field | Value |
|-------|-------|
| Version | 10.0.0.1 |
| Last Updated | 2025-12-11 |
| Status | Approved |
| Owner | Talent & Recruitment Team |

---

## 1. Overview

The Interview Service manages interview scheduling, interviewer assignments, and feedback collection for the recruitment process.

### 1.1 Key Features

- Interview scheduling
- Interviewer assignment
- Calendar integration (Google, Outlook)
- Feedback collection
- Interview panel management

---

## 2. Architecture

### 2.1 Technology Stack

| Component | Technology | Version |
|-----------|------------|---------|
| Runtime | Java | 21+ |
| Framework | Spring Boot | 3.2.0 |
| Database | PostgreSQL | 15+ |
| Port | 8095 | (Changed from 8094 to avoid conflict) |

---

## 3. Data Model

### 3.1 Database Schema

#### Table: interviews

| Column | Type | Description |
|--------|------|-------------|
| id | UUID | PRIMARY KEY |
| organization_id | UUID | Multi-tenant isolation |
| application_id | UUID | FK to applications |
| interview_type | VARCHAR(50) | PHONE, VIDEO, ONSITE, PANEL |
| scheduled_at | TIMESTAMP | Interview time |
| duration_minutes | INTEGER | Duration |
| location | VARCHAR(255) | Room/link |
| status | VARCHAR(20) | SCHEDULED, COMPLETED, CANCELLED |
| meeting_link | VARCHAR(500) | Video call link |
| notes | TEXT | Pre-interview notes |

#### Table: interviewers

| Column | Type | Description |
|--------|------|-------------|
| id | UUID | PRIMARY KEY |
| interview_id | UUID | FK to interviews |
| user_id | UUID | Interviewer user ID |
| role | VARCHAR(50) | LEAD, PANELIST, OBSERVER |
| status | VARCHAR(20) | PENDING, CONFIRMED, DECLINED |

#### Table: interview_feedback

| Column | Type | Description |
|--------|------|-------------|
| id | UUID | PRIMARY KEY |
| interview_id | UUID | FK to interviews |
| interviewer_id | UUID | FK to interviewers |
| rating | INTEGER | 1-5 rating |
| recommendation | VARCHAR(20) | STRONG_HIRE, HIRE, NO_HIRE, STRONG_NO_HIRE |
| strengths | TEXT | Candidate strengths |
| concerns | TEXT | Candidate concerns |
| notes | TEXT | Detailed feedback |
| submitted_at | TIMESTAMP | When submitted |

---

## 4. API Design

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/v1/interviews` | Schedule interview |
| GET | `/api/v1/interviews/{id}` | Get interview |
| PUT | `/api/v1/interviews/{id}` | Update interview |
| DELETE | `/api/v1/interviews/{id}` | Cancel interview |
| POST | `/api/v1/interviews/{id}/interviewers` | Add interviewer |
| POST | `/api/v1/interviews/{id}/feedback` | Submit feedback |
| GET | `/api/v1/interviews/{id}/feedback` | Get all feedback |

---

## 5. Interview Types

| Type | Description |
|------|-------------|
| PHONE | Phone screening call |
| VIDEO | Video conference interview |
| ONSITE | In-person interview |
| PANEL | Multiple interviewers |
| TECHNICAL | Technical assessment |
| CULTURAL | Culture fit interview |

---

## 6. Calendar Integration

### 6.1 Supported Calendars

- Google Calendar (OAuth 2.0)
- Microsoft Outlook (Microsoft Graph API)
- Custom calendar webhook

### 6.2 Flow

```
Schedule Interview → Check Availability → Create Calendar Event → 
Send Invites → Sync Updates
```

---

## 7. Events Published

| Topic | Trigger |
|-------|---------|
| talent.interview.scheduled | New interview scheduled |
| talent.interview.completed | Interview completed |
| talent.interview.cancelled | Interview cancelled |
| talent.interview.feedback-submitted | Feedback added |

---

## 8. References

- [Google Calendar API](https://developers.google.com/calendar)
- [Microsoft Graph API](https://docs.microsoft.com/en-us/graph/)

