import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeTest {

    private Employee validEmployee() {
        return new Employee("Ярослав", "Мудрий", "Розробник", 35000, 25, Department.IT);
    }

    @Test
    void shouldCreateEmployeeWithValidData() {
        Employee e = validEmployee();
        assertEquals("Ярослав", e.getFirstName());
        assertEquals(25, e.getAge());
        assertEquals(Department.IT, e.getDepartment());
    }

    @Test
    void shouldThrowExceptionWhenInvalidValueInSetter() {
        Employee e = validEmployee();
        assertThrows(IllegalArgumentException.class, () -> e.setAge(-1));
        assertThrows(IllegalArgumentException.class, () -> e.setSalary(0));
        assertThrows(IllegalArgumentException.class, () -> e.setFirstName(""));
        assertThrows(IllegalArgumentException.class, () -> e.setDepartment(null));
    }

    @Test
    void shouldThrowExceptionWhenInvalidConstructorData() {
        assertThrows(IllegalArgumentException.class,
                () -> new Employee("", "Мудрий", "Розробник", 35000, 25, Department.IT));
        assertThrows(IllegalArgumentException.class,
                () -> new Employee("Ярослав", "Мудрий", "Розробник", -5, 25, Department.IT));
        assertThrows(IllegalArgumentException.class,
                () -> new Employee("Ярослав", "Мудрий", "Розробник", 35000, 10, Department.IT));
        assertThrows(IllegalArgumentException.class,
                () -> new Employee("Ярослав", "Мудрий", null, 35000, 25, Department.IT));
        assertThrows(IllegalArgumentException.class,
                () -> new Employee("Ярослав", "Мудрий", "Розробник", 35000, 25, null));
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

    @Test
    void copyConstructorShouldCreateEqualButSeparateObject() {
        Employee original = validEmployee();
        Employee copy = new Employee(original);

        assertEquals(original, copy);
        assertNotSame(original, copy);
    }

    @Test
    void changingCopyShouldNotChangeOriginal() {
        Employee original = validEmployee();
        Employee copy = new Employee(original);

        copy.setSalary(99999);
        copy.setDepartment(Department.HR);

        assertEquals(35000, original.getSalary());
        assertEquals(Department.IT, original.getDepartment());
    }

    @Test
    void copyConstructorShouldRejectNull() {
        assertThrows(IllegalArgumentException.class, () -> new Employee((Employee) null));
    }

    @Test
    void counterShouldIncreaseAfterEachCreatedObject() {
        int before = Employee.getCount();

        Employee e = validEmployee();
        assertEquals(before + 1, Employee.getCount());

        new Employee(e);
        assertEquals(before + 2, Employee.getCount());
    }

    @Test
    void counterShouldNotChangeWhenCreationFails() {
        int before = Employee.getCount();

        assertThrows(IllegalArgumentException.class,
                () -> new Employee("", "Мудрий", "Розробник", 35000, 25, Department.IT));
        assertThrows(IllegalArgumentException.class, () -> new Employee((Employee) null));

        assertEquals(before, Employee.getCount());
    }

}