import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int n = readPositiveInt(scanner, "Введіть кількість працівників: ");
        ArrayList<Employee> employees = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            System.out.println("\nПрацівник №" + (i + 1));

            String firstName = readNonEmptyString(scanner, "Ім'я: ");
            String lastName = readNonEmptyString(scanner, "Прізвище: ");
            String position = readNonEmptyString(scanner, "Посада: ");
            double salary = readPositiveDouble(scanner, "Зарплата: ");
            int age = readPositiveInt(scanner, "Вік: ");
            String department = readNonEmptyString(scanner, "Відділ: ");

            employees.add(new Employee(firstName, lastName, position, salary, age, department));
        }

        System.out.println("\n=== Список працівників ===");
        for (Employee e : employees) {
            System.out.println(e);
        }

        if (employees.size() >= 2) {
            System.out.println("\nПерший == другий? "
                    + employees.get(0).equals(employees.get(1)));
        }

        scanner.close();
    }

    private static String readNonEmptyString(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim();
            if (!line.isEmpty()) {
                return line;
            }
            System.out.println("Поле не може бути порожнім.");
        }
    }

    private static int readPositiveInt(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim();
            try {
                int value = Integer.parseInt(line);
                if (value > 0) {
                    return value;
                }
                System.out.println("Число має бути більше 0.");
            } catch (NumberFormatException e) {
                System.out.println("Некоректне ціле число, спробуйте ще раз.");
            }
        }
    }

    private static double readPositiveDouble(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim().replace(',', '.');
            try {
                double value = Double.parseDouble(line);
                if (value > 0) {
                    return value;
                }
                System.out.println("Значення має бути більше 0.");
            } catch (NumberFormatException e) {
                System.out.println("Некоректне число, спробуйте ще раз.");
            }
        }
    }
}