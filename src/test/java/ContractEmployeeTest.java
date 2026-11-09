import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тести класу {@link ContractEmployee}.
 */
class ContractEmployeeTest {

    private ContractEmployee validEmployee() {
        return new ContractEmployee("Олег", "Бойко", "Тестувальник",
                20000, 30, Department.SUPPORT, 6, "TechStaff");
    }

    @Test
    void shouldCreateContractEmployeeWithValidData() {
        ContractEmployee e = validEmployee();
        assertEquals("Бойко", e.getLastName());
        assertEquals(6, e.getContractMonths());
        assertEquals("TechStaff", e.getAgency());
    }

    @Test
    void shouldBeInstanceOfEmployee() {
        assertInstanceOf(Employee.class, validEmployee());
    }

    @Test
    void shouldThrowExceptionWhenInvalidOwnFields() {
        assertThrows(IllegalArgumentException.class,
                () -> new ContractEmployee("Олег", "Бойко", "Тестувальник", 20000, 30, Department.SUPPORT, 0, "TechStaff"));
        assertThrows(IllegalArgumentException.class,
                () -> new ContractEmployee("Олег", "Бойко", "Тестувальник", 20000, 30, Department.SUPPORT, 61, "TechStaff"));
        assertThrows(IllegalArgumentException.class,
                () -> new ContractEmployee("Олег", "Бойко", "Тестувальник", 20000, 30, Department.SUPPORT, 6, ""));
        assertThrows(IllegalArgumentException.class,
                () -> new ContractEmployee("Олег", "Бойко", "Тестувальник", 20000, 30, Department.SUPPORT, 6, null));
    }

    @Test
    void shouldValidateInheritedFieldsThroughBaseConstructor() {
        assertThrows(IllegalArgumentException.class,
                () -> new ContractEmployee("Олег", "Бойко", "Тестувальник", 0, 30, Department.SUPPORT, 6, "TechStaff"));
        assertThrows(IllegalArgumentException.class,
                () -> new ContractEmployee("Олег", "Бойко", "Тестувальник", 20000, 30, null, 6, "TechStaff"));
    }

    @Test
    void shouldCalculateTotalContractCost() {
        assertEquals(120000, validEmployee().getTotalContractCost());
    }

    @Test
    void shouldReturnOwnEmployeeType() {
        assertEquals("Працівник за контрактом", validEmployee().getEmployeeType());
    }

    @Test
    void toStringShouldContainOwnFields() {
        String text = validEmployee().toString();
        assertTrue(text.startsWith("ContractEmployee{"));
        assertTrue(text.contains("contractMonths=6"));
        assertTrue(text.contains("agency='TechStaff'"));
    }

    @Test
    void equalsShouldConsiderOwnFields() {
        ContractEmployee a = validEmployee();
        ContractEmployee b = validEmployee();
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());

        b.setContractMonths(12);
        assertNotEquals(a, b);
    }
}