import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тести валідації та винятків класу {@link Employee}.
 */
class EmployeeTest {

    private Employee validEmployee() {
        return new Employee("Ярослав", "Мудрий", "Розробник", 35000, 25, "IT");
    }

    @Test
    void shouldCreateEmployeeWithValidData() {
        Employee e = validEmployee();
        assertEquals("Ярослав", e.getFirstName());
        assertEquals(25, e.getAge());
        assertEquals("IT", e.getDepartment());
    }

    @Test
    void shouldThrowExceptionWhenInvalidValueInSetter() {
        Employee e = validEmployee();
        assertThrows(IllegalArgumentException.class, () -> e.setAge(-1));
        assertThrows(IllegalArgumentException.class, () -> e.setSalary(0));
        assertThrows(IllegalArgumentException.class, () -> e.setFirstName(""));
    }

    @Test
    void shouldThrowExceptionWhenInvalidConstructorData() {
        assertThrows(IllegalArgumentException.class,
                () -> new Employee("", "Мудрий", "Розробник", 35000, 25, "IT"));
        assertThrows(IllegalArgumentException.class,
                () -> new Employee("Ярослав", "Мудрий", "Розробник", -5, 25, "IT"));
        assertThrows(IllegalArgumentException.class,
                () -> new Employee("Ярослав", "Мудрий", "Розробник", 35000, 10, "IT"));
        assertThrows(IllegalArgumentException.class,
                () -> new Employee("Ярослав", "Мудрий", null, 35000, 25, "IT"));
    }

    @Test
    void shouldKeepOldValueWhenSetterFails() {
        Employee e = validEmployee();
        assertThrows(IllegalArgumentException.class, () -> e.setAge(200));
        assertEquals(25, e.getAge());
    }

    @Test
    void shouldAcceptBoundaryAges() {
        Employee e = validEmployee();
        assertDoesNotThrow(() -> e.setAge(Employee.MIN_AGE));
        assertDoesNotThrow(() -> e.setAge(Employee.MAX_AGE));
    }

    @Test
    void equalEmployeesShouldBeEqual() {
        assertEquals(validEmployee(), validEmployee());
    }
}