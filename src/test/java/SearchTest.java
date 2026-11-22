import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class SearchTest {

    private final Company company = new SampleData().company();

    @Test
    void searchByLastNameShouldFindFullAndPartialIgnoringCase() {
        ArrayList<Employee> full = company.searchByLastName("коваль");
        assertEquals(1, full.size());
        assertEquals("Коваль", full.get(0).getLastName());

        assertEquals(1, company.searchByLastName("ВАЛ").size());
        assertEquals(1, company.searchByLastName("  Мудрий ").size());
    }

    @Test
    void searchByDepartmentShouldReturnAllFromDepartment() {
        assertEquals(3, company.searchByDepartment(Department.IT).size());
        assertEquals(1, company.searchByDepartment(Department.SUPPORT).size());
        assertTrue(company.searchByDepartment(Department.SALES).isEmpty());
    }

    @Test
    void searchBySalaryRangeShouldIncludeBounds() {
        ArrayList<Employee> found = company.searchBySalaryRange(20000, 30000);
        assertEquals(2, found.size());
    }

    @Test
    void searchByAgeRangeShouldIncludeBounds() {
        ArrayList<Employee> found = company.searchByAgeRange(25, 30);
        assertEquals(2, found.size());
    }

    @Test
    void searchByTypeShouldMatchExactClassOnly() {
        assertEquals(1, company.searchByType("Manager").size());
        assertEquals(1, company.searchByType("FullTimeEmployee").size());
        assertEquals(1, company.searchByType("ContractEmployee").size());
        assertEquals(1, company.searchByType("Intern").size());
        assertTrue(company.searchByType("Employee").isEmpty());
    }
}