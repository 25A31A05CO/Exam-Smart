package com.examsmart.examsmart.controller;

import com.examsmart.examsmart.model.Allocation;
import com.examsmart.examsmart.repository.AllocationRepository;
import com.examsmart.examsmart.service.AllocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/allocations")
public class AllocationController {

    @Autowired
    private AllocationService allocationService;

    @Autowired
    private AllocationRepository allocationRepository;

    // Trigger seat allocation for a given exam
    @PostMapping("/run/{examId}")
    public ResponseEntity<?> runAllocation(@PathVariable Long examId) {
        try {
            List<Allocation> result = allocationService.allocateSeats(examId);
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // View all allocations
    @GetMapping
    public List<Allocation> getAllAllocations() {
        return allocationRepository.findAll();
    }
}