import java.util.Scanner; // ★ ЗМІНЕНО (імпорт ArrayList більше не потрібен)

/**
 * Драйвер програми: консольне меню для роботи з компанією та її працівниками. ★
 * Містить інформаційну шапку програми. Некоректне введення не завершує програму. ★
 */
public class Main {

    // ★ НОВЕ ▼
    private static final String PROGRAM_TITLE =
            "Практична робота №6: класи, static, агрегація, enum";
    private static final String AUTHOR = "Мудрий Ярослав, група ІН-43-5";
    // ★ НОВЕ ▲

    private static final Scanner SCANNER = new Scanner(System.in);
    private static Company company; // ★ ЗМІНЕНО (замість ArrayList<Employee> EMPLOYEES)

    /**
     * Точка входу. Виводить шапку, створює компанію й показує меню, ★
     * поки користувач не обере вихід.
     *
     * @param args аргументи командного рядка (не використовуються)
     */
    public static void main(String[] args) {
        printHeader(); // ★ НОВЕ
        company = createCompany(); // ★ НОВЕ

        boolean running = true;
        while (running) {
            printMenu();
            int choice = readInt("Ваш вибір: ");
            switch (choice) {
                case 1 -> createEmployee();
                case 2 -> printAll();
                case 3 -> copyEmployee(); // ★ ЗМІНЕНО (було завершення роботи)
                case 4 -> showCounter(); // ★ НОВЕ
                case 5 -> showStatistics(); // ★ НОВЕ
                case 6 -> { // ★ ЗМІНЕНО (було case 3)
                    running = false;
                    System.out.println("Роботу завершено.");
                }
                default -> System.out.println("Невірний пункт меню. Оберіть число від 1 до 6."); // ★ ЗМІНЕНО
            }
        }
        SCANNER.close();
    }

    // ★ НОВЕ ▼
    /** Виводить інформаційну шапку програми. */
    private static void printHeader() {
        System.out.println("==============================================");
        System.out.println(PROGRAM_TITLE);
        System.out.println("Виконав: " + AUTHOR);
        System.out.println("==============================================");
    }

    /**
     * Зчитує назву й створює компанію.
     *
     * @return нова компанія
     */
    private static Company createCompany() {
        while (true) {
            String name = readNonEmpty("Введіть назву компанії: ");
            try {
                return new Company(name);
            } catch (IllegalArgumentException e) {
                System.out.println("Помилка: " + e.getMessage());
            }
        }
    }
    // ★ НОВЕ ▲

    /** Виводить меню. */
    private static void printMenu() {
        System.out.println("\n=== МЕНЮ (" + company.getName() + ") ==="); // ★ ЗМІНЕНО
        System.out.println("1. Створити нового працівника");
        System.out.println("2. Вивести всіх працівників");
        System.out.println("3. Створити копію працівника (конструктор копіювання)"); // ★ ЗМІНЕНО
        System.out.println("4. Показати кількість створених об'єктів Employee (static)"); // ★ НОВЕ
        System.out.println("5. Статистика компанії"); // ★ НОВЕ
        System.out.println("6. Завершити роботу"); // ★ ЗМІНЕНО (було пунктом 3)
    }

    /**
     * Зчитує дані працівника і додає його до компанії. ★
     * Якщо дані некоректні, виняток перехоплюється й програма повертається в меню. ★
     */
    private static void createEmployee() {
        System.out.println("\n--- Новий працівник ---");
        String firstName = readNonEmpty("Ім'я: ");
        String lastName = readNonEmpty("Прізвище: ");
        String position = readNonEmpty("Посада: ");
        double salary = readDouble("Зарплата: ");
        int age = readInt("Вік: ");
        Department department = readDepartment();

        try {
            Employee employee = new Employee(firstName, lastName, position, salary, age, department);
            company.addEmployee(employee); // ★ ЗМІНЕНО (було EMPLOYEES.add)
            System.out.println("Працівника додано.");
        } catch (IllegalArgumentException e) {
            System.out.println("Помилка: " + e.getMessage());
            System.out.println("Працівника не створено.");
        }
    }

    /** Виводить усіх працівників компанії через toString(). */ // ★ ЗМІНЕНО
    private static void printAll() {
        if (company.isEmpty()) { // ★ ЗМІНЕНО
            System.out.println("Список порожній.");
            return;
        }
        System.out.println("\n=== Працівники компанії " + company.getName() + " ==="); // ★ ЗМІНЕНО
        for (int i = 0; i < company.getEmployeeCount(); i++) { // ★ ЗМІНЕНО
            System.out.println((i + 1) + ". " + company.getEmployee(i)); // ★ ЗМІНЕНО
        }
    }

