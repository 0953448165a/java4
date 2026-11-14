import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    private static final String PROGRAM_TITLE =
            "Практична робота №9: ієрархія класів, робота з файлами";
    private static final String AUTHOR = "Мудрий Ярослав, група ІН-43-5";

    private static final Scanner SCANNER = new Scanner(System.in);
    // Колекція заповнюється лише даними, зчитаними з файлу на старті програми
    private static final ArrayList<Employee> EMPLOYEES = new ArrayList<>();
    private static DataStorage storage;

    /** Спільні дані, які зчитуються для працівника будь-якого типу. */
    private record BaseData(String firstName, String lastName, String position,
                            double salary, int age, Department department) {
    }

    public static void main(String[] args) {
        printHeader();

        storage = chooseStorage();
        EMPLOYEES.addAll(storage.load());

        boolean running = true;
        while (running) {
            printMenu();
            int choice = readInt("Ваш вибір: ");
            switch (choice) {
                case 1 -> createObject();
                case 2 -> printAll();
                case 3 -> {
                    storage.save(EMPLOYEES);
                    running = false;
                    System.out.println("Роботу завершено.");
                }
                default -> System.out.println("Невірний пункт меню. Оберіть число від 1 до 3.");
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

    /** Дає змогу обрати формат файлу, з яким працюватиме програма. */
    private static DataStorage chooseStorage() {
        System.out.println("\nОберіть формат файлу даних:");
        System.out.println("1. input.txt (текстовий формат)");
        System.out.println("2. input.json (JSON)");
        int choice = readIntInRange("Формат: ", 1, 2);
        if (choice == 1) {
            return new TextStorage("input.txt");
        }
        return new JsonStorage("input.json");
    }

    /** Виводить головне меню. */
    private static void printMenu() {
        System.out.println("\n=== МЕНЮ ===");
        System.out.println("1. Створити новий об'єкт");
        System.out.println("2. Вивести інформацію про всі об'єкти");
        System.out.println("3. Завершити роботу програми");
    }

    /** Підменю вибору типу об'єкта. Пункт 0 повертає в головне меню без створення. */
    private static void createObject() {
        while (true) {
            System.out.println("\n--- Оберіть тип об'єкта ---");
            System.out.println("1. Працівник (Employee)");
            System.out.println("2. Штатний працівник (FullTimeEmployee)");
            System.out.println("3. Працівник за контрактом (ContractEmployee)");
            System.out.println("4. Керівник (Manager)");
            System.out.println("5. Стажист (Intern)");
            System.out.println("0. Назад до головного меню");
            int choice = readInt("Тип: ");
            switch (choice) {
                case 0 -> {
                    return;
                }
                case 1 -> {
                    createEmployee();
                    return;
                }
                case 2 -> {
                    createFullTimeEmployee();
                    return;
                }
                case 3 -> {
                    createContractEmployee();
                    return;
                }
                case 4 -> {
                    createManager();
                    return;
                }
                case 5 -> {
                    createIntern();
                    return;
                }
                default -> System.out.println("Невірний пункт. Оберіть число від 0 до 5.");
            }
        }
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
            System.out.println("Об'єкт не створено.");
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
            System.out.println("Об'єкт не створено.");
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
            System.out.println("Об'єкт не створено.");
        }
    }

    private static void createManager() {
        System.out.println("\n--- Новий керівник ---");
        BaseData d = readBaseData();
        double bonus = readDouble("Щомісячна премія: ");
        int vacationDays = readInt("Днів відпустки на рік (0-60): ");
        int teamSize = readInt("Розмір команди (1-100): ");
        String projectName = readNonEmpty("Назва проєкту: ");

        try {
            Employee employee = new Manager(d.firstName(), d.lastName(), d.position(),
                    d.salary(), d.age(), d.department(), bonus, vacationDays,
                    teamSize, projectName);
            EMPLOYEES.add(employee);
            System.out.println("Керівника додано.");
        } catch (IllegalArgumentException e) {
            System.out.println("Помилка: " + e.getMessage());
            System.out.println("Об'єкт не створено.");
        }
    }

    private static void createIntern() {
        System.out.println("\n--- Новий стажист ---");
        BaseData d = readBaseData();
        String university = readNonEmpty("Університет: ");
        int months = readInt("Тривалість стажування, місяців (1-12): ");
        String mentor = readNonEmpty("Ім'я ментора: ");

        try {
            Employee employee = new Intern(d.firstName(), d.lastName(), d.position(),
                    d.salary(), d.age(), d.department(), university, months, mentor);
            EMPLOYEES.add(employee);
            System.out.println("Стажиста додано.");
        } catch (IllegalArgumentException e) {
            System.out.println("Помилка: " + e.getMessage());
            System.out.println("Об'єкт не створено.");
        }
    }

    /** Виводить усі об'єкти колекції та демонструє поліморфізм. */
    private static void printAll() {
        if (EMPLOYEES.isEmpty()) {
            System.out.println("Список порожній.");
            return;
        }
        System.out.println("\n=== Список об'єктів ===");
        double total = 0;
        for (int i = 0; i < EMPLOYEES.size(); i++) {
            Employee e = EMPLOYEES.get(i); // посилання базового типу
            double pay = e.calculateMonthlyPay(); // викликається метод реального класу
            total += pay;
            System.out.println((i + 1) + ". [" + e.getEmployeeType() + "] " + e);
            System.out.printf("   виплата за місяць: %.2f%n", pay);

            // Manager перевіряється раніше за FullTimeEmployee, бо є його нащадком
            if (e instanceof Manager m) {
                System.out.printf("   команда: %d, проєкт: %s, надбавка за керування: %.2f%n",
                        m.getTeamSize(), m.getProjectName(), m.getManagementAllowance());
            } else if (e instanceof FullTimeEmployee f) {
                System.out.printf("   премія: %.2f, днів відпустки: %d%n",
                        f.getBonus(), f.getVacationDays());
            } else if (e instanceof ContractEmployee c) {
                System.out.printf("   контракт: %d міс., агенція: %s, вартість контракту: %.2f%n",
                        c.getContractMonths(), c.getAgency(), c.getTotalContractCost());
            } else if (e instanceof Intern in) {
                System.out.printf("   університет: %s, ментор: %s, виплата за стажування: %.2f%n",
                        in.getUniversity(), in.getMentorName(), in.getTotalStipend());
            }
        }
        System.out.printf("Разом виплат за місяць: %.2f%n", total);
    }

    private static String readLine(String prompt) {
        System.out.print(prompt);
        if (!SCANNER.hasNextLine()) {
            System.out.println("\nВведення завершено.");
            if (storage != null) {
                storage.save(EMPLOYEES);
            }
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