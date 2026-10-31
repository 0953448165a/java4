import java.util.Objects;

/**
 * Працівник організації.
 * <p>
 * Усі поля перевіряються на коректність у конструкторі та сетерах.
 * Якщо значення некоректне, кидається {@link IllegalArgumentException}.
 * Клас рахує кількість створених об'єктів за допомогою статичного поля. ★
 */
public class Employee {

    /** Мінімально допустимий вік працівника. */
    public static final int MIN_AGE = 18;

    /** Максимально допустимий вік працівника. */
    public static final int MAX_AGE = 70;

    // ★ НОВЕ ▼
    /** Кількість успішно створених об'єктів класу (включно з копіями). */
    private static int count = 0;
    // ★ НОВЕ ▲

    private String firstName;
    private String lastName;
    private String position;
    private double salary;
    private int age;
    private Department department;

    /**
     * Створює працівника з перевіркою всіх параметрів.
     * Після успішного створення збільшує лічильник об'єктів. ★
     *
     * @param firstName  ім'я (не порожнє)
     * @param lastName   прізвище (не порожнє)
     * @param position   посада (не порожня)
     * @param salary     зарплата (більше 0)
     * @param age        вік (від {@value #MIN_AGE} до {@value #MAX_AGE})
     * @param department відділ (не null)
     * @throws IllegalArgumentException якщо будь-який параметр некоректний
     */
    public Employee(String firstName, String lastName, String position,
                    double salary, int age, Department department) {
        setFirstName(firstName);
        setLastName(lastName);
        setPosition(position);
        setSalary(salary);
        setAge(age);
        setDepartment(department);
        count++; // ★ НОВЕ (збільшується лише якщо всі перевірки пройдено)
    }

    // ★ НОВЕ ▼
    /**
     * Конструктор копіювання. Створює нового працівника з тими самими
     * значеннями полів, що й у переданого. Копія також враховується в лічильнику.
     *
     * @param other працівник, якого потрібно скопіювати (не null)
     * @throws IllegalArgumentException якщо other дорівнює null
     */
    public Employee(Employee other) {
        this(requireNotNull(other).firstName, other.lastName, other.position,
                other.salary, other.age, other.department);
    }

    /**
     * Перевіряє, що працівник для копіювання не є null.
     *
     * @param other працівник
     * @return той самий працівник
     * @throws IllegalArgumentException якщо other дорівнює null
     */
    private static Employee requireNotNull(Employee other) {
        if (other == null) {
            throw new IllegalArgumentException("Працівник для копіювання не може бути null.");
        }
        return other;
    }

    /**
     * Повертає кількість створених об'єктів класу {@code Employee}.
     * Об'єкти, під час створення яких виникла помилка валідації, не враховуються.
     *
     * @return кількість створених працівників
     */
    public static int getCount() {
        return count;
    }
    // ★ НОВЕ ▲

    /**
     * Перевіряє, що рядок не є null і не порожній.
     *
     * @param value     значення
     * @param fieldName назва поля для повідомлення про помилку
     * @return значення без пробілів по краях
     * @throws IllegalArgumentException якщо рядок null або порожній
     */
    private static String requireNotBlank(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " не може бути порожнім.");
        }
        return value.trim();
    }

    /** @return ім'я */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Встановлює ім'я.
     *
     * @param firstName нове ім'я
     * @throws IllegalArgumentException якщо ім'я null або порожнє
     */
    public void setFirstName(String firstName) {
        this.firstName = requireNotBlank(firstName, "Ім'я");
    }

    /** @return прізвище */
    public String getLastName() {
        return lastName;
    }

    /**
     * Встановлює прізвище.
     *
     * @param lastName нове прізвище
     * @throws IllegalArgumentException якщо прізвище null або порожнє
     */
    public void setLastName(String lastName) {
        this.lastName = requireNotBlank(lastName, "Прізвище");
    }

    /** @return посада */
    public String getPosition() {
        return position;
    }

    /**
     * Встановлює посаду.
     *
     * @param position нова посада
     * @throws IllegalArgumentException якщо посада null або порожня
     */
    public void setPosition(String position) {
        this.position = requireNotBlank(position, "Посада");
    }

    /** @return зарплата */
    public double getSalary() {
        return salary;
    }

    /**
     * Встановлює зарплату.
     *
     * @param salary нова зарплата
     * @throws IllegalArgumentException якщо зарплата не більша за 0 або не є скінченним числом
     */
    public void setSalary(double salary) {
        if (Double.isNaN(salary) || Double.isInfinite(salary) || salary <= 0) {
            throw new IllegalArgumentException("Зарплата має бути більшою за 0.");
        }
        this.salary = salary;
    }

    /** @return вік */
    public int getAge() {
        return age;
    }

    /**
     * Встановлює вік.
     *
     * @param age новий вік
     * @throws IllegalArgumentException якщо вік поза межами від {@value #MIN_AGE} до {@value #MAX_AGE}
     */
    public void setAge(int age) {
        if (age < MIN_AGE || age > MAX_AGE) {
            throw new IllegalArgumentException(
                    "Вік має бути від " + MIN_AGE + " до " + MAX_AGE + ".");
        }
        this.age = age;
    }

    /** @return відділ */
    public Department getDepartment() {
        return department;
    }

    /**
     * Встановлює відділ.
     *
     * @param department новий відділ
     * @throws IllegalArgumentException якщо відділ null
     */
    public void setDepartment(Department department) {
        if (department == null) {
            throw new IllegalArgumentException("Відділ не може бути порожнім.");
        }
        this.department = department;
    }

    /** @return текстове подання працівника з усіма полями */
    @Override
    public String toString() {
        return "Employee{firstName='" + firstName +
                "', lastName='" + lastName +
                "', position='" + position +
                "', salary=" + salary +
                ", age=" + age +
                ", department=" + department + "}";
    }

    /**
     * Порівнює працівників за всіма полями.
     *
     * @param o інший об'єкт
     * @return true, якщо всі поля збігаються
     */
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

    /** @return хеш-код, узгоджений з {@link #equals(Object)} */
    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, position, salary, age, department);
    }
}