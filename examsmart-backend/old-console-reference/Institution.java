import java.util.ArrayList;

public class Institution {

    String institutionName;
    String institutionType;
    String institutionCode;

    ArrayList<Student> students;
    ArrayList<Room> rooms;

    public Institution(String institutionName, String institutionType,
                       String institutionCode) {

        this.institutionName = institutionName;
        this.institutionType = institutionType;
        this.institutionCode = institutionCode;

        students = new ArrayList<>();
        rooms = new ArrayList<>();
    }

    public void addStudent(Student student) {
        students.add(student);
    }

    public void addRoom(Room room) {
        rooms.add(room);
    }
}