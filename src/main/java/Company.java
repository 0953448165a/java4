import java.util.ArrayList;

/**
 * Компанія, яка агрегує працівників.
 * <p>
 * Це приклад агрегації: об'єкти {@link Employee} створюються незалежно
 * від компанії й лише додаються до її списку.
 */
public class Company {

    private String name;
    private final ArrayList<Employee> employees;

    /**
     * Створює компанію без працівників.
     *
     * @param name назва компанії (не порожня)
     * @throws IllegalArgumentException якщо назва null або порожня
     */
    public Company(String name) {
        setName(name);
        this.employees = new ArrayList<>();
    }

    /** @return назва компанії */
    public String getName() {
        return name;
    }

    /**
     * Встановлює назву компанії.
     *
     * @param name нова назва
     * @throws IllegalArgumentException якщо назва null або порожня
     */
    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Назва компанії не може бути порожньою.");
        }
        this.name = name.trim();
    }

    /**
     * Додає працівника до компанії.
     *
     * @param employee працівник (не null)
     * @throws IllegalArgumentException якщо працівник null
     */
    public void addEmployee(Employee employee) {
        if (employee == null) {
            throw new IllegalArgumentException("Працівник не може бути null.");
        }
        employees.add(employee);
    }

    /**
     * Повертає працівника за індексом.
     *
     * @param index індекс від 0 до {@code getEmployeeCount() - 1}
     * @return працівник
     * @throws IllegalArgumentException якщо індекс поза межами списку
     */
    public Employee getEmployee(int index) {
        if (index < 0 || index >= employees.size()) {
            throw new IllegalArgumentException("Немає працівника з індексом " + index + ".");
        }
        return employees.get(index);
    }

    /**
     * Повертає копію списку працівників. Зміна повернутого списку
     * не впливає на склад компанії.
     *
     * @return новий список з тими самими працівниками
     */
    public ArrayList<Employee> getEmployees() {
        return new ArrayList<>(employees);
    }

    /** @return кількість працівників у компанії */
    public int getEmployeeCount() {
        return employees.size();
    }

    /** @return true, якщо в компанії немає працівників */
    public boolean isEmpty() {
        return employees.isEmpty();
    }

    /** @return сумарна зарплата всіх працівників */
    public double getTotalSalary() {
        double total = 0;
        for (Employee e : employees) {
            total += e.getSalary();
        }
        return total;
    }

    /** @return середня зарплата, або 0, якщо працівників немає */
    public double getAverageSalary() {
        if (employees.isEmpty()) {
            return 0;
        }
        return getTotalSalary() / employees.size();
    }

    /**
     * Рахує працівників заданого відділу.
     *
     * @param department відділ (не null)
     * @return кількість працівників відділу
     * @throws IllegalArgumentException якщо відділ null
     */
    public int countByDepartment(Department department) {
        if (department == null) {
            throw new IllegalArgumentException("Відділ не може бути null.");
        }
        int result = 0;
        for (Employee e : employees) {
            if (e.getDepartment() == department) {
                result++;
            }
        }
        return result;
    }

    /** @return текстове подання компанії */
    @Override
    public String toString() {
        return "Company{name='" + name + "', employees=" + employees.size() + "}";
    }
}