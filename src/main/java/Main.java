import java.util.Scanner; 


public class Main {

   
    private static final String PROGRAM_TITLE =
            "Практична робота №6: класи, static, агрегація, enum";
    private static final String AUTHOR = "Мудрий Ярослав, група ІН-43-5";
   

    private static final Scanner SCANNER = new Scanner(System.in);
    private static Company company; 

   
    public static void main(String[] args) {
        printHeader();
        company = createCompany(); 

        boolean running = true;
        while (running) {
            printMenu();
            int choice = readInt("Ваш вибір: ");
            switch (choice) {
                case 1 -> createEmployee();
                case 2 -> printAll();
                case 3 -> copyEmployee();
					 case 4 -> showStatistics();
					 case 5 -> {
					 	running = false;
						System.out.println("Роботу завершено.");
					 }
					default -> System.out.println("Невірний пункт меню. Оберіть число від 1 до 5.");
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

    private static void printMenu() {
        System.out.println("\n=== МЕНЮ (" + company.getName() + ") ==="); 
        System.out.println("1. Створити нового працівника");
        System.out.println("2. Вивести всіх працівників");
        System.out.println("3. Створити копію працівника (конструктор копіювання)"); 
        System.out.println("4. Статистика компанії"); 
        System.out.println("5. Завершити роботу"); 
    }

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
            company.addEmployee(employee);
            System.out.println("Працівника додано.");
        } catch (IllegalArgumentException e) {
            System.out.println("Помилка: " + e.getMessage());
            System.out.println("Працівника не створено.");
        }
    }

    private static void printAll() {
        if (company.isEmpty()) {
            System.out.println("Список порожній.");
            return;
        }
        System.out.println("\n=== Працівники компанії " + company.getName() + " ===");
        for (int i = 0; i < company.getEmployeeCount(); i++) {
            System.out.println((i + 1) + ". " + company.getEmployee(i));
        }
    }

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