import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.UUID;

public class Company {

    private String name;
    private String city;
    private int foundedYear;

    private final ArrayList<Employee> employees;
    private final ArrayList<Integer> quantities;

    public Company(String name) {
        this(name, "Не вказано", 2000);
    }

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
            throw new InvalidFieldValueException("Назва компанії не може бути порожньою.");
        }
        this.name = name.trim();
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        if (city == null || city.isBlank()) {
            throw new InvalidFieldValueException("Місто компанії не може бути порожнім.");
        }
        this.city = city.trim();
    }

    public int getFoundedYear() {
        return foundedYear;
    }

    public void setFoundedYear(int foundedYear) {
        if (foundedYear < 1800 || foundedYear > 2100) {
            throw new InvalidFieldValueException("Рік заснування має бути від 1800 до 2100.");
        }
        this.foundedYear = foundedYear;
    }

    public void addEmployee(Employee employee, int quantity) {
        if (employee == null) {
            throw new InvalidFieldValueException("Працівник не може бути null.");
        }
        if (quantity <= 0) {
            throw new InvalidFieldValueException("Кількість працівників має бути більшою за 0.");
        }

        int index = employees.indexOf(employee);
        if (index >= 0) {
            quantities.set(index, quantities.get(index) + quantity);
        } else {
            employees.add(employee);
            quantities.add(quantity);
        }
    }

    public void addNewEmployee(Employee employee, int quantity) {
        if (employee == null) {
            throw new InvalidFieldValueException("Працівник не може бути null.");
        }
        if (quantity <= 0) {
            throw new InvalidFieldValueException("Кількість працівників має бути більшою за 0.");
        }
        if (employees.contains(employee)) {
            throw new DuplicateObjectException("Працівник «" + employee.getFirstName() + " "
                    + employee.getLastName() + "» уже існує у штаті компанії.");
        }
        employees.add(employee);
        quantities.add(quantity);
    }

    public boolean removeEmployee(Employee employee, int quantity) {
        if (employee == null || quantity <= 0) {
            return false;
        }
        int index = employees.indexOf(employee);
        if (index < 0) {
            return false;
        }
        int current = quantities.get(index);
        if (quantity >= current) {
            employees.remove(index);
            quantities.remove(index);
        } else {
            quantities.set(index, current - quantity);
        }
        return true;
    }

    public boolean update(Employee existingObject, Employee newObject) {
        if (newObject == null) {
            throw new InvalidFieldValueException("Новий об'єкт працівника не може бути null.");
        }
        if (existingObject == null) {
            throw new ObjectNotFoundException("Об'єкт для модифікації не може бути null.");
        }

        int index = findEmployeeIndex(existingObject);
        if (index < 0) {
            throw new ObjectNotFoundException("Працівника «" + existingObject.getFirstName() + " "
                    + existingObject.getLastName() + "» не знайдено у колекції компанії.");
        }

        if (existingObject.getUuid() != null) {
            newObject.setUuid(existingObject.getUuid());
        }

        int duplicateIndex = employees.indexOf(newObject);
        if (duplicateIndex >= 0 && duplicateIndex != index) {
            int mergedQuantity = quantities.get(duplicateIndex) + quantities.get(index);
            quantities.set(duplicateIndex, mergedQuantity);
            employees.remove(index);
            quantities.remove(index);
            return true;
        }

        employees.set(index, newObject);
        return true;
    }

    public boolean delete(Employee existingObject) {
        if (existingObject == null) {
            throw new ObjectNotFoundException("Об'єкт для видалення не може бути null.");
        }

        int index = findEmployeeIndex(existingObject);
        if (index < 0) {
            throw new ObjectNotFoundException("Працівника «" + existingObject.getFirstName() + " "
                    + existingObject.getLastName() + "» не знайдено у колекції компанії.");
        }

        employees.remove(index);
        quantities.remove(index);
        return true;
    }

    private int findEmployeeIndex(Employee target) {
        int index = employees.indexOf(target);
        if (index < 0 && target.getUuid() != null) {
            for (int i = 0; i < employees.size(); i++) {
                if (target.getUuid().equals(employees.get(i).getUuid())) {
                    return i;
                }
            }
        }
        return index;
    }

    public ArrayList<Employee> getEmployees() {
        return new ArrayList<Employee>(employees);
    }

    public ArrayList<Integer> getQuantities() {
        return new ArrayList<Integer>(quantities);
    }

    public int getEmployeeQuantity(Employee employee) {
        if (employee == null) {
            return 0;
        }
        int index = employees.indexOf(employee);
        return index >= 0 ? quantities.get(index) : 0;
    }

    public int getUniqueEmployeesCount() {
        return employees.size();
    }

    public int getTotalHeadcount() {
        int total = 0;
        for (int i = 0; i < quantities.size(); i++) {
            total += quantities.get(i);
        }
        return total;
    }

    public boolean isEmpty() {
        return employees.isEmpty();
    }

    public double calculateTotalMonthlyPayroll() {
        double total = 0.0;
        for (int i = 0; i < employees.size(); i++) {
            total += employees.get(i).calculateMonthlyPay() * quantities.get(i);
        }
        return total;
    }

    public ArrayList<Employee> getSortedEmployees(Comparator<Employee> comparator) {
        if (comparator == null) {
            throw new InvalidFieldValueException("Компаратор не може бути null.");
        }
        ArrayList<Employee> sorted = new ArrayList<Employee>(employees);
        Collections.sort(sorted, comparator);
        return sorted;
    }

    public ArrayList<Employee> searchByLastName(String query) {
        ArrayList<Employee> result = new ArrayList<Employee>();
        if (query == null || query.isBlank()) {
            return result;
        }
        String lower = query.trim().toLowerCase();
        for (int i = 0; i < employees.size(); i++) {
            Employee e = employees.get(i);
            if (e.getLastName().toLowerCase().contains(lower)) {
                result.add(e);
            }
        }
        return result;
    }

    public ArrayList<Employee> searchByDepartment(Department department) {
        ArrayList<Employee> result = new ArrayList<Employee>();
        if (department == null) {
            return result;
        }
        for (int i = 0; i < employees.size(); i++) {
            Employee e = employees.get(i);
            if (e.getDepartment() == department) {
                result.add(e);
            }
        }
        return result;
    }

    public ArrayList<Employee> searchBySalaryRange(double min, double max) {
        ArrayList<Employee> result = new ArrayList<Employee>();
        for (int i = 0; i < employees.size(); i++) {
            Employee e = employees.get(i);
            if (e.getSalary() >= min && e.getSalary() <= max) {
                result.add(e);
            }
        }
        return result;
    }

    public ArrayList<Employee> searchByAgeRange(int min, int max) {
        ArrayList<Employee> result = new ArrayList<Employee>();
        for (int i = 0; i < employees.size(); i++) {
            Employee e = employees.get(i);
            if (e.getAge() >= min && e.getAge() <= max) {
                result.add(e);
            }
        }
        return result;
    }

    public ArrayList<Employee> searchByType(String typeName) {
        ArrayList<Employee> result = new ArrayList<Employee>();
        if (typeName == null || typeName.isBlank()) {
            return result;
        }
        String trimmed = typeName.trim();
        for (int i = 0; i < employees.size(); i++) {
            Employee e = employees.get(i);
            if (e.getClass().getSimpleName().equals(trimmed)) {
                result.add(e);
            }
        }
        return result;
    }

    public Employee searchByUuid(UUID uuid) {
        if (uuid == null) {
            return null;
        }
        for (int i = 0; i < employees.size(); i++) {
            Employee e = employees.get(i);
            if (uuid.equals(e.getUuid())) {
                return e;
            }
        }
        return null;
    }

    public Employee searchByUuid(String rawUuid) {
        if (rawUuid == null || rawUuid.isBlank()) {
            throw new InvalidFieldValueException("UUID не може бути порожнім.");
        }
        try {
            UUID parsed = UUID.fromString(rawUuid.trim());
            return searchByUuid(parsed);
        } catch (IllegalArgumentException ex) {
            throw new InvalidFieldValueException("Некоректний формат UUID: «" + rawUuid.trim() + "».");
        }
    }

    @Override
    public String toString() {
        return "Company{name='" + name + "', city='" + city + "', foundedYear=" + foundedYear
                + ", uniquePositions=" + getUniqueEmployeesCount()
                + ", totalHeadcount=" + getTotalHeadcount() + "}";
    }
}