    // ★ НОВЕ ▼
    /**
     * Створює копію обраного працівника через конструктор копіювання
     * і демонструє різницю між equals() та порівнянням посилань.
     */
    private static void copyEmployee() {
        if (company.isEmpty()) {
            System.out.println("Немає кого копіювати: список порожній.");
            return;
        }
        printAll();
        int number = readIntInRange("Номер працівника для копіювання: ", 1, company.getEmployeeCount());

        try {
            Employee original = company.getEmployee(number - 1);
            Employee copy = new Employee(original);
            company.addEmployee(copy);
            System.out.println("Копію створено: " + copy);
            System.out.println("original.equals(copy): " + original.equals(copy));
            System.out.println("original == copy: " + (original == copy));
        } catch (IllegalArgumentException e) {
            System.out.println("Помилка: " + e.getMessage());
        }
    }

    /** Демонструє статичний гетер {@link Employee#getCount()}. */
    private static void showCounter() {
        System.out.println("Створено об'єктів Employee (Employee.getCount()): " + Employee.getCount());
        System.out.println("Зараз у компанії працівників: " + company.getEmployeeCount());
    }

    /** Виводить статистику компанії: кількість, зарплати, розподіл за відділами. */
    private static void showStatistics() {
        if (company.isEmpty()) {
            System.out.println("Список порожній, статистики немає.");
            return;
        }
        System.out.println("\n=== Статистика: " + company + " ===");
        System.out.printf("Сумарна зарплата: %.2f%n", company.getTotalSalary());
        System.out.printf("Середня зарплата: %.2f%n", company.getAverageSalary());
        System.out.println("За відділами:");
        for (Department d : Department.values()) {
            System.out.println("  " + d + ": " + company.countByDepartment(d));
        }
    }
    // ★ НОВЕ ▲

    /**
     * Зчитує рядок з консолі. Якщо введення закінчилось (Ctrl+Z), програма завершується.
     *
     * @param prompt підказка для користувача
     * @return введений рядок без пробілів по краях
     */
    private static String readLine(String prompt) {
        System.out.print(prompt);
        if (!SCANNER.hasNextLine()) {
            System.out.println("\nВведення завершено.");
            System.exit(0);
        }
        return SCANNER.nextLine().trim();
    }

    /**
     * Зчитує непорожній рядок, повторюючи запит при порожньому введенні.
     *
     * @param prompt підказка
     * @return непорожній рядок
     */
    private static String readNonEmpty(String prompt) {
        while (true) {
            String line = readLine(prompt);
            if (!line.isEmpty()) {
                return line;
            }
            System.out.println("Поле не може бути порожнім.");
        }
    }

    /**
     * Зчитує ціле число, повторюючи запит при нечисловому введенні.
     *
     * @param prompt підказка
     * @return введене ціле число
     */
    private static int readInt(String prompt) {
        while (true) {
            String line = readLine(prompt);
            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("Потрібно ввести ціле число.");
            }
        }
    }

    /**
     * Зчитує ціле число з заданого діапазону, повторюючи запит при помилці.
     *
     * @param prompt підказка
     * @param min    мінімальне допустиме значення
     * @param max    максимальне допустиме значення
     * @return число з діапазону [min; max]
     */
    private static int readIntInRange(String prompt, int min, int max) {
        while (true) {
            int value = readInt(prompt);
            if (value >= min && value <= max) {
                return value;
            }
            System.out.println("Число має бути від " + min + " до " + max + ".");
        }
    }

    /**
     * Зчитує дійсне число (допускається кома), повторюючи запит при помилці.
     *
     * @param prompt підказка
     * @return введене число
     */
    private static double readDouble(String prompt) {
        while (true) {
            String line = readLine(prompt).replace(',', '.');
            try {
                return Double.parseDouble(line);
            } catch (NumberFormatException e) {
                System.out.println("Потрібно ввести число.");
            }
        }
    }

    /**
     * Показує список відділів і зчитує вибір користувача.
     *
     * @return обраний відділ
     */
    private static Department readDepartment() {
        Department[] values = Department.values();
        System.out.println("Оберіть відділ:");
        for (int i = 0; i < values.length; i++) {
            System.out.println((i + 1) + ". " + values[i]);
        }
        int choice = readIntInRange("Номер відділу: ", 1, values.length);
        return values[choice - 1];
    }
}