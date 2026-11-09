import java.util.Objects;

public class Employee {

    
    public static final int MIN_AGE = 18;

   
    public static final int MAX_AGE = 70;

    
    private static int count = 0;


    private String firstName;
    private String lastName;
    private String position;
    private double salary;
    private int age;
    private Department department;

    
    public Employee(String firstName, String lastName, String position,
                    double salary, int age, Department department) {
        setFirstName(firstName);
        setLastName(lastName);
        setPosition(position);
        setSalary(salary);
        setAge(age);
        setDepartment(department);
        count++; 
    }

    
    public Employee(Employee other) {
        this(requireNotNull(other).firstName, other.lastName, other.position,
                other.salary, other.age, other.department);
    }

    
    private static Employee requireNotNull(Employee other) {
        if (other == null) {
            throw new IllegalArgumentException("Працівник для копіювання не може бути null.");
        }
        return other;
    }

    
    public static int getCount() {
        return count;
    }
    
    private static String requireNotBlank(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " не може бути порожнім.");
        }
        return value.trim();
    }

    
    public String getFirstName() {
        return firstName;
    }

   
    public void setFirstName(String firstName) {
        this.firstName = requireNotBlank(firstName, "Ім'я");
    }

    
    public String getLastName() {
        return lastName;
    }

    
    public void setLastName(String lastName) {
        this.lastName = requireNotBlank(lastName, "Прізвище");
    }

   
    public String getPosition() {
        return position;
    }

    
    public void setPosition(String position) {
        this.position = requireNotBlank(position, "Посада");
    }

    
    public double getSalary() {
        return salary;
    }

    
    public void setSalary(double salary) {
        if (Double.isNaN(salary) || Double.isInfinite(salary) || salary <= 0) {
            throw new IllegalArgumentException("Зарплата має бути більшою за 0.");
        }
        this.salary = salary;
    }

    
    public int getAge() {
        return age;
    }

   
    public void setAge(int age) {
        if (age < MIN_AGE || age > MAX_AGE) {
            throw new IllegalArgumentException(
                    "Вік має бути від " + MIN_AGE + " до " + MAX_AGE + ".");
        }
        this.age = age;
    }

    
    public Department getDepartment() {
        return department;
    }

    
    public void setDepartment(Department department) {
        if (department == null) {
            throw new IllegalArgumentException("Відділ не може бути порожнім.");
        }
        this.department = department;
    }

    @Override
    public String toString() {
        return "Employee{firstName='" + firstName +
                "', lastName='" + lastName +
                "', position='" + position +
                "', salary=" + salary +
                ", age=" + age +
                ", department=" + department + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return Double.compare(employee.salary, salary) == 0
                && age == employee.age
                && department == employee.department
                && Objects.equals(firstName, employee.firstName)
                && Objects.equals(lastName, employee.lastName)
                && Objects.equals(position, employee.position);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, position, salary, age, department);
    }
}