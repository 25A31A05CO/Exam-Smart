package com.examsmart.examsmart.controller;

import com.examsmart.examsmart.model.Institution;
import com.examsmart.examsmart.repository.InstitutionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/institutions")
public class InstitutionController {

    @Autowired
    private InstitutionRepository institutionRepository;

    // CREATE - add a new institution
    @PostMapping
    public Institution addInstitution(@RequestBody Institution institution) {
        return institutionRepository.save(institution);
    }

    // READ - get all institutions
    @GetMapping
    public List<Institution> getAllInstitutions() {
        return institutionRepository.findAll();
    }

    // READ - get one institution by id
    @GetMapping("/{id}")
    public ResponseEntity<Institution> getInstitutionById(@PathVariable Long id) {
        Optional<Institution> institution = institutionRepository.findById(id);
        return institution.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}