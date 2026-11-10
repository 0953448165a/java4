import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ManagerTest {

    private Manager validManager() {
        return new Manager("Марія", "Коваль", "Керівник відділу",
                40000, 38, Department.IT, 10000, 28, 5, "Alpha");
    }

    @Test
    void shouldCreateManagerWithValidData() {
        Manager m = validManager();
        assertEquals("Коваль", m.getLastName());
        assertEquals(10000, m.getBonus());
        assertEquals(5, m.getTeamSize());
        assertEquals("Alpha", m.getProjectName());
    }

    @Test
    void shouldBeInstanceOfFullTimeEmployeeAndEmployee() {
        assertInstanceOf(FullTimeEmployee.class, validManager());
        assertInstanceOf(Employee.class, validManager());
    }

    @Test
    void shouldThrowExceptionWhenInvalidOwnFields() {
        assertThrows(IllegalArgumentException.class,
                () -> new Manager("Марія", "Коваль", "Керівник", 40000, 38, Department.IT, 10000, 28, 0, "Alpha"));
        assertThrows(IllegalArgumentException.class,
                () -> new Manager("Марія", "Коваль", "Керівник", 40000, 38, Department.IT, 10000, 28, 101, "Alpha"));
        assertThrows(IllegalArgumentException.class,
                () -> new Manager("Марія", "Коваль", "Керівник", 40000, 38, Department.IT, 10000, 28, 5, " "));
        assertThrows(IllegalArgumentException.class,
                () -> new Manager("Марія", "Коваль", "Керівник", 40000, 38, Department.IT, 10000, 28, 5, null));
    }

    @Test
    void shouldValidateInheritedFields() {
        assertThrows(IllegalArgumentException.class,
                () -> new Manager("", "Коваль", "Керівник", 40000, 38, Department.IT, 10000, 28, 5, "Alpha"));
        assertThrows(IllegalArgumentException.class,
                () -> new Manager("Марія", "Коваль", "Керівник", 40000, 38, Department.IT, -1, 28, 5, "Alpha"));
        assertThrows(IllegalArgumentException.class,
                () -> new Manager("Марія", "Коваль", "Керівник", 40000, 38, Department.IT, 10000, 61, 5, "Alpha"));
    }

    @Test
    void monthlyPayShouldIncludeBonusAndManagementAllowance() {
        // 40000 + 10000 + 5 * 1000
        assertEquals(55000, validManager().calculateMonthlyPay());
        assertEquals(5000, validManager().getManagementAllowance());
    }

    @Test
    void shouldReturnOwnEmployeeType() {
        assertEquals("Керівник", validManager().getEmployeeType());
    }

    @Test
    void toStringShouldContainOwnFields() {
        String text = validManager().toString();
        assertTrue(text.startsWith("Manager{"));
        assertTrue(text.contains("teamSize=5"));
        assertTrue(text.contains("projectName='Alpha'"));
    }

    @Test
    void equalsShouldConsiderOwnFields() {
        Manager a = validManager();
        Manager b = validManager();
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());

        b.setTeamSize(6);
        assertNotEquals(a, b);
    }

    @Test
    void managerAndFullTimeEmployeeShouldNotBeEqual() {
        FullTimeEmployee f = new FullTimeEmployee("Марія", "Коваль", "Керівник відділу",
                40000, 38, Department.IT, 10000, 28);
        assertNotEquals(f, validManager());
    }
}