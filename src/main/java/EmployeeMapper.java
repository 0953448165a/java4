import java.util.LinkedHashMap;
import java.util.Map;

public class EmployeeMapper {

    /** Перетворює об'єкт на впорядкований набір полів. Тип зберігається в полі "type". */
    public LinkedHashMap<String, Object> toFields(Employee e) {
        LinkedHashMap<String, Object> fields = new LinkedHashMap<String, Object>();
        fields.put("type", e.getClass().getSimpleName());
        fields.put("firstName", e.getFirstName());
        fields.put("lastName", e.getLastName());
        fields.put("position", e.getPosition());
        fields.put("salary", e.getSalary());
        fields.put("age", e.getAge());
        fields.put("department", e.getDepartment().name());

        // Manager перевіряється раніше за FullTimeEmployee, бо є його нащадком
        if (e instanceof Manager) {
            Manager m = (Manager) e;
            fields.put("bonus", m.getBonus());
            fields.put("vacationDays", m.getVacationDays());
            fields.put("teamSize", m.getTeamSize());
            fields.put("projectName", m.getProjectName());
        } else if (e instanceof FullTimeEmployee) {
            FullTimeEmployee f = (FullTimeEmployee) e;
            fields.put("bonus", f.getBonus());
            fields.put("vacationDays", f.getVacationDays());
        } else if (e instanceof ContractEmployee) {
            ContractEmployee c = (ContractEmployee) e;
            fields.put("contractMonths", c.getContractMonths());
            fields.put("agency", c.getAgency());
        } else if (e instanceof Intern) {
            Intern i = (Intern) e;
            fields.put("university", i.getUniversity());
            fields.put("internshipMonths", i.getInternshipMonths());
            fields.put("mentorName", i.getMentorName());
        }
        return fields;
    }

    /**
     * Створює об'єкт потрібного класу за полем "type".
     * @throws IllegalArgumentException якщо поле відсутнє, має неправильний формат
     *         або значення не пройшло перевірку в конструкторі
     */
    public Employee fromFields(Map<String, String> f) {
        String type = text(f, "type");
        String firstName = text(f, "firstName");
        String lastName = text(f, "lastName");
        String position = text(f, "position");
        double salary = decimal(f, "salary");
        int age = integer(f, "age");
        Department department = department(f, "department");

        if (type.equals("Employee")) {
            return new Employee(firstName, lastName, position, salary, age, department);
        } else if (type.equals("FullTimeEmployee")) {
            return new FullTimeEmployee(firstName, lastName, position, salary, age, department,
                    decimal(f, "bonus"), integer(f, "vacationDays"));
        } else if (type.equals("ContractEmployee")) {
            return new ContractEmployee(firstName, lastName, position, salary, age, department,
                    integer(f, "contractMonths"), text(f, "agency"));
        } else if (type.equals("Manager")) {
            return new Manager(firstName, lastName, position, salary, age, department,
                    decimal(f, "bonus"), integer(f, "vacationDays"),
                    integer(f, "teamSize"), text(f, "projectName"));
        } else if (type.equals("Intern")) {
            return new Intern(firstName, lastName, position, salary, age, department,
                    text(f, "university"), integer(f, "internshipMonths"), text(f, "mentorName"));
        }
        throw new IllegalArgumentException("невідомий тип об'єкта: " + type);
    }

    private String text(Map<String, String> f, String key) {
        String value = f.get(key);
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("відсутнє поле «" + key + "»");
        }
        return value.trim();
    }

    private int integer(Map<String, String> f, String key) {
        String value = text(f, key);
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("поле «" + key + "» має бути цілим числом, а не «" + value + "»");
        }
    }

    private double decimal(Map<String, String> f, String key) {
        String value = text(f, key);
        double result;
        try {
            result = Double.parseDouble(value);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("поле «" + key + "» має бути числом, а не «" + value + "»");
        }
        if (Double.isNaN(result) || Double.isInfinite(result)) {
            throw new IllegalArgumentException("поле «" + key + "» має бути скінченним числом");
        }
        return result;
    }

    private Department department(Map<String, String> f, String key) {
        String value = text(f, key);
        try {
            return Department.valueOf(value);
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("невідомий відділ «" + value + "»");
        }
    }
}