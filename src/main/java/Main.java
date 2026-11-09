import java.util.ArrayList;
import java.util.Scanner;

/**
 * Драйвер програми: консольне меню для роботи зі списком працівників.
 * Містить інформаційну шапку програми. Некоректне введення не завершує програму.
 */
public class Main {

    private static final String PROGRAM_TITLE =
            "Практична робота №7: наслідування, поліморфізм, колекції";
    private static final String AUTHOR = "Мудрий Ярослав, група ІН-43-5";

    private static final Scanner SCANNER = new Scanner(System.in);
    private static final ArrayList<Employee> EMPLOYEES = new ArrayList<>();

    /**
     * Точка входу. Виводить шапку й показує меню,
     * поки користувач не обере вихід.
     *
     * @param args аргументи командного рядка (не використовуються)
     */
    public static void main(String[] args) {
        printHeader();

        boolean running = true;
        while (running) {
            printMenu();
            int choice = readInt("Ваш вибір: ");
            switch (choice) {
                case 1 -> createEmployee();
                case 2 -> printAll();
                case 3 -> copyEmployee();
                case 4 -> {
                    running = false;
                    System.out.println("Роботу завершено.");
                }
                default -> System.out.println("Невірний пункт меню. Оберіть число від 1 до 4.");
            }
        }
        SCANNER.close();
    }

    /** Виводить інформаційну шапку програми. */
    private static void printHeader() {
        System.out.println("==============================================");
        System.out.println(PROGRAM_TITLE);
        System.out.println("Виконав: " + AUTHOR);
        System.out.println("==============================================");
    }

    /** Виводить меню. */
    private static void printMenu() {
        System.out.println("\n=== МЕНЮ ===");
        System.out.println("1. Створити нового працівника");
        System.out.println("2. Вивести всіх працівників");
        System.out.println("3. Створити копію працівника (конструктор копіювання)");
        System.out.println("4. Завершити роботу");
    }

    /**
     * Зчитує дані працівника і додає його до списку.
     * Якщо дані некоректні, виняток перехоплюється й програма повертається в меню.
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
            EMPLOYEES.add(employee);
            System.out.println("Працівника додано.");
        } catch (IllegalArgumentException e) {
            System.out.println("Помилка: " + e.getMessage());
            System.out.println("Працівника не створено.");
        }
    }

    /** Виводить усіх працівників зі списку через toString(). */
    private static void printAll() {
        if (EMPLOYEES.isEmpty()) {
            System.out.println("Список порожній.");
            return;
        }
        System.out.println("\n=== Список працівників ===");
        for (int i = 0; i < EMPLOYEES.size(); i++) {
            System.out.println((i + 1) + ". " + EMPLOYEES.get(i));
        }
    }

    /**
     * Створює копію обраного працівника через конструктор копіювання
     * і демонструє різницю між equals() та порівнянням посилань.
     */
    private static void copyEmployee() {
        if (EMPLOYEES.isEmpty()) {
            System.out.println("Немає кого копіювати: список порожній.");
            return;
        }
        printAll();
        int number = readIntInRange("Номер працівника для копіювання: ", 1, EMPLOYEES.size());

        try {
            Employee original = EMPLOYEES.get(number - 1);
            Employee copy = new Employee(original);
            EMPLOYEES.add(copy);
            System.out.println("Копію створено: " + copy);
            System.out.println("original.equals(copy): " + original.equals(copy));
            System.out.println("original == copy: " + (original == copy));
        } catch (IllegalArgumentException e) {
            System.out.println("Помилка: " + e.getMessage());
        }
    }

private static String readLine(String prompt) {
        System.out.print(prompt);
        if (!SCANNER.hasNextLine()) {
            System.out.println("\nВведення завершено.");
            System.exit(0);
        }
        return SCANNER.nextLine().trim();
    }

    private static String readNonEmpty(String prompt) {
        while (true) {
            String line = readLine(prompt);
            if (!line.isEmpty()) {
                return line;
            }
            System.out.println("Поле не може бути порожнім.");
        }
    }

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

    private static int readIntInRange(String prompt, int min, int max) {
        while (true) {
            int value = readInt(prompt);
            if (value >= min && value <= max) {
                return value;
            }
            System.out.println("Число має бути від " + min + " до " + max + ".");
        }
    }

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