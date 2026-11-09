import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class PolymorphismTest {

    private ArrayList<Employee> createList() {
        ArrayList<Employee> list = new ArrayList<>();
        list.add(new Employee("Іван", "Петренко", "Менеджер", 10000, 40, Department.SALES));
        list.add(new FullTimeEmployee("Ярослав", "Мудрий", "Розробник", 30000, 25, Department.IT, 5000, 24));
        list.add(new ContractEmployee("Олег", "Бойко", "Тестувальник", 20000, 30, Department.SUPPORT, 6, "TechStaff"));
        return list;
    }

    @Test
    void listShouldHoldObjectsOfAllTypes() {
        assertEquals(3, createList().size());
    }

    @Test
    void shouldCallOverriddenMethodsThroughBaseReference() {
        ArrayList<Employee> list = createList();

        assertEquals("Працівник", list.get(0).getEmployeeType());
        assertEquals("Штатний працівник", list.get(1).getEmployeeType());
        assertEquals("Працівник за контрактом", list.get(2).getEmployeeType());

        assertEquals(10000, list.get(0).calculateMonthlyPay());
        assertEquals(35000, list.get(1).calculateMonthlyPay());
        assertEquals(20000, list.get(2).calculateMonthlyPay());
    }

    @Test
    void toStringShouldDependOnRealClass() {
        ArrayList<Employee> list = createList();

        assertTrue(list.get(0).toString().startsWith("Employee{"));
        assertTrue(list.get(1).toString().startsWith("FullTimeEmployee{"));
        assertTrue(list.get(2).toString().startsWith("ContractEmployee{"));
    }

    @Test
    void shouldCalculateTotalPayOverMixedList() {
        double total = 0;
        for (Employee e : createList()) {
            total += e.calculateMonthlyPay();
        }
        assertEquals(65000, total);
    }

    @Test
    void employeesOfDifferentClassesShouldNotBeEqual() {
        Employee base = new Employee("Ярослав", "Мудрий", "Розробник", 30000, 25, Department.IT);
        Employee derived = new FullTimeEmployee("Ярослав", "Мудрий", "Розробник", 30000, 25, Department.IT, 0, 0);
        assertNotEquals(base, derived);
    }
}