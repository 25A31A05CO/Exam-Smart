public class Student {

    String name;
    String dateOfBirth;
    String institutionName;
    String institutionType;
    String hallTicketNumber;

    public Student(String name, String dateOfBirth,
                   String institutionName, String institutionType) {

        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.institutionName = institutionName;
        this.institutionType = institutionType;
        this.hallTicketNumber = "";
    }
}