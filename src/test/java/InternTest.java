import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InternTest {

    private Intern validIntern() {
        return new Intern("Андрій", "Шевченко", "Стажист",
                12000, 20, Department.IT, "СумДУ", 3, "Ярослав Мудрий");
    }

    @Test
    void shouldCreateInternWithValidData() {
        Intern i = validIntern();
        assertEquals("Шевченко", i.getLastName());
        assertEquals("СумДУ", i.getUniversity());
        assertEquals(3, i.getInternshipMonths());
        assertEquals("Ярослав Мудрий", i.getMentorName());
    }

    @Test
    void shouldBeInstanceOfEmployee() {
        assertInstanceOf(Employee.class, validIntern());
    }

    @Test
    void shouldThrowExceptionWhenInvalidOwnFields() {
        assertThrows(IllegalArgumentException.class,
                () -> new Intern("Андрій", "Шевченко", "Стажист", 12000, 20, Department.IT, "", 3, "Ментор"));
        assertThrows(IllegalArgumentException.class,
                () -> new Intern("Андрій", "Шевченко", "Стажист", 12000, 20, Department.IT, "СумДУ", 0, "Ментор"));
        assertThrows(IllegalArgumentException.class,
                () -> new Intern("Андрій", "Шевченко", "Стажист", 12000, 20, Department.IT, "СумДУ", 13, "Ментор"));
        assertThrows(IllegalArgumentException.class,
                () -> new Intern("Андрій", "Шевченко", "Стажист", 12000, 20, Department.IT, "СумДУ", 3, null));
    }

    @Test
    void shouldValidateInheritedFields() {
        assertThrows(IllegalArgumentException.class,
                () -> new Intern("Андрій", "Шевченко", "Стажист", 12000, 17, Department.IT, "СумДУ", 3, "Ментор"));
        assertThrows(IllegalArgumentException.class,
                () -> new Intern("Андрій", "Шевченко", "Стажист", 12000, 20, null, "СумДУ", 3, "Ментор"));
    }

    @Test
    void monthlyPayShouldBeHalfOfSalary() {
        assertEquals(6000, validIntern().calculateMonthlyPay());
        assertEquals(18000, validIntern().getTotalStipend());
    }

    @Test
    void shouldReturnOwnEmployeeType() {
        assertEquals("Стажист", validIntern().getEmployeeType());
    }

    @Test
    void toStringShouldContainOwnFields() {
        String text = validIntern().toString();
        assertTrue(text.startsWith("Intern{"));
        assertTrue(text.contains("university='СумДУ'"));
        assertTrue(text.contains("internshipMonths=3"));
        assertTrue(text.contains("mentorName='Ярослав Мудрий'"));
    }

    @Test
    void equalsShouldConsiderOwnFields() {
        Intern a = validIntern();
        Intern b = validIntern();
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());

        b.setInternshipMonths(6);
        assertNotEquals(a, b);
    }
}