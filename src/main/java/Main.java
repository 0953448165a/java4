import java.text.Collator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Locale;
import java.util.Scanner;

public class Main {

    private static final String PROGRAM_TITLE =
            "Лабораторна робота №15: лямбда-вирази (Lambda expressions)";
    private static final String AUTHOR = "Мудрий Ярослав, група ІН-43-5";
    private static final String DATA_FILE = "input.txt";

    private static final Scanner SCANNER = new Scanner(System.in);
    private static final TextStorage STORAGE = new TextStorage(DATA_FILE);
    private static Company company;

    private record BaseData(String firstName, String lastName, String position,
                            double salary, int age, Department department) {
    }

    public static void main(String[] args) {
        printHeader();

        company = STORAGE.loadCompany();
        System.out.println("Завантажено об'єкт: " + company);

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
                    sortMenu();
                    break;
                case 5:
                    STORAGE.saveCompany(company);
                    break;
                case 6:
                    company = STORAGE.loadCompany();
                    System.out.println("Дані перезавантажено: " + company);
                    break;
                case 0:
                    STORAGE.saveCompany(company);
                    running = false;
                    System.out.println("Роботу завершено.");
                    break;
                default:
                    System.out.println("Невірний пункт меню. Оберіть число від 0 до 6.");
                    break;
            }
        }
        SCANNER.close();
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
        System.out.println("4. Вивести відсортовану інформацію про всі об'єкти");
        System.out.println("5. Зберегти дані компанії у " + DATA_FILE);
        System.out.println("6. Перезавантажити дані компанії з " + DATA_FILE);
        System.out.println("0. Зберегти та завершити роботу програми");
    }

    // ===================== Підменю сортування (Lambda expressions) =====================

    /**
     * Підменю вибору критерію сортування для пункту №4.
     * Для кожного критерію використовується лямбда-вираз Comparator<Employee>.
     */
    private static void sortMenu() {
        while (true) {
            System.out.println("\n--- Оберіть критерій сортування ---");
            System.out.println("1. Сортувати за базовою зарплатою (за зростанням)");
            System.out.println("2. Сортувати за місячною виплатою (за спаданням)");
            System.out.println("3. Сортувати за віком (за зростанням)");
            System.out.println("4. Сортувати за відділом та ПІБ (за алфавітом)");
            System.out.println("0. Повернутися в головне меню");
            int choice = readInt("Критерій сортування: ");

            switch (choice) {
                case 0:
                    return;
                case 1:
                    Comparator<Employee> bySalaryAsc = (o1, o2) -> {
                        int cmp = Double.compare(o1.getSalary(), o2.getSalary());
                        return cmp != 0 ? cmp : o1.compareTo(o2);
                    };
                    printSortedBy("базовою зарплатою (за зростанням)", bySalaryAsc);
                    return;
                case 2:
                    Comparator<Employee> byMonthlyPayDesc = (o1, o2) -> {
                        int cmp = Double.compare(o2.calculateMonthlyPay(), o1.calculateMonthlyPay());
                        return cmp != 0 ? cmp : o1.compareTo(o2);
                    };
                    printSortedBy("місячною виплатою (за спаданням)", byMonthlyPayDesc);
                    return;
                case 3:
                    Comparator<Employee> byAgeAsc = (o1, o2) -> {
                        int cmp = Integer.compare(o1.getAge(), o2.getAge());
                        return cmp != 0 ? cmp : o1.compareTo(o2);
                    };
                    printSortedBy("віком (за зростанням)", byAgeAsc);
                    return;
                case 4:
                    final Collator uaCollator = Collator.getInstance(Locale.forLanguageTag("uk-UA"));
                    uaCollator.setStrength(Collator.PRIMARY);
                    Comparator<Employee> byDepartmentAndName = (o1, o2) -> {
                        int cmp = uaCollator.compare(
                                o1.getDepartment().getTitle(),
                                o2.getDepartment().getTitle());
                        return cmp != 0 ? cmp : o1.compareTo(o2);
                    };
                    printSortedBy("відділом та ПІБ (за алфавітом)", byDepartmentAndName);
                    return;
                default:
                    System.out.println("Невірний пункт. Оберіть число від 0 до 4.");
                    break;
            }
        }
    }

    /**
     * Сортує об'єкти компанії за переданим Comparator та виводить результат у консоль.
     */
    private static void printSortedBy(String criterionTitle, Comparator<Employee> comparator) {
        ArrayList<Employee> sortedEmployees = company.getSortedEmployees(comparator);
        if (sortedEmployees.isEmpty()) {
            System.out.println("Список об'єктів порожній.");
            return;
        }

        System.out.println("\n=== Відсортований список об'єктів (за " + criterionTitle + ") ===");
        for (int i = 0; i < sortedEmployees.size(); i++) {
            Employee emp = sortedEmployees.get(i);
            printEmployee(i + 1, emp, company.getEmployeeQuantity(emp));
        }
        System.out.printf("Загальний місячний фонд оплати праці компанії: %.2f%n",
                company.calculateTotalMonthlyPayroll());
    }

    // ===================== Підменю пошуку =====================

    private static void searchMenu() {
        while (true) {
            System.out.println("\n--- Пошук працівників у компанії «" + company.getName() + "» ---");
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
        System.out.println("Знайдено унікальних записів: " + found.size());
        for (int i = 0; i < found.size(); i++) {
            Employee emp = found.get(i);
            printEmployee(i + 1, emp, company.getEmployeeQuantity(emp));
        }
    }

    // ===================== Створення об'єктів =====================

    private static void createObject() {
        while (true) {
            System.out.println("\n--- Оберіть тип працівника ---");
            System.out.println("1. Штатний працівник (FullTimeEmployee)");
            System.out.println("2. Працівник за контрактом (ContractEmployee)");
            System.out.println("3. Керівник (Manager)");
            System.out.println("4. Стажист (Intern)");
            System.out.println("0. Назад до головного меню");
            int choice = readInt("Тип: ");
            switch (choice) {
                case 0:
                    return;
                case 1:
                    createFullTimeEmployee();
                    return;
                case 2:
                    createContractEmployee();
                    return;
                case 3:
                    createManager();
                    return;
                case 4:
                    createIntern();
                    return;
                default:
                    System.out.println("Невірний пункт. Оберіть число від 0 до 4.");
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

    private static int readQuantity() {
        return readIntInRange("Кількість працівників з такими даними (1-1000): ", 1, 1000);
    }

    private static void addToCompany(Employee employee, int quantity, String successMessage) {
        int beforeUnique = company.getUniqueEmployeesCount();
        company.addEmployee(employee, quantity);
        if (company.getUniqueEmployeesCount() == beforeUnique) {
            System.out.println("Такий запис уже існує — кількість збільшено до "
                    + company.getEmployeeQuantity(employee) + ".");
        } else {
            System.out.println(successMessage + " (кількість: " + quantity + ").");
        }
    }

    private static void createFullTimeEmployee() {
        System.out.println("\n--- Новий штатний працівник ---");
        BaseData d = readBaseData();
        double bonus = readDouble("Щомісячна премія: ");
        int vacationDays = readInt("Днів відпустки на рік (0-60): ");
        int quantity = readQuantity();

        try {
            Employee employee = new FullTimeEmployee(d.firstName(), d.lastName(), d.position(),
                    d.salary(), d.age(), d.department(), bonus, vacationDays);
            addToCompany(employee, quantity, "Штатного працівника додано до компанії.");
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
        int quantity = readQuantity();

        try {
            Employee employee = new ContractEmployee(d.firstName(), d.lastName(), d.position(),
                    d.salary(), d.age(), d.department(), contractMonths, agency);
            addToCompany(employee, quantity, "Працівника за контрактом додано до компанії.");
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
        int quantity = readQuantity();

        try {
            Employee employee = new Manager(d.firstName(), d.lastName(), d.position(),
                    d.salary(), d.age(), d.department(), bonus, vacationDays,
                    teamSize, projectName);
            addToCompany(employee, quantity, "Керівника додано до компанії.");
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
        int quantity = readQuantity();

        try {
            Employee employee = new Intern(d.firstName(), d.lastName(), d.position(),
                    d.salary(), d.age(), d.department(), university, months, mentor);
            addToCompany(employee, quantity, "Стажиста додано до компанії.");
        } catch (IllegalArgumentException e) {
            System.out.println("Помилка: " + e.getMessage());
            System.out.println("Об'єкт не створено.");
        }
    }

    // ===================== Вивід =====================

    private static void printAll() {
        System.out.println("\n=== Інформація про компанію ===");
        System.out.println("Назва компанії: " + company.getName());
        System.out.println("Місто: " + company.getCity() + ", рік заснування: " + company.getFoundedYear());
        System.out.println("Унікальних позицій у штаті: " + company.getUniqueEmployeesCount());
        System.out.println("Загальна чисельність працівників: " + company.getTotalHeadcount());

        if (company.isEmpty()) {
            System.out.println("Список працівників порожній.");
            return;
        }

        ArrayList<Employee> employees = company.getEmployees();
        ArrayList<Integer> quantities = company.getQuantities();

        System.out.println("\n--- Склад працівників компанії ---");
        for (int i = 0; i < employees.size(); i++) {
            printEmployee(i + 1, employees.get(i), quantities.get(i));
        }
        System.out.printf("Загальний місячний фонд оплати праці компанії: %.2f%n",
                company.calculateTotalMonthlyPayroll());
    }

    private static void printEmployee(int number, Employee e, int quantity) {
        double unitPay = e.calculateMonthlyPay();
        double groupPay = unitPay * quantity;
        System.out.println(number + ". [" + e.getEmployeeType() + "] " + e
                + " | кількість: " + quantity);
        System.out.printf("   виплата на 1 працівника: %.2f, разом за позицією (%d шт.): %.2f%n",
                unitPay, quantity, groupPay);

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
    }

    // ===================== Введення =====================

    private static String readLine(String prompt) {
        System.out.print(prompt);
        if (!SCANNER.hasNextLine()) {
            System.out.println("\nВведення завершено.");
            if (company != null) {
                STORAGE.saveCompany(company);
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
        String[] names = {"FullTimeEmployee", "ContractEmployee", "Manager", "Intern"};
        System.out.println("Оберіть тип працівника:");
        for (int i = 0; i < names.length; i++) {
            System.out.println((i + 1) + ". " + names[i]);
        }
        int choice = readIntInRange("Тип: ", 1, names.length);
        return names[choice - 1];
    }
}