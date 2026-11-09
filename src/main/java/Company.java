import java.util.ArrayList;

public class Company {

    private String name;
    private final ArrayList<Employee> employees;


    public Company(String name) {
        setName(name);
        this.employees = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    
    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Назва компанії не може бути порожньою.");
        }
        this.name = name.trim();
    }

    
    public void addEmployee(Employee employee) {
        if (employee == null) {
            throw new IllegalArgumentException("Працівник не може бути null.");
        }
        employees.add(employee);
    }

    
    public Employee getEmployee(int index) {
        if (index < 0 || index >= employees.size()) {
            throw new IllegalArgumentException("Немає працівника з індексом " + index + ".");
        }
        return employees.get(index);
    }

    
    public ArrayList<Employee> getEmployees() {
        return new ArrayList<>(employees);
    }

    
    public int getEmployeeCount() {
        return employees.size();
    }

   
    public boolean isEmpty() {
        return employees.isEmpty();
    }

  
    public double getTotalSalary() {
        double total = 0;
        for (Employee e : employees) {
            total += e.getSalary();
        }
        return total;
    }

   
    public double getAverageSalary() {
        if (employees.isEmpty()) {
            return 0;
        }
        return getTotalSalary() / employees.size();
    }

   
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

    
    @Override
    public String toString() {
        return "Company{name='" + name + "', employees=" + employees.size() + "}";
    }
}