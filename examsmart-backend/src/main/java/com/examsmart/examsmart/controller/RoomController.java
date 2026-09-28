package com.examsmart.examsmart.controller;

import com.examsmart.examsmart.model.Institution;
import com.examsmart.examsmart.model.Room;
import com.examsmart.examsmart.repository.InstitutionRepository;
import com.examsmart.examsmart.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private InstitutionRepository institutionRepository;

    // CREATE - add a room under a specific institution
    @PostMapping("/institution/{institutionId}")
    public ResponseEntity<?> addRoom(@PathVariable Long institutionId, @RequestBody Room room) {
        Optional<Institution> institutionOpt = institutionRepository.findById(institutionId);

        if (institutionOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Institution not found with id: " + institutionId);
        }

        Institution institution = institutionOpt.get();
        room.setInstitution(institution);
        Room saved = roomRepository.save(room);
        return ResponseEntity.ok(saved);
    }

    // READ - get all rooms
    @GetMapping
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    // READ - get one room by id
    @GetMapping("/{id}")
    public ResponseEntity<Room> getRoomById(@PathVariable Long id) {
        Optional<Room> room = roomRepository.findById(id);
        return room.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}