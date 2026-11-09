import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CompanyTest {

    private Employee employee(String lastName, double salary, Department department) {
        return new Employee("Іван", lastName, "Спеціаліст", salary, 30, department);
    }

    @Test
    void shouldThrowExceptionWhenCompanyNameIsBlank() {
        assertThrows(IllegalArgumentException.class, () -> new Company(""));
        assertThrows(IllegalArgumentException.class, () -> new Company(null));
    }

    @Test
    void shouldAddEmployeeAndCountThem() {
        Company company = new Company("Тест");
        assertTrue(company.isEmpty());

        company.addEmployee(employee("Мудрий", 10000, Department.IT));
        company.addEmployee(employee("Бойко", 20000, Department.HR));

        assertEquals(2, company.getEmployeeCount());
        assertFalse(company.isEmpty());
    }

    @Test
    void shouldThrowExceptionWhenAddingNullEmployee() {
        Company company = new Company("Тест");
        assertThrows(IllegalArgumentException.class, () -> company.addEmployee(null));
    }

    @Test
    void shouldThrowExceptionWhenIndexIsOutOfRange() {
        Company company = new Company("Тест");
        company.addEmployee(employee("Мудрий", 10000, Department.IT));

        assertThrows(IllegalArgumentException.class, () -> company.getEmployee(1));
        assertThrows(IllegalArgumentException.class, () -> company.getEmployee(-1));
    }

    @Test
    void shouldCalculateTotalAndAverageSalary() {
        Company company = new Company("Тест");
        company.addEmployee(employee("Мудрий", 10000, Department.IT));
        company.addEmployee(employee("Бойко", 20000, Department.HR));

        assertEquals(30000, company.getTotalSalary());
        assertEquals(15000, company.getAverageSalary());
    }

    @Test
    void averageSalaryOfEmptyCompanyShouldBeZero() {
        assertEquals(0, new Company("Тест").getAverageSalary());
    }

    @Test
    void shouldCountEmployeesByDepartment() {
        Company company = new Company("Тест");
        company.addEmployee(employee("Мудрий", 10000, Department.IT));
        company.addEmployee(employee("Бойко", 20000, Department.IT));
        company.addEmployee(employee("Горох", 15000, Department.HR));

        assertEquals(2, company.countByDepartment(Department.IT));
        assertEquals(1, company.countByDepartment(Department.HR));
        assertEquals(0, company.countByDepartment(Department.SALES));
        assertThrows(IllegalArgumentException.class, () -> company.countByDepartment(null));
    }

    @Test
    void returnedListShouldBeACopy() {
        Company company = new Company("Тест");
        company.addEmployee(employee("Мудрий", 10000, Department.IT));

        ArrayList<Employee> list = company.getEmployees();
        list.clear();

        assertEquals(1, company.getEmployeeCount());
    }
}