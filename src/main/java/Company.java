import java.util.ArrayList;

public class Company {

    private String name;
    private String city;
    private int foundedYear;
    private final ArrayList<Employee> employees;
    private final ArrayList<Integer> quantities;

    public Company(String name, String city, int foundedYear) {
        setName(name);
        setCity(city);
        setFoundedYear(foundedYear);
        this.employees = new ArrayList<Employee>();
        this.quantities = new ArrayList<Integer>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Назва компанії не може бути порожньою.");
        }
        this.name = name.trim();
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        if (city == null || city.isBlank()) {
            throw new IllegalArgumentException("Місто компанії не може бути порожнім.");
        }
        this.city = city.trim();
    }

    public int getFoundedYear() {
        return foundedYear;
    }

    public void setFoundedYear(int foundedYear) {
        if (foundedYear < 1800 || foundedYear > 2026) {
            throw new IllegalArgumentException("Рік заснування компанії має бути від 1800 до 2026.");
        }
        this.foundedYear = foundedYear;
    }

    public ArrayList<Employee> getEmployees() {
        return new ArrayList<Employee>(employees);
    }

    public ArrayList<Integer> getQuantities() {
        return new ArrayList<Integer>(quantities);
    }

    /**
     * Додає працівника до колекції компанії.
     * Якщо такий об'єкт уже існує (перевірка через equals), збільшує його кількість.
     */
    public void addNewEmployee(Employee emp, int quantity) {
        if (emp == null) {
            throw new IllegalArgumentException("Працівник не може бути null.");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Кількість працівників має бути більшою за 0.");
        }

        for (int i = 0; i < employees.size(); i++) {
            Employee existing = employees.get(i);
            if (existing.equals(emp)) {
                Integer currentQty = quantities.get(i);
                Integer updatedQty = Integer.valueOf(currentQty.intValue() + quantity);
                quantities.set(i, updatedQty);
                return;
            }
        }

        employees.add(emp);
        quantities.add(Integer.valueOf(quantity));
    }

    /** Повертає кількість для конкретного працівника (або 0, якщо його немає в компанії). */
    public int getEmployeeQuantity(Employee emp) {
        if (emp == null) {
            return 0;
        }
        for (int i = 0; i < employees.size(); i++) {
            if (employees.get(i).equals(emp)) {
                return quantities.get(i).intValue();
            }
        }
        return 0;
    }

    /** Повертає загальну кількість усіх працівників у компанії з урахуванням quantity. */
    public int getTotalHeadcount() {
        int total = 0;
        for (int i = 0; i < quantities.size(); i++) {
            total += quantities.get(i).intValue();
        }
        return total;
    }

    /** Повертає сумарні місячні виплати всім працівникам з урахуванням їхньої кількості. */
    public double calculateTotalMonthlyPayroll() {
        double total = 0.0;
        for (int i = 0; i < employees.size(); i++) {
            Employee emp = employees.get(i);
            int qty = quantities.get(i).intValue();
            total += emp.calculateMonthlyPay() * qty;
        }
        return total;
    }

    // ===================== Методи пошуку з Практичної №10 =====================

    /** 1. Пошук працівників за прізвищем (без урахування регістру). */
    public ArrayList<Employee> searchByLastName(String query) {
        ArrayList<Employee> found = new ArrayList<Employee>();
        if (query == null || query.isBlank()) {
            return found;
        }
        String needle = query.trim().toLowerCase();
        for (int i = 0; i < employees.size(); i++) {
            Employee e = employees.get(i);
            if (e.getLastName().toLowerCase().contains(needle)) {
                found.add(e);
            }
        }
        return found;
    }

    /** 2. Пошук працівників за відділом. */
    public ArrayList<Employee> searchByDepartment(Department department) {
        ArrayList<Employee> found = new ArrayList<Employee>();
        if (department == null) {
            return found;
        }
        for (int i = 0; i < employees.size(); i++) {
            Employee e = employees.get(i);
            if (e.getDepartment() == department) {
                found.add(e);
            }
        }
        return found;
    }

    /** 3. Пошук працівників за діапазоном зарплати [min; max]. */
    public ArrayList<Employee> searchBySalaryRange(double min, double max) {
        ArrayList<Employee> found = new ArrayList<Employee>();
        if (min > max) {
            return found;
        }
        for (int i = 0; i < employees.size(); i++) {
            Employee e = employees.get(i);
            if (e.getSalary() >= min && e.getSalary() <= max) {
                found.add(e);
            }
        }
        return found;
    }

    /** 4. Пошук працівників за діапазоном віку [min; max]. */
    public ArrayList<Employee> searchByAgeRange(int min, int max) {
        ArrayList<Employee> found = new ArrayList<Employee>();
        if (min > max) {
            return found;
        }
        for (int i = 0; i < employees.size(); i++) {
            Employee e = employees.get(i);
            if (e.getAge() >= min && e.getAge() <= max) {
                found.add(e);
            }
        }
        return found;
    }

    /** 5. Пошук працівників за точною назвою класу. */
    public ArrayList<Employee> searchByType(String typeName) {
        ArrayList<Employee> found = new ArrayList<Employee>();
        if (typeName == null || typeName.isBlank()) {
            return found;
        }
        for (int i = 0; i < employees.size(); i++) {
            Employee e = employees.get(i);
            if (e.getClass().getSimpleName().equals(typeName)) {
                found.add(e);
            }
        }
        return found;
    }

    @Override
    public String toString() {
        return "Company{name='" + name + '\''
                + ", city='" + city + '\''
                + ", foundedYear=" + foundedYear
                + ", uniquePositions=" + employees.size()
                + ", totalHeadcount=" + getTotalHeadcount() + '}';
    }
}