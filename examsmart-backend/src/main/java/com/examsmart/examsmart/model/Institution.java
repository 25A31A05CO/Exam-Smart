package com.examsmart.examsmart.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "institutions")
public class Institution {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String institutionName;
    private String institutionType;
    private String institutionCode;

    @OneToMany(mappedBy = "institution", cascade = CascadeType.ALL)
    private List<Student> students = new ArrayList<>();

    @OneToMany(mappedBy = "institution", cascade = CascadeType.ALL)
    private List<Room> rooms = new ArrayList<>();

    public Institution() {
    }

    public Institution(String institutionName, String institutionType, String institutionCode) {
        this.institutionName = institutionName;
        this.institutionType = institutionType;
        this.institutionCode = institutionCode;
    }

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getInstitutionName() { return institutionName; }
    public void setInstitutionName(String institutionName) { this.institutionName = institutionName; }

    public String getInstitutionType() { return institutionType; }
    public void setInstitutionType(String institutionType) { this.institutionType = institutionType; }

    public String getInstitutionCode() { return institutionCode; }
    public void setInstitutionCode(String institutionCode) { this.institutionCode = institutionCode; }

    public List<Student> getStudents() { return students; }
    public void setStudents(List<Student> students) { this.students = students; }

    public List<Room> getRooms() { return rooms; }
    public void setRooms(List<Room> rooms) { this.rooms = rooms; }

    public void addStudent(Student student) {
        students.add(student);
        student.setInstitution(this);
    }

    public void addRoom(Room room) {
        rooms.add(room);
        room.setInstitution(this);
    }
}