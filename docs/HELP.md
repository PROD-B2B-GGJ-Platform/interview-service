# Interview Service - Help Guide

## Quick Start

```bash
git clone https://github.com/PROD-B2B-GGJ-Platform/interview-service.git
cd interview-service

export DB_HOST=localhost
export GOOGLE_CLIENT_ID=your-client-id
export GOOGLE_CLIENT_SECRET=your-secret

mvn spring-boot:run
```

Access: http://localhost:8095/swagger-ui.html

---

## API Examples

### Schedule Interview

```bash
curl -X POST http://localhost:8095/api/v1/interviews \
  -H "Content-Type: application/json" \
  -d '{
    "applicationId": "app-uuid",
    "interviewType": "VIDEO",
    "scheduledAt": "2025-12-15T10:00:00Z",
    "durationMinutes": 60,
    "meetingLink": "https://meet.google.com/xxx",
    "interviewerIds": ["user-1", "user-2"]
  }'
```

### Submit Feedback

```bash
curl -X POST http://localhost:8095/api/v1/interviews/{id}/feedback \
  -H "Content-Type: application/json" \
  -d '{
    "rating": 4,
    "recommendation": "HIRE",
    "strengths": "Strong technical skills, good communication",
    "concerns": "May need mentorship on system design",
    "notes": "Recommend moving forward"
  }'
```

---

## Interview Types

| Type | Duration | Participants |
|------|----------|--------------|
| PHONE | 30 min | 1 interviewer |
| VIDEO | 45-60 min | 1-2 interviewers |
| ONSITE | 3-4 hours | Multiple |
| PANEL | 60 min | 3+ interviewers |

---

## Recommendation Scale

| Value | Meaning |
|-------|---------|
| STRONG_HIRE | Exceptional candidate |
| HIRE | Good fit, recommend hiring |
| NO_HIRE | Not a fit for this role |
| STRONG_NO_HIRE | Significant concerns |

---

## Support

GitHub: https://github.com/PROD-B2B-GGJ-Platform/interview-service

