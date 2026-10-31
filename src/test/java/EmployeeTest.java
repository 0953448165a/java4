import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тести валідації та винятків класу {@link Employee}.
 */
class EmployeeTest {

    private Employee validEmployee() {
        return new Employee("Ярослав", "Мудрий", "Розробник", 35000, 25, Department.IT); // ★ ЗМІНЕНО
    }

    @Test
    void shouldCreateEmployeeWithValidData() {
        Employee e = validEmployee();
        assertEquals("Ярослав", e.getFirstName());
        assertEquals(25, e.getAge());
        assertEquals(Department.IT, e.getDepartment()); // ★ ЗМІНЕНО
    }

    @Test
    void shouldThrowExceptionWhenInvalidValueInSetter() {
        Employee e = validEmployee();
        assertThrows(IllegalArgumentException.class, () -> e.setAge(-1));
        assertThrows(IllegalArgumentException.class, () -> e.setSalary(0));
        assertThrows(IllegalArgumentException.class, () -> e.setFirstName(""));
        assertThrows(IllegalArgumentException.class, () -> e.setDepartment(null)); // ★ НОВЕ
    }

    @Test
    void shouldThrowExceptionWhenInvalidConstructorData() {
        assertThrows(IllegalArgumentException.class,
                () -> new Employee("", "Мудрий", "Розробник", 35000, 25, Department.IT)); // ★ ЗМІНЕНО
        assertThrows(IllegalArgumentException.class,
                () -> new Employee("Ярослав", "Мудрий", "Розробник", -5, 25, Department.IT)); // ★ ЗМІНЕНО
        assertThrows(IllegalArgumentException.class,
                () -> new Employee("Ярослав", "Мудрий", "Розробник", 35000, 10, Department.IT)); // ★ ЗМІНЕНО
        assertThrows(IllegalArgumentException.class,
                () -> new Employee("Ярослав", "Мудрий", null, 35000, 25, Department.IT)); // ★ ЗМІНЕНО
        // ★ НОВЕ ▼
        assertThrows(IllegalArgumentException.class,
                () -> new Employee("Ярослав", "Мудрий", "Розробник", 35000, 25, null));
        // ★ НОВЕ ▲
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