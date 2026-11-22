import org.junit.jupiter.api.Test;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class ComparatorSortingTest {

    @Test
    void shouldSortBySalaryAscendingUsingLambda() {
        Company company = new SampleData().company();

        Comparator<Employee> bySalaryAsc = (o1, o2) -> {
            int cmp = Double.compare(o1.getSalary(), o2.getSalary());
            return cmp != 0 ? cmp : o1.compareTo(o2);
        };

        ArrayList<Employee> sorted = company.getSortedEmployees(bySalaryAsc);
        assertEquals(4, sorted.size());
        assertEquals("Шевченко", sorted.get(0).getLastName()); // 12000
        assertEquals("Бойко", sorted.get(1).getLastName());    // 20000
        assertEquals("Мудрий", sorted.get(2).getLastName());   // 30000
        assertEquals("Коваль", sorted.get(3).getLastName());   // 40000
    }

    @Test
    void shouldSortByMonthlyPayDescendingUsingLambda() {
        Company company = new SampleData().company();

        Comparator<Employee> byMonthlyPayDesc = (o1, o2) -> {
            int cmp = Double.compare(o2.calculateMonthlyPay(), o1.calculateMonthlyPay());
            return cmp != 0 ? cmp : o1.compareTo(o2);
        };

        ArrayList<Employee> sorted = company.getSortedEmployees(byMonthlyPayDesc);
        assertEquals(4, sorted.size());
        assertEquals("Коваль", sorted.get(0).getLastName());   // 55000
        assertEquals("Мудрий", sorted.get(1).getLastName());   // 35000
        assertEquals("Бойко", sorted.get(2).getLastName());    // 20000
        assertEquals("Шевченко", sorted.get(3).getLastName()); // 6000
    }

    @Test
    void shouldSortByAgeAscendingUsingLambda() {
        Company company = new SampleData().company();

        Comparator<Employee> byAgeAsc = (o1, o2) -> {
            int cmp = Integer.compare(o1.getAge(), o2.getAge());
            return cmp != 0 ? cmp : o1.compareTo(o2);
        };

        ArrayList<Employee> sorted = company.getSortedEmployees(byAgeAsc);
        assertEquals(20, sorted.get(0).getAge());
        assertEquals(25, sorted.get(1).getAge());
        assertEquals(30, sorted.get(2).getAge());
        assertEquals(38, sorted.get(3).getAge());
    }

    @Test
    void shouldSortByDepartmentAndNameUsingLambda() {
        Company company = new SampleData().company();

        final Collator uaCollator = Collator.getInstance(Locale.forLanguageTag("uk-UA"));
        uaCollator.setStrength(Collator.PRIMARY);

        Comparator<Employee> byDepartmentAndName = (o1, o2) -> {
            int cmp = uaCollator.compare(
                    o1.getDepartment().getTitle(),
                    o2.getDepartment().getTitle());
            return cmp != 0 ? cmp : o1.compareTo(o2);
        };

        ArrayList<Employee> sorted = company.getSortedEmployees(byDepartmentAndName);
        assertEquals(Department.IT, sorted.get(0).getDepartment());
        assertEquals("Коваль", sorted.get(0).getLastName());
        assertEquals(Department.IT, sorted.get(1).getDepartment());
        assertEquals("Мудрий", sorted.get(1).getLastName());
        assertEquals(Department.IT, sorted.get(2).getDepartment());
        assertEquals("Шевченко", sorted.get(2).getLastName());
        assertEquals(Department.SUPPORT, sorted.get(3).getDepartment());
        assertEquals("Бойко", sorted.get(3).getLastName());
    }

    @Test
    void shouldResolveEqualFieldValuesStablyUsingLambda() {
        Company company = new Company("EqualSalaryCorp");
        Employee secondByName = new FullTimeEmployee("Ярослав", "Мудрий", "Розробник",
                30000, 25, Department.IT, 5000, 24);
        Employee firstByName = new FullTimeEmployee("Олег", "Бойко", "Розробник",
                30000, 28, Department.IT, 4000, 24);

        company.addNewEmployee(secondByName, 1);
        company.addNewEmployee(firstByName, 1);

        Comparator<Employee> bySalaryAsc = (o1, o2) -> {
            int cmp = Double.compare(o1.getSalary(), o2.getSalary());
            return cmp != 0 ? cmp : o1.compareTo(o2);
        };

        ArrayList<Employee> sorted = company.getSortedEmployees(bySalaryAsc);
        assertEquals("Бойко", sorted.get(0).getLastName());
        assertEquals("Мудрий", sorted.get(1).getLastName());
    }

    @Test
    void shouldHandleEmptySingleElementAndNotModifyOriginalOrder() {
        Comparator<Employee> byAge = (o1, o2) -> Integer.compare(o1.getAge(), o2.getAge());

        Company empty = new Company("EmptyCorp");
        assertTrue(empty.getSortedEmployees(byAge).isEmpty());

        Company single = new Company("SingleCorp");
        Employee one = new FullTimeEmployee("Ярослав", "Мудрий", "Розробник",
                30000, 25, Department.IT, 5000, 24);
        single.addNewEmployee(one, 1);
        ArrayList<Employee> singleSorted = single.getSortedEmployees(byAge);
        assertEquals(1, singleSorted.size());
        assertEquals(one, singleSorted.get(0));

        Company full = new SampleData().company();
        full.getSortedEmployees(byAge);
        assertEquals("Мудрий", full.getEmployees().get(0).getLastName());
    }

    @Test
    void shouldRejectNullComparator() {
        Company company = new SampleData().company();
        assertThrows(IllegalArgumentException.class, () -> company.getSortedEmployees(null));
    }
}