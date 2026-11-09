import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    private static final String PROGRAM_TITLE =
            "Практична робота №7: наслідування, поліморфізм, колекції";
    private static final String AUTHOR = "Мудрий Ярослав, група ІН-43-5";

    private static final Scanner SCANNER = new Scanner(System.in);
    private static final ArrayList<Employee> EMPLOYEES = new ArrayList<>();

    /** Спільні дані, які зчитуються для працівника будь-якого типу. */
    private record BaseData(String firstName, String lastName, String position,
                            double salary, int age, Department department) {
    }

    public static void main(String[] args) {
        printHeader();

        boolean running = true;
        while (running) {
            printMenu();
            int choice = readInt("Ваш вибір: ");
            switch (choice) {
                case 1 -> createEmployee();
                case 2 -> createFullTimeEmployee();
                case 3 -> createContractEmployee();
                case 4 -> printAll();
                case 5 -> showPolymorphism();
                case 6 -> {
                    running = false;
                    System.out.println("Роботу завершено.");
                }
                default -> System.out.println("Невірний пункт меню. Оберіть число від 1 до 6.");
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
        System.out.println("1. Створити працівника (Employee)");
        System.out.println("2. Створити штатного працівника (FullTimeEmployee)");
        System.out.println("3. Створити працівника за контрактом (ContractEmployee)");
        System.out.println("4. Вивести всіх працівників");
        System.out.println("5. Демонстрація поліморфізму (виплати за місяць)");
        System.out.println("6. Завершити роботу");
    }


    private static BaseData readBaseData() {
        String firstName = readNonEmpty("Ім'я: ");
        String lastName = readNonEmpty("Прізвище: ");
        String position = readNonEmpty("Посада: ");
        double salary = readDouble("Зарплата: ");
        int age = readInt("Вік: ");
        Department department = readDepartment();
        return new BaseData(firstName, lastName, position, salary, age, department);
    }

    private static void createEmployee() {
        System.out.println("\n--- Новий працівник ---");
        BaseData d = readBaseData();

        try {
            Employee employee = new Employee(d.firstName(), d.lastName(), d.position(),
                    d.salary(), d.age(), d.department());
            EMPLOYEES.add(employee);
            System.out.println("Працівника додано.");
        } catch (IllegalArgumentException e) {
            System.out.println("Помилка: " + e.getMessage());
            System.out.println("Працівника не створено.");
        }
    }

    private static void createFullTimeEmployee() {
        System.out.println("\n--- Новий штатний працівник ---");
        BaseData d = readBaseData();
        double bonus = readDouble("Щомісячна премія: ");
        int vacationDays = readInt("Днів відпустки на рік (0-60): ");

        try {
            Employee employee = new FullTimeEmployee(d.firstName(), d.lastName(), d.position(),
                    d.salary(), d.age(), d.department(), bonus, vacationDays);
            EMPLOYEES.add(employee);
            System.out.println("Штатного працівника додано.");
        } catch (IllegalArgumentException e) {
            System.out.println("Помилка: " + e.getMessage());
            System.out.println("Працівника не створено.");
        }
    }

    private static void createContractEmployee() {
        System.out.println("\n--- Новий працівник за контрактом ---");
        BaseData d = readBaseData();
        int contractMonths = readInt("Строк контракту, місяців (1-60): ");
        String agency = readNonEmpty("Агенція: ");

        try {
            Employee employee = new ContractEmployee(d.firstName(), d.lastName(), d.position(),
                    d.salary(), d.age(), d.department(), contractMonths, agency);
            EMPLOYEES.add(employee);
            System.out.println("Працівника за контрактом додано.");
        } catch (IllegalArgumentException e) {
            System.out.println("Помилка: " + e.getMessage());
            System.out.println("Працівника не створено.");
        }
    }


    private static void printAll() {
        if (EMPLOYEES.isEmpty()) {
            System.out.println("Список порожній.");
            return;
        }
        System.out.println("\n=== Список працівників ===");
        for (int i = 0; i < EMPLOYEES.size(); i++) {
            Employee e = EMPLOYEES.get(i);
            System.out.println((i + 1) + ". [" + e.getEmployeeType() + "] " + e);
        }
    }

    private static void showPolymorphism() {
        if (EMPLOYEES.isEmpty()) {
            System.out.println("Список порожній, демонструвати нічого.");
            return;
        }
        System.out.println("\n=== Демонстрація поліморфізму ===");
        double total = 0;
        for (int i = 0; i < EMPLOYEES.size(); i++) {
            Employee e = EMPLOYEES.get(i); // посилання базового типу
            double pay = e.calculateMonthlyPay(); // викликається метод реального класу
            total += pay;
            System.out.printf("%d. %s %s | %s | виплата за місяць: %.2f%n",
                    i + 1, e.getFirstName(), e.getLastName(), e.getEmployeeType(), pay);

            if (e instanceof FullTimeEmployee fullTime) {
                System.out.printf("   премія: %.2f, днів відпустки: %d%n",
                        fullTime.getBonus(), fullTime.getVacationDays());
            } else if (e instanceof ContractEmployee contract) {
                System.out.printf("   контракт: %d міс., агенція: %s, вартість контракту: %.2f%n",
                        contract.getContractMonths(), contract.getAgency(), contract.getTotalContractCost());
            }
        }
        System.out.printf("Разом виплат за місяць: %.2f%n", total);
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