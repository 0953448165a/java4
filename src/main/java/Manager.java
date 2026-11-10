import java.util.Objects;

public class Manager extends FullTimeEmployee {

    /** Надбавка до місячної виплати за кожного підлеглого. */
    private static final double BONUS_PER_SUBORDINATE = 1000.0;

    private int teamSize;
    private String projectName;

    public Manager(String firstName, String lastName, String position,
                   double salary, int age, Department department,
                   double bonus, int vacationDays,
                   int teamSize, String projectName) {
        super(firstName, lastName, position, salary, age, department, bonus, vacationDays);
        setTeamSize(teamSize);
        setProjectName(projectName);
    }

    public int getTeamSize() {
        return teamSize;
    }

    public void setTeamSize(int teamSize) {
        if (teamSize < 1 || teamSize > 100) {
            throw new IllegalArgumentException("Розмір команди має бути від 1 до 100.");
        }
        this.teamSize = teamSize;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        if (projectName == null || projectName.isBlank()) {
            throw new IllegalArgumentException("Назва проєкту не може бути порожньою.");
        }
        this.projectName = projectName.trim();
    }

    /** @return надбавка за керування командою */
    public double getManagementAllowance() {
        return teamSize * BONUS_PER_SUBORDINATE;
    }

    @Override
    public String getEmployeeType() {
        return "Керівник";
    }

    @Override
    public double calculateMonthlyPay() {
        return super.calculateMonthlyPay() + getManagementAllowance();
    }

    @Override
    public String toString() {
        return "Manager{" + baseFieldsToString() +
                ", bonus=" + getBonus() +
                ", vacationDays=" + getVacationDays() +
                ", teamSize=" + teamSize +
                ", projectName='" + projectName + "'}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!super.equals(o)) return false; // перевіряє також, що клас однаковий
        Manager that = (Manager) o;
        return teamSize == that.teamSize
                && Objects.equals(projectName, that.projectName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), teamSize, projectName);
    }
}