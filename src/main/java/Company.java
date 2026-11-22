import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;
import java.util.UUID;

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

    public Company(String name) {
        this(name, "Суми", 2020);
    }

    public Company(String name, ArrayList<Employee> employees, ArrayList<Integer> quantities) {
        this(name);
        if (employees == null || quantities == null) {
            throw new IllegalArgumentException("Списки працівників та кількостей не можуть бути null.");
        }
        if (employees.size() != quantities.size()) {
            throw new IllegalArgumentException("Кількість записів працівників і кількостей має збігатися.");
        }
        for (int i = 0; i < employees.size(); i++) {
            addNewEmployee(employees.get(i), quantities.get(i));
        }
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
            throw new IllegalArgumentException("Місто не може бути порожнім.");
        }
        this.city = city.trim();
    }

    public int getFoundedYear() {
        return foundedYear;
    }

    public void setFoundedYear(int foundedYear) {
        if (foundedYear < 1800 || foundedYear > 2100) {
            throw new IllegalArgumentException("Рік заснування має бути в межах від 1800 до 2100.");
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
     * Повертає новий список усіх унікальних працівників компанії,
     * відсортований за природним порядком (Comparable).
     */
    public ArrayList<Employee> getSortedEmployees() {
        ArrayList<Employee> sorted = new ArrayList<Employee>(employees);
        Collections.sort(sorted);
        return sorted;
    }

    /**
     * Повертає новий список усіх унікальних працівників компанії,
     * відсортований за переданим компаратором (Comparator).
     */
    public ArrayList<Employee> getSortedEmployees(Comparator<Employee> comparator) {
        if (comparator == null) {
            throw new IllegalArgumentException("Компаратор не може бути null.");
        }
        ArrayList<Employee> sorted = new ArrayList<Employee>(employees);
        Collections.sort(sorted, comparator);
        return sorted;
    }

    public int getUniqueEmployeesCount() {
        return employees.size();
    }

    public int getTotalHeadcount() {
        int total = 0;
        for (int qty : quantities) {
            total += qty;
        }
        return total;
    }

    public int getTotalEmployeesCount() {
        return getTotalHeadcount();
    }

    public boolean isEmpty() {
        return employees.isEmpty();
    }

    public int getEmployeeQuantity(Employee employee) {
        if (employee == null) {
            return 0;
        }
        int index = employees.indexOf(employee);
        return index >= 0 ? quantities.get(index) : 0;
    }

    public void addNewEmployee(Employee employee, int quantity) {
        if (employee == null) {
            throw new IllegalArgumentException("Працівник не може бути null.");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Кількість працівників має бути більшою за 0.");
        }
        int index = employees.indexOf(employee);
        if (index >= 0) {
            quantities.set(index, quantities.get(index) + quantity);
        } else {
            employees.add(employee);
            quantities.add(quantity);
        }
    }

    public void addEmployee(Employee employee, int quantity) {
        addNewEmployee(employee, quantity);
    }

    public void addEmployee(Employee employee) {
        addNewEmployee(employee, 1);
    }

    public boolean removeEmployee(Employee employee, int quantity) {
        if (employee == null) {
            throw new IllegalArgumentException("Працівник не може бути null.");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Кількість для видалення має бути більшою за 0.");
        }
        int index = employees.indexOf(employee);
        if (index < 0) {
            return false;
        }
        int current = quantities.get(index);
        if (quantity > current) {
            throw new IllegalArgumentException("Неможливо видалити більше працівників, ніж є у компанії.");
        }
        if (quantity == current) {
            employees.remove(index);
            quantities.remove(index);
        } else {
            quantities.set(index, current - quantity);
        }
        return true;
    }

    public double calculateTotalMonthlyPayroll() {
        double total = 0.0;
        for (int i = 0; i < employees.size(); i++) {
            total += employees.get(i).calculateMonthlyPay() * quantities.get(i);
        }
        return total;
    }

    public ArrayList<Employee> searchByLastName(String query) {
        ArrayList<Employee> found = new ArrayList<Employee>();
        if (query == null || query.isBlank()) {
            return found;
        }
        String needle = query.trim().toLowerCase();
        for (Employee e : employees) {
            if (e.getLastName().toLowerCase().contains(needle)) {
                found.add(e);
            }
        }
        return found;
    }

    public ArrayList<Employee> searchByDepartment(Department department) {
        ArrayList<Employee> found = new ArrayList<Employee>();
        if (department == null) {
            return found;
        }
        for (Employee e : employees) {
            if (e.getDepartment() == department) {
                found.add(e);
            }
        }
        return found;
    }

    public ArrayList<Employee> searchBySalaryRange(double min, double max) {
        ArrayList<Employee> found = new ArrayList<Employee>();
        for (Employee e : employees) {
            if (e.getSalary() >= min && e.getSalary() <= max) {
                found.add(e);
            }
        }
        return found;
    }

    public ArrayList<Employee> searchByAgeRange(int min, int max) {
        ArrayList<Employee> found = new ArrayList<Employee>();
        for (Employee e : employees) {
            if (e.getAge() >= min && e.getAge() <= max) {
                found.add(e);
            }
        }
        return found;
    }

    public ArrayList<Employee> searchByType(String typeName) {
        ArrayList<Employee> found = new ArrayList<Employee>();
        if (typeName == null || typeName.isBlank()) {
            return found;
        }
        for (Employee e : employees) {
            if (e.getClass().getSimpleName().equals(typeName)) {
                found.add(e);
            }
        }
        return found;
    }
	 public Employee searchByUuid(UUID uuid) {
        if (uuid == null) {
            return null;
        }
        for (Employee e : employees) {
            if (uuid.equals(e.getUuid())) {
                return e;
            }
        }
        return null;
    }

    public Employee searchByUuid(String rawUuid) {
        if (rawUuid == null || rawUuid.isBlank()) {
            throw new IllegalArgumentException("UUID не може бути порожнім.");
        }
        try {
            UUID parsed = UUID.fromString(rawUuid.trim());
            return searchByUuid(parsed);
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Некоректний формат UUID: «" + rawUuid.trim() + "».");
        }
    }

    @Override
    public String toString() {
        return "Company{name='" + name + "', city='" + city + "', foundedYear=" + foundedYear
                + ", uniquePositions=" + getUniqueEmployeesCount()
                + ", totalHeadcount=" + getTotalHeadcount() + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Company company = (Company) o;
        return foundedYear == company.foundedYear
                && Objects.equals(name, company.name)
                && Objects.equals(city, company.city)
                && Objects.equals(employees, company.employees)
                && Objects.equals(quantities, company.quantities);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, city, foundedYear, employees, quantities);
    }
}