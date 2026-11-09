import java.util.Objects;
public class FullTimeEmployee extends Employee {

    private double bonus;
    private int vacationDays;

    public FullTimeEmployee(String firstName, String lastName, String position,
                            double salary, int age, Department department,
                            double bonus, int vacationDays) {
        super(firstName, lastName, position, salary, age, department);
        setBonus(bonus);
        setVacationDays(vacationDays);
    }

    public double getBonus() {
        return bonus;
    }

    public void setBonus(double bonus) {
        if (Double.isNaN(bonus) || Double.isInfinite(bonus) || bonus < 0) {
            throw new IllegalArgumentException("Премія не може бути від'ємною.");
        }
        this.bonus = bonus;
    }

    /** @return кількість днів відпустки на рік */
    public int getVacationDays() {
        return vacationDays;
    }

    public void setVacationDays(int vacationDays) {
        if (vacationDays < 0 || vacationDays > 60) {
            throw new IllegalArgumentException("Днів відпустки має бути від 0 до 60.");
        }
        this.vacationDays = vacationDays;
    }

    /** @return назва типу працівника */
    @Override
    public String getEmployeeType() {
        return "Штатний працівник";
    }

    @Override
    public double calculateMonthlyPay() {
        return super.calculateMonthlyPay() + bonus;
    }

    @Override
    public String toString() {
        return "FullTimeEmployee{" + baseFieldsToString() +
                ", bonus=" + bonus +
                ", vacationDays=" + vacationDays + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!super.equals(o)) return false; // перевіряє також, що клас однаковий
        FullTimeEmployee that = (FullTimeEmployee) o;
        return Double.compare(that.bonus, bonus) == 0
                && vacationDays == that.vacationDays;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), bonus, vacationDays);
    }
}