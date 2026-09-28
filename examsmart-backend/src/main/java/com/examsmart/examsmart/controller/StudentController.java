package com.examsmart.examsmart.controller;

import com.examsmart.examsmart.model.Institution;
import com.examsmart.examsmart.model.Student;
import com.examsmart.examsmart.repository.InstitutionRepository;
import com.examsmart.examsmart.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private InstitutionRepository institutionRepository;

    // CREATE - add a student under a specific institution
    @PostMapping("/institution/{institutionId}")
    public ResponseEntity<?> addStudent(@PathVariable Long institutionId, @RequestBody Student student) {
        Optional<Institution> institutionOpt = institutionRepository.findById(institutionId);

        if (institutionOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Institution not found with id: " + institutionId);
        }

        Institution institution = institutionOpt.get();
        student.setInstitution(institution);
        Student saved = studentRepository.save(student);
        return ResponseEntity.ok(saved);
    }

    // READ - get all students
    @GetMapping
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    // READ - get one student by id
    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable Long id) {
        Optional<Student> student = studentRepository.findById(id);
        return student.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}