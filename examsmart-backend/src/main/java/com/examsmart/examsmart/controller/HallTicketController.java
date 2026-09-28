package com.examsmart.examsmart.controller;

import com.examsmart.examsmart.model.Allocation;
import com.examsmart.examsmart.model.Student;
import com.examsmart.examsmart.repository.AllocationRepository;
import com.examsmart.examsmart.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/hallticket")
public class HallTicketController {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private AllocationRepository allocationRepository;

    @PostMapping("/lookup")
    public ResponseEntity<?> lookupHallTicket(@RequestBody LookupRequest request) {
        // 1. Find the student by institution name + name + date of birth
        Student matchedStudent = null;
        for (Student s : studentRepository.findAll()) {
            if (s.getInstitution() != null
                    && s.getInstitution().getInstitutionName().equalsIgnoreCase(request.getInstitutionName())
                    && s.getName().equalsIgnoreCase(request.getName())
                    && s.getDateOfBirth().equals(request.getDateOfBirth())) {
                matchedStudent = s;
                break;
            }
        }

        if (matchedStudent == null) {
            return ResponseEntity.badRequest().body("No matching student found. Check institution, name and date of birth.");
        }

        // 2. Pick the most recent allocation for this student
        Allocation latest = null;
        for (Allocation a : allocationRepository.findAll()) {
            if (a.getStudent().getId().equals(matchedStudent.getId())
                    && (latest == null || a.getId() > latest.getId())) {
                latest = a;
            }
        }

        if (latest == null) {
            return ResponseEntity.badRequest().body("No allocation found yet for this student. Allocation may not have run.");
        }

        return ResponseEntity.ok(latest);
    }

    public static class LookupRequest {
        private String institutionName;
        private String name;
        private String dateOfBirth;

        public String getInstitutionName() { return institutionName; }
        public void setInstitutionName(String institutionName) { this.institutionName = institutionName; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getDateOfBirth() { return dateOfBirth; }
        public void setDateOfBirth(String dateOfBirth) { this.dateOfBirth = dateOfBirth; }
    }
}