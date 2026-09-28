import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.println("==========================================");
        System.out.println("           WELCOME TO EXAMSMART");
        System.out.println("==========================================");

        // 1. Institution details
        System.out.print("\nEnter institution name: ");
        String institutionName = sc.nextLine();

        System.out.print("Enter institution type (School/College): ");
        String institutionType = sc.nextLine();

        System.out.print("Enter institution code: ");
        String institutionCode = sc.nextLine();

        Institution institution = new Institution(
                institutionName,
                institutionType,
                institutionCode
        );

        // 2. Exam details
        System.out.println("\n===== EXAM DETAILS =====");

        System.out.print("Enter exam name: ");
        String examName = sc.nextLine();

        System.out.print("Enter exam date (DD/MM/YYYY): ");
        String examDate = sc.nextLine();

        System.out.print("Enter exam time: ");
        String examTime = sc.nextLine();

        Exam exam = new Exam(examName, examDate, examTime);

        // 3. Room setup
        System.out.println("\n===== ROOM SETUP =====");

        System.out.print("Enter number of rooms: ");
        int roomCount = sc.nextInt();
        sc.nextLine();

        for (int i = 0; i < roomCount; i++) {

            System.out.println("\nRoom " + (i + 1));

            System.out.print("Enter room number: ");
            String roomNumber = sc.nextLine();

            System.out.print("Enter room capacity: ");
            int capacity = sc.nextInt();
            sc.nextLine();

            Room room = new Room(roomNumber, capacity);
            institution.addRoom(room);
        }

        // 4. Student data
        System.out.println("\n===== STUDENT DATA =====");

        System.out.print("Enter number of students: ");
        int studentCount = sc.nextInt();
        sc.nextLine();

        for (int i = 0; i < studentCount; i++) {

            System.out.println("\nStudent " + (i + 1));

            System.out.print("Name: ");
            String name = sc.nextLine();

            System.out.print("Date of Birth (DD/MM/YYYY): ");
            String dateOfBirth = sc.nextLine();

            Student student = new Student(
                    name,
                    dateOfBirth,
                    institutionName,
                    institutionType
            );

            institution.addStudent(student);
        }

        // 5. Check total capacity
        int totalCapacity = 0;

        for (Room room : institution.rooms) {
            totalCapacity += room.capacity;
        }

        System.out.println("\n===== ALLOCATION CHECK =====");

        System.out.println("Students registered : " + institution.students.size());
        System.out.println("Total room capacity : " + totalCapacity);

        if (institution.students.size() > totalCapacity) {

            System.out.println("\nERROR: Not enough seats available.");
            System.out.println("Please add more rooms or increase room capacity.");

            sc.close();
            return;
        }

        // 6. Generate allocation
        System.out.println("\n===== EXAM ALLOCATION =====");

        int studentIndex = 0;
        int hallTicketNumber = 1001;

        for (Room room : institution.rooms) {

            int seatNumber = 1;

            while (seatNumber <= room.capacity
                    && studentIndex < institution.students.size()) {

                Student student = institution.students.get(studentIndex);

                // ExamSmart generates the hall ticket number
                student.hallTicketNumber = "ES" + hallTicketNumber;

                System.out.println("\n------------------------------");
                System.out.println("Hall Ticket : " + student.hallTicketNumber);
                System.out.println("Student     : " + student.name);
                System.out.println("Date of Birth : " + student.dateOfBirth);
                System.out.println("Institution : " + student.institutionName);
                System.out.println("Exam        : " + exam.examName);
                System.out.println("Date        : " + exam.examDate);
                System.out.println("Time        : " + exam.examTime);
                System.out.println("Room        : " + room.roomNumber);
                System.out.println("Seat        : " + seatNumber);

                studentIndex++;
                seatNumber++;
                hallTicketNumber++;
            }
        }

        System.out.println("\n==========================================");
        System.out.println("         ALLOCATION COMPLETED");
        System.out.println("==========================================");

        sc.close();
    }
}