import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тести класу {@link FullTimeEmployee}.
 */
class FullTimeEmployeeTest {

    private FullTimeEmployee validEmployee() {
        return new FullTimeEmployee("Ярослав", "Мудрий", "Розробник",
                30000, 25, Department.IT, 5000, 24);
    }

    @Test
    void shouldCreateFullTimeEmployeeWithValidData() {
        FullTimeEmployee e = validEmployee();
        assertEquals("Мудрий", e.getLastName());
        assertEquals(5000, e.getBonus());
        assertEquals(24, e.getVacationDays());
    }

    @Test
    void shouldBeInstanceOfEmployee() {
        assertInstanceOf(Employee.class, validEmployee());
    }

    @Test
    void shouldThrowExceptionWhenInvalidOwnFields() {
        assertThrows(IllegalArgumentException.class,
                () -> new FullTimeEmployee("Ярослав", "Мудрий", "Розробник", 30000, 25, Department.IT, -1, 24));
        assertThrows(IllegalArgumentException.class,
                () -> new FullTimeEmployee("Ярослав", "Мудрий", "Розробник", 30000, 25, Department.IT, 5000, -1));
        assertThrows(IllegalArgumentException.class,
                () -> new FullTimeEmployee("Ярослав", "Мудрий", "Розробник", 30000, 25, Department.IT, 5000, 61));
    }

    @Test
    void shouldValidateInheritedFieldsThroughBaseConstructor() {
        assertThrows(IllegalArgumentException.class,
                () -> new FullTimeEmployee("", "Мудрий", "Розробник", 30000, 25, Department.IT, 5000, 24));
        assertThrows(IllegalArgumentException.class,
                () -> new FullTimeEmployee("Ярослав", "Мудрий", "Розробник", 30000, 10, Department.IT, 5000, 24));
    }

    @Test
    void shouldKeepOldValueWhenSetterFails() {
        FullTimeEmployee e = validEmployee();
        assertThrows(IllegalArgumentException.class, () -> e.setBonus(-100));
        assertEquals(5000, e.getBonus());
    }

    @Test
    void monthlyPayShouldIncludeBonus() {
        assertEquals(35000, validEmployee().calculateMonthlyPay());
    }

    @Test
    void shouldReturnOwnEmployeeType() {
        assertEquals("Штатний працівник", validEmployee().getEmployeeType());
    }

    @Test
    void toStringShouldContainOwnFields() {
        String text = validEmployee().toString();
        assertTrue(text.startsWith("FullTimeEmployee{"));
        assertTrue(text.contains("bonus=5000.0"));
        assertTrue(text.contains("vacationDays=24"));
    }

    @Test
    void equalsShouldConsiderOwnFields() {
        FullTimeEmployee a = validEmployee();
        FullTimeEmployee b = validEmployee();
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());

        b.setBonus(1);
        assertNotEquals(a, b);
    }
}