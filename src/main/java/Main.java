import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    private static final String PROGRAM_TITLE =
            "Практична робота №11: колекції, агрегація, класи-обгортки";
    private static final String AUTHOR = "Мудрий Ярослав, група ІН-43-5";
    private static final String DEFAULT_FILE = "input.txt";

    private static final Scanner SCANNER = new Scanner(System.in);
    private static TextStorage textStorage;
    private static Company company;

    private static final class BaseData {
        private final String firstName;
        private final String lastName;
        private final String position;
        private final double salary;
        private final int age;
        private final Department department;

        private BaseData(String firstName, String lastName, String position,
                         double salary, int age, Department department) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.position = position;
            this.salary = salary;
            this.age = age;
            this.department = department;
        }
    }

    public static void main(String[] args) {
        printHeader();

        textStorage = new TextStorage(DEFAULT_FILE);
        company = initializeCompany();

        boolean running = true;
        while (running) {
            printMenu();
            int choice = readInt("Ваш вибір: ");
            switch (choice) {
                case 1:
                    searchMenu();
                    break;
                case 2:
                    createObject();
                    break;
                case 3:
                    printAll();
                    break;
                case 4:
                    textStorage.saveCompany(company);
                    break;
                case 5:
                    company = initializeCompany();
                    break;
                case 0:
                    textStorage.saveCompany(company);
                    running = false;
                    System.out.println("Роботу завершено.");
                    break;
                default:
                    System.out.println("Невірний пункт меню. Оберіть число від 0 до 5.");
                    break;
            }
        }
        SCANNER.close();
    }

    private static Company initializeCompany() {
        try {
            return textStorage.loadCompany();
        } catch (Exception e) {
            System.out.println("Не вдалося завантажити компанію з " + DEFAULT_FILE + ": " + e.getMessage());
            System.out.println("Створіть компанію вручну:");
            String name = readNonEmpty("Назва компанії: ");
            String city = readNonEmpty("Місто: ");
            int year = readIntInRange("Рік заснування (1800-2026): ", 1800, 2026);
            return new Company(name, city, year);
        }
    }

    private static void printHeader() {
        System.out.println("==============================================");
        System.out.println(PROGRAM_TITLE);
        System.out.println("Виконав: " + AUTHOR);
        System.out.println("==============================================");
    }

    private static void printMenu() {
        System.out.println("\n=== МЕНЮ УПРАВЛІННЯ КОМПАНІЄЮ ===");
        System.out.println("1. Пошук працівників у компанії");
        System.out.println("2. Додати працівника (або збільшити кількість існуючого)");
        System.out.println("3. Вивести інформацію про компанію та всіх працівників");
        System.out.println("4. Зберегти дані компанії у " + DEFAULT_FILE);
        System.out.println("5. Перезавантажити дані компанії з " + DEFAULT_FILE);
        System.out.println("0. Зберегти та завершити роботу програми");
    }

    // ===================== Підменю пошуку =====================

    private static void searchMenu() {
        while (true) {
            System.out.println("\n--- Пошук у компанії «" + company.getName() + "» ---");
            System.out.println("1. За прізвищем");
            System.out.println("2. За відділом");
            System.out.println("3. За діапазоном зарплати");
            System.out.println("4. За діапазоном віку");
            System.out.println("5. За типом працівника");
            System.out.println("0. Назад до головного меню");
            int choice = readInt("Критерій: ");
            switch (choice) {
                case 0:
                    return;
                case 1:
                    runSearchByLastName();
                    break;
                case 2:
                    runSearchByDepartment();
                    break;
                case 3:
                    runSearchBySalaryRange();
                    break;
                case 4:
                    runSearchByAgeRange();
                    break;
                case 5:
                    runSearchByType();
                    break;
                default:
                    System.out.println("Невірний пункт. Оберіть число від 0 до 5.");
                    break;
            }
        }
    }

    private static void runSearchByLastName() {
        String query = readNonEmpty("Введіть прізвище або його частину: ");
        ArrayList<Employee> found = company.searchByLastName(query);
        printSearchResult("прізвище містить «" + query + "»", found);
    }

    private static void runSearchByDepartment() {
        Department department = readDepartment();
        ArrayList<Employee> found = company.searchByDepartment(department);
        printSearchResult("відділ " + department, found);
    }

    private static void runSearchBySalaryRange() {
        double min = readDouble("Мінімальна зарплата: ");
        double max = readDouble("Максимальна зарплата: ");
        if (min > max) {
            System.out.println("Помилка: мінімум не може бути більшим за максимум.");
            return;
        }
        ArrayList<Employee> found = company.searchBySalaryRange(min, max);
        printSearchResult("зарплата від " + min + " до " + max, found);
    }

    private static void runSearchByAgeRange() {
        int min = readInt("Мінімальний вік: ");
        int max = readInt("Максимальний вік: ");
        if (min > max) {
            System.out.println("Помилка: мінімум не може бути більшим за максимум.");
            return;
        }
        ArrayList<Employee> found = company.searchByAgeRange(min, max);
        printSearchResult("вік від " + min + " до " + max, found);
    }

    private static void runSearchByType() {
        String typeName = readTypeName();
        ArrayList<Employee> found = company.searchByType(typeName);
        printSearchResult("тип " + typeName, found);
    }

    private static void printSearchResult(String criterion, ArrayList<Employee> found) {
        System.out.println("\nКритерій: " + criterion);
        if (found.isEmpty()) {
            System.out.println("Об'єктів, що відповідають умовам пошуку, не знайдено.");
            return;
        }
        System.out.println("Знайдено позицій: " + found.size());
        for (int i = 0; i < found.size(); i++) {
            Employee emp = found.get(i);
            int qty = company.getEmployeeQuantity(emp);
            printEmployee(i + 1, emp, qty);
        }
    }

    // Статичні методи збережено для сумісності з SearchTest з Практичної роботи №10
    static ArrayList<Employee> searchByLastName(ArrayList<Employee> source, String query) {
        ArrayList<Employee> found = new ArrayList<Employee>();
        String needle = query.trim().toLowerCase();
        for (int i = 0; i < source.size(); i++) {
            Employee e = source.get(i);
            if (e.getLastName().toLowerCase().contains(needle)) {
                found.add(e);
            }
        }
        return found;
    }

    static ArrayList<Employee> searchByDepartment(ArrayList<Employee> source, Department department) {
        ArrayList<Employee> found = new ArrayList<Employee>();
        for (int i = 0; i < source.size(); i++) {
            Employee e = source.get(i);
            if (e.getDepartment() == department) {
                found.add(e);
            }
        }
        return found;
    }

    static ArrayList<Employee> searchBySalaryRange(ArrayList<Employee> source, double min, double max) {
        ArrayList<Employee> found = new ArrayList<Employee>();
        for (int i = 0; i < source.size(); i++) {
            Employee e = source.get(i);
            if (e.getSalary() >= min && e.getSalary() <= max) {
                found.add(e);
            }
        }
        return found;
    }

    static ArrayList<Employee> searchByAgeRange(ArrayList<Employee> source, int min, int max) {
        ArrayList<Employee> found = new ArrayList<Employee>();
        for (int i = 0; i < source.size(); i++) {
            Employee e = source.get(i);
            if (e.getAge() >= min && e.getAge() <= max) {
                found.add(e);
            }
        }
        return found;
    }

    static ArrayList<Employee> searchByType(ArrayList<Employee> source, String typeName) {
        ArrayList<Employee> found = new ArrayList<Employee>();
        for (int i = 0; i < source.size(); i++) {
            Employee e = source.get(i);
            if (e.getClass().getSimpleName().equals(typeName)) {
                found.add(e);
            }
        }
        return found;
    }

    // ===================== Додавання об'єктів до компанії =====================

    private static void createObject() {
        while (true) {
            System.out.println("\n--- Оберіть тип працівника для додавання ---");
            System.out.println("1. Працівник (Employee)");
            System.out.println("2. Штатний працівник (FullTimeEmployee)");
            System.out.println("3. Працівник за контрактом (ContractEmployee)");
            System.out.println("4. Керівник (Manager)");
            System.out.println("5. Стажист (Intern)");
            System.out.println("0. Назад до головного меню");
            int choice = readInt("Тип: ");
            switch (choice) {
                case 0:
                    return;
                case 1:
                    createEmployee();
                    return;
                case 2:
                    createFullTimeEmployee();
                    return;
                case 3:
                    createContractEmployee();
                    return;
                case 4:
                    createManager();
                    return;
                case 5:
                    createIntern();
                    return;
                default:
                    System.out.println("Невірний пункт. Оберіть число від 0 до 5.");
                    break;
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

    private static void addEmployeeWithQuantity(Employee employee) {
        int quantity = readInt("Кількість (шт.): ");
        company.addNewEmployee(employee, quantity);
        System.out.println("Операцію виконано. Актуальна кількість для цього працівника у компанії: "
                + company.getEmployeeQuantity(employee) + " шт.");
    }

    private static void createEmployee() {
        System.out.println("\n--- Новий працівник ---");
        BaseData d = readBaseData();
        try {
            Employee employee = new Employee(d.firstName, d.lastName, d.position,
                    d.salary, d.age, d.department);
            addEmployeeWithQuantity(employee);
        } catch (IllegalArgumentException e) {
            System.out.println("Помилка: " + e.getMessage());
        }
    }

    private static void createFullTimeEmployee() {
        System.out.println("\n--- Новий штатний працівник ---");
        BaseData d = readBaseData();
        double bonus = readDouble("Щомісячна премія: ");
        int vacationDays = readInt("Днів відпустки на рік (0-60): ");
        try {
            Employee employee = new FullTimeEmployee(d.firstName, d.lastName, d.position,
                    d.salary, d.age, d.department, bonus, vacationDays);
            addEmployeeWithQuantity(employee);
        } catch (IllegalArgumentException e) {
            System.out.println("Помилка: " + e.getMessage());
        }
    }

    private static void createContractEmployee() {
        System.out.println("\n--- Новий працівник за контрактом ---");
        BaseData d = readBaseData();
        int contractMonths = readInt("Строк контракту, місяців (1-60): ");
        String agency = readNonEmpty("Агенція: ");
        try {
            Employee employee = new ContractEmployee(d.firstName, d.lastName, d.position,
                    d.salary, d.age, d.department, contractMonths, agency);
            addEmployeeWithQuantity(employee);
        } catch (IllegalArgumentException e) {
            System.out.println("Помилка: " + e.getMessage());
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
            Employee employee = new Manager(d.firstName, d.lastName, d.position,
                    d.salary, d.age, d.department, bonus, vacationDays,
                    teamSize, projectName);
            addEmployeeWithQuantity(employee);
        } catch (IllegalArgumentException e) {
            System.out.println("Помилка: " + e.getMessage());
        }
    }

    private static void createIntern() {
        System.out.println("\n--- Новий стажист ---");
        BaseData d = readBaseData();
        String university = readNonEmpty("Університет: ");
        int months = readInt("Тривалість стажування, місяців (1-12): ");
        String mentor = readNonEmpty("Ім'я ментора: ");
        try {
            Employee employee = new Intern(d.firstName, d.lastName, d.position,
                    d.salary, d.age, d.department, university, months, mentor);
            addEmployeeWithQuantity(employee);
        } catch (IllegalArgumentException e) {
            System.out.println("Помилка: " + e.getMessage());
        }
    }

    // ===================== Вивід =====================

    private static void printAll() {
        System.out.println("\n==============================================");
        System.out.println("Компанія: " + company.getName()
                + " (" + company.getCity() + ", заснована у " + company.getFoundedYear() + " р.)");
        System.out.println("==============================================");

        ArrayList<Employee> employees = company.getEmployees();
        ArrayList<Integer> quantities = company.getQuantities();

        if (employees.isEmpty()) {
            System.out.println("Список працівників порожній.");
            return;
        }

        for (int i = 0; i < employees.size(); i++) {
            printEmployee(i + 1, employees.get(i), quantities.get(i).intValue());
        }

        System.out.println("----------------------------------------------");
        System.out.println("Унікальних позицій: " + employees.size()
                + " | Загальний штат: " + company.getTotalHeadcount() + " осіб");
        System.out.printf("Сумарний фонд оплати праці за місяць: %.2f%n",
                company.calculateTotalMonthlyPayroll());
    }

    private static double printEmployee(int number, Employee e, int quantity) {
        double pay = e.calculateMonthlyPay();
        System.out.println(number + ". [" + e.getEmployeeType() + "] " + e + " | Кількість: " + quantity + " шт.");
        System.out.printf("   виплата за місяць (1 особа): %.2f | разом (%d шт.): %.2f%n",
                pay, quantity, pay * quantity);

        if (e instanceof Manager) {
            Manager m = (Manager) e;
            System.out.printf("   команда: %d, проєкт: %s, надбавка за керування: %.2f%n",
                    m.getTeamSize(), m.getProjectName(), m.getManagementAllowance());
        } else if (e instanceof FullTimeEmployee) {
            FullTimeEmployee f = (FullTimeEmployee) e;
            System.out.printf("   премія: %.2f, днів відпустки: %d%n",
                    f.getBonus(), f.getVacationDays());
        } else if (e instanceof ContractEmployee) {
            ContractEmployee c = (ContractEmployee) e;
            System.out.printf("   контракт: %d міс., агенція: %s, вартість контракту: %.2f%n",
                    c.getContractMonths(), c.getAgency(), c.getTotalContractCost());
        } else if (e instanceof Intern) {
            Intern in = (Intern) e;
            System.out.printf("   університет: %s, ментор: %s, виплата за стажування: %.2f%n",
                    in.getUniversity(), in.getMentorName(), in.getTotalStipend());
        }
        return pay * quantity;
    }

    // ===================== Введення =====================

    private static String readLine(String prompt) {
        System.out.print(prompt);
        if (!SCANNER.hasNextLine()) {
            System.out.println("\nВведення завершено.");
            if (textStorage != null && company != null) {
                textStorage.saveCompany(company);
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