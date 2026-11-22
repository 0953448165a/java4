import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CompanyTest {

    @Test
    void shouldCreateEmptyCompany() {
        Company c = new Company("TechNova Ukraine");
        assertEquals("TechNova Ukraine", c.getName());
        assertTrue(c.isEmpty());
        assertEquals(0, c.getUniqueEmployeesCount());
        assertEquals(0, c.getTotalEmployeesCount());
    }

    @Test
    void shouldAggregateIdenticalEmployeesAndCalculatePayroll() {
        Company c = new SampleData().company();
        assertEquals(4, c.getUniqueEmployeesCount());
        assertEquals(7, c.getTotalEmployeesCount());
        // 2 * 35000 + 1 * 20000 + 1 * 55000 + 3 * 6000 = 163000
        assertEquals(163000.0, c.calculateTotalMonthlyPayroll(), 0.001);
    }

    @Test
    void getSortedEmployeesShouldHandleEmptySingleAndMultipleElements() {
        Company empty = new Company("EmptyCorp");
        assertTrue(empty.getSortedEmployees().isEmpty());

        Company single = new Company("SingleCorp");
        Employee one = new FullTimeEmployee("Ярослав", "Мудрий", "Розробник", 30000, 25, Department.IT, 5000, 24);
        single.addEmployee(one);
        ArrayList<Employee> singleSorted = single.getSortedEmployees();
        assertEquals(1, singleSorted.size());
        assertEquals(one, singleSorted.get(0));

        Company full = new SampleData().company();
        ArrayList<Employee> sorted = full.getSortedEmployees();
        assertEquals(4, sorted.size());
        assertEquals("Бойко", sorted.get(0).getLastName());
        assertEquals("Коваль", sorted.get(1).getLastName());
        assertEquals("Мудрий", sorted.get(2).getLastName());
        assertEquals("Шевченко", sorted.get(3).getLastName());

        // Внутрішній порядок у компанії залишається незмінним
        assertEquals("Мудрий", full.getEmployees().get(0).getLastName());
    }
}