package com.examsmart.examsmart.service;

import com.examsmart.examsmart.model.*;
import com.examsmart.examsmart.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AllocationService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private AllocationRepository allocationRepository;

    public List<Allocation> allocateSeats(Long examId) {
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new RuntimeException("Exam not found with id: " + examId));

        // NEW: remove old allocations for this exam so re-running doesn't create duplicates
        List<Allocation> oldAllocations = allocationRepository.findByExamId(examId);
        allocationRepository.deleteAll(oldAllocations);

        List<Student> allStudents = studentRepository.findAll();
        List<Room> allRooms = roomRepository.findAll();

        if (allStudents.isEmpty() || allRooms.isEmpty()) {
            throw new RuntimeException("No students or rooms available for allocation");
        }

        // Group students by institution
        Map<Long, List<Student>> studentsByInstitution = new LinkedHashMap<>();
        for (Student s : allStudents) {
            Long instId = s.getInstitution().getId();
            studentsByInstitution.computeIfAbsent(instId, k -> new ArrayList<>()).add(s);
        }

        // Round-robin merge: one student from each institution in turn
        List<Student> roundRobinOrder = new ArrayList<>();
        List<List<Student>> groups = new ArrayList<>(studentsByInstitution.values());
        int maxGroupSize = groups.stream().mapToInt(List::size).max().orElse(0);

        for (int i = 0; i < maxGroupSize; i++) {
            for (List<Student> group : groups) {
                if (i < group.size()) {
                    roundRobinOrder.add(group.get(i));
                }
            }
        }

        // Assign seats room by room
        List<Allocation> allocations = new ArrayList<>();
        int roomIndex = 0;
        int seatInRoom = 1;
        int hallTicketCounter = 1000;

        for (Student student : roundRobinOrder) {
            while (roomIndex < allRooms.size() && seatInRoom > allRooms.get(roomIndex).getCapacity()) {
                roomIndex++;
                seatInRoom = 1;
            }

            if (roomIndex >= allRooms.size()) {
                throw new RuntimeException("Not enough room capacity to seat all students");
            }

            Room currentRoom = allRooms.get(roomIndex);

            String hallTicketNumber = "HT-" + examId + "-" + hallTicketCounter;
            student.setHallTicketNumber(hallTicketNumber);
            studentRepository.save(student);

            Allocation allocation = new Allocation(student, exam, currentRoom, seatInRoom);
            allocations.add(allocation);

            seatInRoom++;
            hallTicketCounter++;
        }

        return allocationRepository.saveAll(allocations);
    }
}