# ExamSmart

A web-based exam management system that takes the manual work out of seating arrangements. Institutions enter their students, rooms and exams, and ExamSmart automatically generates hall-ticket numbers and assigns every student a room and seat, mixing students from different institutions so classmates rarely sit next to each other. Students then log in with their institution name, name and date of birth to see their hall ticket.

Built as a Java mini project with a Spring Boot backend, MySQL database and a plain HTML/CSS/JavaScript frontend.

## What it does

- Add institutions, students, examination rooms (with capacities) and exams
- Automatically generate a unique hall-ticket number for every student
- Allocate students to rooms and seats, respecting each room's capacity
- Spread students from the same institution apart using round-robin allocation
- Let students look up their hall ticket: exam, date, time, room and seat number
- Re-run allocation at any time without creating duplicate records

## Tech stack

| Layer | Technology |
|---|---|
| Backend | Java, Spring Boot |
| Persistence | Spring Data JPA, Hibernate |
| Database | MySQL 8 |
| API | REST (JSON) |
| Frontend | HTML, CSS, JavaScript (`fetch`) |
| Testing | Postman |

## How the seat allocation works

1. All students are grouped by their institution.
2. Students are merged in round-robin order: one from institution A, one from institution B, then back to A, and so on. This keeps students from the same institution apart wherever the data allows it.
3. Rooms are filled in order. When a room reaches its capacity, allocation moves on to the next room.
4. Each student gets a hall-ticket number in the format `HT-<examId>-<number>` (for example `HT-1-1000`) and an allocation record linking student, exam, room and seat.
5. Running allocation again deletes the old allocations for that exam first, so results are never duplicated.

If the total room capacity is smaller than the number of students, the API returns a clear error instead of leaving anyone unseated.

## Project structure

```
examsmart-backend/
├── src/main/java/com/examsmart/examsmart/
│   ├── model/          Institution, Student, Exam, Room, Allocation (JPA entities)
│   ├── repository/     Spring Data JPA repositories
│   ├── service/        AllocationService (the seat allocation logic)
│   ├── controller/     REST controllers
│   ├── WebConfig.java  CORS configuration
│   └── ExamsmartApplication.java
├── src/main/resources/application.properties
├── frontend/
│   ├── admin.html      Admin panel
│   └── student.html    Student hall-ticket page
└── pom.xml
```

Entity relationships: an Institution has many Students and many Rooms. An Allocation links one Student, one Exam and one Room, and stores the seat number.

## Getting started

### Prerequisites

- Java 21 or later
- MySQL 8
- Nothing else to install for Maven: the project includes the Maven wrapper (`mvnw`)

### 1. Create the database

```sql
CREATE DATABASE examsmart_db;
```

### 2. Set your MySQL password

The password is read from an environment variable so it is never stored in the code. On Windows PowerShell:

```powershell
$env:DB_PASSWORD="your_mysql_password"
```

The database user defaults to `root`. Change it in `src/main/resources/application.properties` if yours is different.

### 3. Run the backend

```powershell
.\mvnw.cmd spring-boot:run
```

The server starts on `http://localhost:8080` and Hibernate creates all tables automatically on the first run.

### 4. Open the frontend

With the backend running, open these files directly in your browser:

- `frontend/admin.html` to add data and run allocation
- `frontend/student.html` to look up a hall ticket

### Quick walkthrough

1. In the admin panel, add an institution, then add students and rooms using that institution's ID.
2. Add an exam.
3. Enter the exam's ID under **Run Seat Allocation** and click **Run Allocation**.
4. Open the student page and enter an institution name, student name and date of birth to see the hall ticket.

## API reference

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/institutions` | Add an institution |
| GET | `/api/institutions` | List all institutions |
| GET | `/api/institutions/{id}` | Get one institution |
| POST | `/api/students/institution/{institutionId}` | Add a student to an institution |
| GET | `/api/students` | List all students |
| GET | `/api/students/{id}` | Get one student |
| POST | `/api/rooms/institution/{institutionId}` | Add a room |
| GET | `/api/rooms` | List all rooms |
| GET | `/api/rooms/{id}` | Get one room |
| POST | `/api/exams` | Add an exam |
| GET | `/api/exams` | List all exams |
| GET | `/api/exams/{id}` | Get one exam |
| POST | `/api/allocations/run/{examId}` | Run seat allocation for an exam |
| GET | `/api/allocations` | List all allocations |
| POST | `/api/hallticket/lookup` | Look up a student's hall ticket |

Example request for the hall-ticket lookup:

```json
POST /api/hallticket/lookup

{
    "institutionName": "Pragathi College",
    "name": "Kalyan",
    "dateOfBirth": "2003-05-15"
}
```

Example response:

```json
{
    "student": { "name": "Kalyan", "dateOfBirth": "2003-05-15", "hallTicketNumber": "HT-1-1000", "id": 1 },
    "exam": { "examName": "Data Structures", "examDate": "2026-10-15", "examTime": "10:00 AM", "id": 1 },
    "room": { "roomNumber": "R101", "capacity": 30, "id": 1 },
    "seatNumber": 1,
    "id": 1
}
```


## Known limitations

- Student login uses institution name, name and date of birth, which is not unique or secure. A production system would use student IDs with a password or OTP.
- The admin panel has no authentication.
- Allocation currently uses every student and every room in the database for the chosen exam.
- CORS is open to all origins to keep local development simple.

## Future improvements

- Proper login with Spring Security and separate admin and student roles
- Downloadable hall tickets as PDF
- Bulk student upload from CSV
- Support for several examination centres and multiple exams running at the same time
- Unit tests for the allocation service

## Author

Built by Kalyan. GitHub: https://github.com/25A31A05CO
