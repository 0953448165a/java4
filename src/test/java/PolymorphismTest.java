import org.junit.jupiter.api.Test;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class PolymorphismTest {

    private ArrayList<Employee> createList() {
        return new SampleData().employees();
    }

    @Test
    void baseClassShouldBeAbstractAndImplementComparable() {
        assertTrue(Modifier.isAbstract(Employee.class.getModifiers()));
        assertTrue(Comparable.class.isAssignableFrom(Employee.class));
    }

    @Test
    void listShouldHoldObjectsOfAllSubtypes() {
        assertEquals(4, createList().size());
    }

    @Test
    void shouldCallOverriddenMethodsThroughBaseReference() {
        ArrayList<Employee> list = createList();

        assertEquals("Штатний працівник", list.get(0).getEmployeeType());
        assertEquals("Працівник за контрактом", list.get(1).getEmployeeType());
        assertEquals("Керівник", list.get(2).getEmployeeType());
        assertEquals("Стажист", list.get(3).getEmployeeType());

        assertEquals(35000, list.get(0).calculateMonthlyPay());
        assertEquals(20000, list.get(1).calculateMonthlyPay());
        assertEquals(55000, list.get(2).calculateMonthlyPay());
        assertEquals(6000, list.get(3).calculateMonthlyPay());
    }

    @Test
    void shouldSortPolymorphicCollectionUsingComparable() {
        ArrayList<Employee> list = createList();
        Collections.sort(list);

        assertEquals("Бойко", list.get(0).getLastName());
        assertEquals("Коваль", list.get(1).getLastName());
        assertEquals("Мудрий", list.get(2).getLastName());
        assertEquals("Шевченко", list.get(3).getLastName());
    }

    @Test
    void employeesOfDifferentClassesShouldNotBeEqual() {
        Employee fullTime = new FullTimeEmployee("Марія", "Коваль", "Керівник відділу", 40000, 38, Department.IT, 10000, 28);
        Employee manager = new Manager("Марія", "Коваль", "Керівник відділу", 40000, 38, Department.IT, 10000, 28, 5, "Alpha");
        assertNotEquals(fullTime, manager);
    }
}