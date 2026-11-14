import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    private static final String PROGRAM_TITLE =
            "Практична робота №10: пошук у колекціях";
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
                case 1 -> searchMenu();
                case 2 -> createObject();
                case 3 -> printAll();
                case 4 -> {
                    storage.save(EMPLOYEES);
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
        System.out.println("1. Пошук об'єкта");
        System.out.println("2. Створити новий об'єкт");
        System.out.println("3. Вивести інформацію про всі об'єкти");
        System.out.println("4. Завершити роботу програми");
    }

    // ===================== Підменю пошуку =====================

    /** Підменю пошуку. Пункт 0 повертає в головне меню. */
    private static void searchMenu() {
        while (true) {
            System.out.println("\n--- Пошук об'єкта ---");
            System.out.println("1. За прізвищем");
            System.out.println("2. За відділом");
            System.out.println("3. За діапазоном зарплати");
            System.out.println("4. За діапазоном віку");
            System.out.println("5. За типом працівника");
            System.out.println("0. Назад до головного меню");
            int choice = readInt("Критерій: ");
            switch (choice) {
                case 0 -> {
                    return;
                }
                case 1 -> runSearchByLastName();
                case 2 -> runSearchByDepartment();
                case 3 -> runSearchBySalaryRange();
                case 4 -> runSearchByAgeRange();
                case 5 -> runSearchByType();
                default -> System.out.println("Невірний пункт. Оберіть число від 0 до 5.");
            }
        }
    }

    private static void runSearchByLastName() {
        String query = readNonEmpty("Введіть прізвище або його частину: ");
        ArrayList<Employee> found = searchByLastName(EMPLOYEES, query);
        printSearchResult("прізвище містить «" + query + "»", found);
    }

    private static void runSearchByDepartment() {
        Department department = readDepartment();
        ArrayList<Employee> found = searchByDepartment(EMPLOYEES, department);
        printSearchResult("відділ " + department, found);
    }

    private static void runSearchBySalaryRange() {
        double min = readDouble("Мінімальна зарплата: ");
        double max = readDouble("Максимальна зарплата: ");
        if (min > max) {
            System.out.println("Помилка: мінімум не може бути більшим за максимум.");
            return;
        }
        ArrayList<Employee> found = searchBySalaryRange(EMPLOYEES, min, max);
        printSearchResult("зарплата від " + min + " до " + max, found);
    }

    private static void runSearchByAgeRange() {
        int min = readInt("Мінімальний вік: ");
        int max = readInt("Максимальний вік: ");
        if (min > max) {
            System.out.println("Помилка: мінімум не може бути більшим за максимум.");
            return;
        }
        ArrayList<Employee> found = searchByAgeRange(EMPLOYEES, min, max);
        printSearchResult("вік від " + min + " до " + max, found);
    }

    private static void runSearchByType() {
        String typeName = readTypeName();
        ArrayList<Employee> found = searchByType(EMPLOYEES, typeName);
        printSearchResult("тип " + typeName, found);
    }

    /** Виводить результат пошуку або повідомлення про відсутність збігів. */
    private static void printSearchResult(String criterion, ArrayList<Employee> found) {
        System.out.println("\nКритерій: " + criterion);
        if (found.isEmpty()) {
            System.out.println("Об'єктів, що відповідають умовам пошуку, не знайдено.");
            return;
        }
        System.out.println("Знайдено об'єктів: " + found.size());
        for (int i = 0; i < found.size(); i++) {
            printEmployee(i + 1, found.get(i));
        }
    }

    // ===================== Пошук (колекція не змінюється) =====================

    /** Шукає працівників, прізвище яких містить заданий рядок (без урахування регістру). */
    static ArrayList<Employee> searchByLastName(ArrayList<Employee> source, String query) {
        ArrayList<Employee> found = new ArrayList<>();
        String needle = query.trim().toLowerCase();
        for (Employee e : source) {
            if (e.getLastName().toLowerCase().contains(needle)) {
                found.add(e);
            }
        }
        return found;
    }

    /** Шукає працівників заданого відділу. */
    static ArrayList<Employee> searchByDepartment(ArrayList<Employee> source, Department department) {
        ArrayList<Employee> found = new ArrayList<>();
        for (Employee e : source) {
            if (e.getDepartment() == department) {
                found.add(e);
            }
        }
        return found;
    }

    /** Шукає працівників із зарплатою в діапазоні [min; max] (межі включно). */
    static ArrayList<Employee> searchBySalaryRange(ArrayList<Employee> source, double min, double max) {
        ArrayList<Employee> found = new ArrayList<>();
        for (Employee e : source) {
            if (e.getSalary() >= min && e.getSalary() <= max) {
                found.add(e);
            }
        }
        return found;
    }

    /** Шукає працівників віком у діапазоні [min; max] (межі включно). */
    static ArrayList<Employee> searchByAgeRange(ArrayList<Employee> source, int min, int max) {
        ArrayList<Employee> found = new ArrayList<>();
        for (Employee e : source) {
            if (e.getAge() >= min && e.getAge() <= max) {
                found.add(e);
            }
        }
        return found;
    }

    /** Шукає об'єкти точного класу (наприклад, "Manager" не знайде FullTimeEmployee і навпаки). */
    static ArrayList<Employee> searchByType(ArrayList<Employee> source, String typeName) {
        ArrayList<Employee> found = new ArrayList<>();
        for (Employee e : source) {
            if (e.getClass().getSimpleName().equals(typeName)) {
                found.add(e);
            }
        }
        return found;
    }

    // ===================== Створення об'єктів =====================

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

    // ===================== Вивід =====================

    /** Виводить усі об'єкти колекції та демонструє поліморфізм. */
    private static void printAll() {
        if (EMPLOYEES.isEmpty()) {
            System.out.println("Список порожній.");
            return;
        }
        System.out.println("\n=== Список об'єктів ===");
        double total = 0;
        for (int i = 0; i < EMPLOYEES.size(); i++) {
            total += printEmployee(i + 1, EMPLOYEES.get(i));
        }
        System.out.printf("Разом виплат за місяць: %.2f%n", total);
    }

    /** Виводить один об'єкт із додатковими полями нащадка. Повертає його місячну виплату. */
    private static double printEmployee(int number, Employee e) {
        double pay = e.calculateMonthlyPay(); // викликається метод реального класу
        System.out.println(number + ". [" + e.getEmployeeType() + "] " + e);
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
        return pay;
    }

    // ===================== Введення =====================

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

    /** Дає змогу обрати тип працівника для пошуку. Повертає назву класу. */
    private static String readTypeName() {
        String[] names = {"Employee", "FullTimeEmployee", "ContractEmployee", "Manager", "Intern"};
        System.out.println("Оберіть тип працівника:");
        for (int i = 0; i < names.length; i++) {
            System.out.println((i + 1) + ". " + names[i]);
        }
        int choice = readIntInRange("Тип: ", 1, names.length);
        return names[choice - 1];
    }
}