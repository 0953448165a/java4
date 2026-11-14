import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class SearchTest {

    // Іван (SALES, 10000, 40), Ярослав (IT, 30000, 25), Олег (SUPPORT, 20000, 30),
    // Марія (IT, 40000, 38, Manager), Андрій (IT, 12000, 20, Intern)
    private final ArrayList<Employee> source = new SampleData().employees();

    @Test
    void searchByLastNameShouldFindFullAndPartialIgnoringCase() {
        ArrayList<Employee> full = Main.searchByLastName(source, "коваль");
        assertEquals(1, full.size());
        assertEquals("Коваль", full.get(0).getLastName());

        assertEquals(1, Main.searchByLastName(source, "ВАЛ").size());
        assertEquals(1, Main.searchByLastName(source, "  Мудрий ").size());
    }

    @Test
    void searchByLastNameShouldReturnEmptyWhenNothingMatches() {
        assertTrue(Main.searchByLastName(source, "Xyz").isEmpty());
    }

    @Test
    void searchByDepartmentShouldReturnAllFromDepartment() {
        assertEquals(3, Main.searchByDepartment(source, Department.IT).size());
        assertEquals(1, Main.searchByDepartment(source, Department.SALES).size());
        assertEquals(1, Main.searchByDepartment(source, Department.SUPPORT).size());
    }

    @Test
    void searchBySalaryRangeShouldIncludeBounds() {
        ArrayList<Employee> found = Main.searchBySalaryRange(source, 20000, 30000);
        assertEquals(2, found.size());
        assertEquals("Мудрий", found.get(0).getLastName());
        assertEquals("Бойко", found.get(1).getLastName());

        assertTrue(Main.searchBySalaryRange(source, 50000, 60000).isEmpty());
    }

    @Test
    void searchByAgeRangeShouldIncludeBounds() {
        ArrayList<Employee> found = Main.searchByAgeRange(source, 25, 30);
        assertEquals(2, found.size());

        assertTrue(Main.searchByAgeRange(source, 60, 70).isEmpty());
    }

    @Test
    void searchByTypeShouldMatchExactClassOnly() {
        assertEquals(1, Main.searchByType(source, "Manager").size());
        assertEquals(1, Main.searchByType(source, "FullTimeEmployee").size()); // Manager не враховується
        assertEquals(1, Main.searchByType(source, "Employee").size());         // нащадки не враховуються
        assertEquals(1, Main.searchByType(source, "Intern").size());
        assertTrue(Main.searchByType(source, "Robot").isEmpty());
    }

    @Test
    void searchShouldNotModifyCollection() {
        ArrayList<Employee> before = new ArrayList<>(source);

        Main.searchByLastName(source, "ко");
        Main.searchByDepartment(source, Department.IT);
        Main.searchBySalaryRange(source, 0, 100000);
        Main.searchByAgeRange(source, 18, 70);
        Main.searchByType(source, "Manager");

        assertEquals(before.size(), source.size());
        for (int i = 0; i < before.size(); i++) {
            assertSame(before.get(i), source.get(i)); // той самий порядок і ті самі об'єкти
        }
    }

    @Test
    void searchShouldReturnSameObjectsNotCopies() {
        ArrayList<Employee> found = Main.searchByType(source, "Manager");
        assertSame(source.get(3), found.get(0));
    }

    @Test
    void searchInEmptyCollectionShouldReturnEmptyLists() {
        ArrayList<Employee> empty = new ArrayList<>();
        assertTrue(Main.searchByLastName(empty, "a").isEmpty());
        assertTrue(Main.searchByDepartment(empty, Department.IT).isEmpty());
        assertTrue(Main.searchBySalaryRange(empty, 0, 1000).isEmpty());
        assertTrue(Main.searchByAgeRange(empty, 0, 100).isEmpty());
        assertTrue(Main.searchByType(empty, "Manager").isEmpty());
    }
}