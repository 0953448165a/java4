import java.util.Objects;

public class Intern extends Employee {

    /** Стажист отримує частку від ставки. */
    private static final double STIPEND_RATIO = 0.5;

    private String university;
    private int internshipMonths;
    private String mentorName;

    public Intern(String firstName, String lastName, String position,
                  double salary, int age, Department department,
                  String university, int internshipMonths, String mentorName) {
        super(firstName, lastName, position, salary, age, department);
        setUniversity(university);
        setInternshipMonths(internshipMonths);
        setMentorName(mentorName);
    }

    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        if (university == null || university.isBlank()) {
            throw new IllegalArgumentException("Назва університету не може бути порожньою.");
        }
        this.university = university.trim();
    }

    public int getInternshipMonths() {
        return internshipMonths;
    }

    public void setInternshipMonths(int internshipMonths) {
        if (internshipMonths < 1 || internshipMonths > 12) {
            throw new IllegalArgumentException("Тривалість стажування має бути від 1 до 12 місяців.");
        }
        this.internshipMonths = internshipMonths;
    }

    public String getMentorName() {
        return mentorName;
    }

    public void setMentorName(String mentorName) {
        if (mentorName == null || mentorName.isBlank()) {
            throw new IllegalArgumentException("Ім'я ментора не може бути порожнім.");
        }
        this.mentorName = mentorName.trim();
    }

    /** @return сумарна виплата стажисту за весь строк стажування */
    public double getTotalStipend() {
        return calculateMonthlyPay() * internshipMonths;
    }

    @Override
    public String getEmployeeType() {
        return "Стажист";
    }

    @Override
    public double calculateMonthlyPay() {
        return super.calculateMonthlyPay() * STIPEND_RATIO;
    }

    @Override
    public String toString() {
        return "Intern{" + baseFieldsToString() +
                ", university='" + university + "'" +
                ", internshipMonths=" + internshipMonths +
                ", mentorName='" + mentorName + "'}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!super.equals(o)) return false;
        Intern that = (Intern) o;
        return internshipMonths == that.internshipMonths
                && Objects.equals(university, that.university)
                && Objects.equals(mentorName, that.mentorName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), university, internshipMonths, mentorName);
    }
}