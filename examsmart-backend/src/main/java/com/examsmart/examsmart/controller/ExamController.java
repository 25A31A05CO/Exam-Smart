package com.examsmart.examsmart.controller;

import com.examsmart.examsmart.model.Exam;
import com.examsmart.examsmart.repository.ExamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/exams")
public class ExamController {

    @Autowired
    private ExamRepository examRepository;

    // CREATE - add a new exam
    @PostMapping
    public Exam addExam(@RequestBody Exam exam) {
        return examRepository.save(exam);
    }

    // READ - get all exams
    @GetMapping
    public List<Exam> getAllExams() {
        return examRepository.findAll();
    }

    // READ - get one exam by id
    @GetMapping("/{id}")
    public ResponseEntity<Exam> getExamById(@PathVariable Long id) {
        Optional<Exam> exam = examRepository.findById(id);
        return exam.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}