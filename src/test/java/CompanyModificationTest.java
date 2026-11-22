import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CompanyModificationTest {

    @Test
    void shouldUpdateExistingEmployeeAndReturnTrue() {
        Company company = new SampleData().company();
        Employee existing = company.getEmployees().get(0); // Ярослав Мудрий
        UUID originalUuid = existing.getUuid();
        int originalQty = company.getEmployeeQuantity(existing);

        Employee updated = new FullTimeEmployee("Ярослав", "Мудрий", "Senior Java Developer",
                45000.0, 26, Department.IT, 8000.0, 24);

        boolean result = company.update(existing, updated);

        assertTrue(result);
        assertEquals(4, company.getUniqueEmployeesCount());
        assertEquals(originalQty, company.getEmployeeQuantity(updated));
        assertEquals("Senior Java Developer", company.getEmployees().get(0).getPosition());
        assertEquals(45000.0, company.getEmployees().get(0).getSalary());
        assertEquals(originalUuid, company.getEmployees().get(0).getUuid());
    }

    @Test
    void shouldReturnFalseWhenUpdatingNonExistentOrNullEmployee() {
        Company company = new SampleData().company();
        Employee stranger = new Intern("Петро", "Іваненко", "Стажист",
                10000.0, 21, Department.HR, "СумДУ", 3, "Марія Коваль");
        Employee replacement = new Intern("Петро", "Іваненко", "Молодший спеціаліст",
                15000.0, 21, Department.HR, "СумДУ", 6, "Марія Коваль");

        assertFalse(company.update(stranger, replacement));
        assertFalse(company.update(null, replacement));
        assertThrows(IllegalArgumentException.class, () -> company.update(company.getEmployees().get(0), null));
    }

    @Test
    void shouldDeleteExistingEmployeeAndReturnTrue() {
        Company company = new SampleData().company();
        int initialUnique = company.getUniqueEmployeesCount();
        int initialHeadcount = company.getTotalHeadcount();

        Employee toDelete = company.getEmployees().get(1); // Олег Бойко (кількість: 1)
        boolean result = company.delete(toDelete);

        assertTrue(result);
        assertEquals(initialUnique - 1, company.getUniqueEmployeesCount());
        assertEquals(initialHeadcount - 1, company.getTotalHeadcount());
        assertEquals(0, company.getEmployeeQuantity(toDelete));
        assertNull(company.searchByUuid(toDelete.getUuid()));
    }

@Test
    void shouldReturnFalseWhenDeletingFromEmptyOrMissingObject() {
        Company empty = new Company("EmptyCorp");
        Employee sample = new FullTimeEmployee("Ярослав", "Мудрий", "Розробник",
                30000.0, 25, Department.IT, 5000.0, 24);

        assertFalse(empty.delete(sample));
        assertFalse(empty.delete(null));

        Company full = new SampleData().company();
        Employee missing = new FullTimeEmployee("Петро", "Іваненко", "Аналітик",
                28000.0, 29, Department.FINANCE, 4000.0, 24);
        assertFalse(full.delete(missing));
    }
}