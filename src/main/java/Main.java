import java.util.ArrayList;
import java.util.Scanner;

/**
 * Драйвер програми: консольне меню для роботи зі списком працівників.
 * Некоректне введення не завершує програму.
 */
public class Main {

    private static final Scanner SCANNER = new Scanner(System.in);
    private static final ArrayList<Employee> EMPLOYEES = new ArrayList<>();

    /**
     * Точка входу. Показує меню, поки користувач не обере вихід.
     *
     * @param args аргументи командного рядка (не використовуються)
     */
    public static void main(String[] args) {
        boolean running = true;
        while (running) {
            printMenu();
            int choice = readInt("Ваш вибір: ");
            switch (choice) {
                case 1 -> createEmployee();
                case 2 -> printAll();
                case 3 -> {
                    running = false;
                    System.out.println("Роботу завершено.");
                }
                default -> System.out.println("Невірний пункт меню. Оберіть 1, 2 або 3.");
            }
        }
        SCANNER.close();
    }

    /** Виводить меню. */
    private static void printMenu() {
        System.out.println("\n=== МЕНЮ ===");
        System.out.println("1. Створити нового працівника");
        System.out.println("2. Вивести всіх працівників");
        System.out.println("3. Завершити роботу");
    }

    /**
     * Зчитує дані працівника і створює об'єкт.
     * Якщо дані некоректні, виняток перехоплюється, об'єкт не створюється,
     * програма повертається в меню.
     */
    private static void createEmployee() {
        System.out.println("\n--- Новий працівник ---");
        String firstName = readNonEmpty("Ім'я: ");
        String lastName = readNonEmpty("Прізвище: ");
        String position = readNonEmpty("Посада: ");
        double salary = readDouble("Зарплата: ");
        int age = readInt("Вік: ");
        Department department = readDepartment(); // ★ ЗМІНЕНО (було readNonEmpty("Відділ: "))

        try {
            Employee employee = new Employee(firstName, lastName, position, salary, age, department);
            EMPLOYEES.add(employee);
            System.out.println("Працівника додано.");
        } catch (IllegalArgumentException e) {
            System.out.println("Помилка: " + e.getMessage());
            System.out.println("Працівника не створено.");
        }
    }

    /** Виводить усіх працівників через toString(). */
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

    // ★ НОВЕ ▼
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
    // ★ НОВЕ ▲

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

    // ★ НОВЕ ▼
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
    // ★ НОВЕ ▲
}