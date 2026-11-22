import org.junit.jupiter.api.Test;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeTest {

    private static class TestEmployee extends Employee {
        TestEmployee(String firstName, String lastName, String position,
                     double salary, int age, Department department) {
            super(firstName, lastName, position, salary, age, department);
        }

        @Override
        public String getEmployeeType() {
            return "Тестовий працівник";
        }
    }

    private Employee validEmployee() {
        return new TestEmployee("Ярослав", "Мудрий", "Розробник", 35000, 25, Department.IT);
    }

    @Test
    void employeeClassShouldBeAbstractAndComparable() {
        assertTrue(Modifier.isAbstract(Employee.class.getModifiers()));
        assertTrue(Comparable.class.isAssignableFrom(Employee.class));
    }

    @Test
    void shouldCreateEmployeeSubclassWithValidData() {
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
                () -> new TestEmployee("", "Мудрий", "Розробник", 35000, 25, Department.IT));
        assertThrows(IllegalArgumentException.class,
                () -> new TestEmployee("Ярослав", "Мудрий", "Розробник", -5, 25, Department.IT));
        assertThrows(IllegalArgumentException.class,
                () -> new TestEmployee("Ярослав", "Мудрий", "Розробник", 35000, 10, Department.IT));
        assertThrows(IllegalArgumentException.class,
                () -> new TestEmployee("Ярослав", "Мудрий", null, 35000, 25, Department.IT));
        assertThrows(IllegalArgumentException.class,
                () -> new TestEmployee("Ярослав", "Мудрий", "Розробник", 35000, 25, null));
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
    void compareToShouldSortByLastNameAndFirstNameUnambiguously() {
        ArrayList<Employee> list = new ArrayList<Employee>();
        list.add(new TestEmployee("Ярослав", "Мудрий", "Розробник", 30000, 25, Department.IT));
        list.add(new TestEmployee("Олег", "Бойко", "Тестувальник", 20000, 30, Department.SUPPORT));
        list.add(new TestEmployee("Анна", "Коваль", "Аналітик", 28000, 27, Department.FINANCE));
        list.add(new TestEmployee("Марія", "коваль", "Менеджер", 35000, 34, Department.IT));

        Collections.sort(list);

        assertEquals("Бойко", list.get(0).getLastName());
        assertEquals("Коваль", list.get(1).getLastName());
        assertEquals("Анна", list.get(1).getFirstName());
        assertEquals("коваль", list.get(2).getLastName());
        assertEquals("Марія", list.get(2).getFirstName());
        assertEquals("Мудрий", list.get(3).getLastName());
    }

    @Test
    void compareToShouldRejectNull() {
        assertThrows(NullPointerException.class, () -> validEmployee().compareTo(null));
    }
}