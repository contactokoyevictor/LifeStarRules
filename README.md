# LifeStarRules

A **Java patient-engagement platform** for rule-driven SMS/email cadence, blood-pressure tracking, and consumer lifecycle messaging — built around a **Drools** rules engine and XML servlet connectors.

Originally developed for a Novartis **GetOnTrack**-style programme: segment patients, schedule reminders, process inbound SMS (MO), send outbound messages (MT), and keep opt-in / opt-out and BP readings in sync with MySQL.

## What it covers

| Domain | Capabilities |
|--------|----------------|
| **Rules & cadence** | Drools (`Cadence.drl`) evaluates users by segment, week-in-programme, and calendar factors; schedules SMS/email kits |
| **Patient segments** | Segment codes for sceptics, students, independents, strugglers — driving different message paths |
| **SMS messaging** | MO / MT / opt-in / opt-out / broadcast servlet APIs with XSD-backed request/response schemas |
| **BP tracker** | Inbound SMS BP readings (systolic/diastolic), validation, tips, and safety escalation messaging |
| **Consumer profile** | Get/set consumer details, page elements for partner integrations |
| **Enrollment & typing** | Enrollment acceptance, multi-question typing tool for segmentation |
| **Reminders** | Custom schedulers (`RemindSender`, daily iterators) for timed reminder delivery |
| **Auth helpers** | Login, forgot password, set new password data objects |

## Architecture

- **Drools KnowledgeBase** — loads `Cadence.drl`, inserts `User` facts, fires segment/time-based rules
- **Cadence layer** — pulls opted-in users from MySQL and invokes the rules engine
- **Servlet connectors** — XML in/out endpoints (`mo`, `mt`, `optin`, `optout`, `broadcast`, consumer/page APIs)
- **Prep / messaging layer** — request parsing, SQL, and response builders for each channel
- **JBoss** datasources + Ant / NetBeans Java EE web layout

```
src/java/com/novartis/
├── cadence/
│   ├── rulesengine/     Drools RulesEngine + Cadence.drl
│   └── database/        User model, Cadence orchestration, connection pool
├── connectors/
│   ├── servlets/        HTTP XML APIs (MO/MT/opt-in/out/broadcast/…)
│   ├── prep/            Request processors
│   ├── database/        Domain DB objects (drugs, BP buddy, typing, enrollment)
│   ├── schemas/         XSD contracts
│   └── schedulers/      Reminder scheduling
└── messaging/           SMS BP tracker, tips, MO/MT helpers
```

## Stack

- **Java** servlets / JSP
- **Drools** rules engine
- **MySQL** via JNDI datasource (JBoss)
- **XML / XSD** messaging contracts
- **Apache Ant** / NetBeans / JBoss AS

## Notes

Portfolio snapshot of patient-engagement and rules-driven messaging work. Local DB credentials belong in server config / environment — use placeholders, never commit live secrets.
