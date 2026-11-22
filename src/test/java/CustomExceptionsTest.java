import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CustomExceptionsTest {

    @Test
    void shouldThrowInvalidFieldValueExceptionWhenEmployeeFieldsAreInvalid() {
        assertThrows(InvalidFieldValueException.class, () ->
                new FullTimeEmployee("", "Мудрий", "Розробник", 30000.0, 25, Department.IT, 5000.0, 24));

        assertThrows(InvalidFieldValueException.class, () ->
                new FullTimeEmployee("Ярослав", "Мудрий", "Розробник", -100.0, 25, Department.IT, 5000.0, 24));

        assertThrows(InvalidFieldValueException.class, () ->
                new FullTimeEmployee("Ярослав", "Мудрий", "Розробник", 30000.0, 15, Department.IT, 5000.0, 24));

        Employee valid = new FullTimeEmployee("Ярослав", "Мудрий", "Розробник",
                30000.0, 25, Department.IT, 5000.0, 24);
        assertThrows(InvalidFieldValueException.class, () -> valid.setDepartment(null));
    }

    @Test
    void shouldThrowInvalidFieldValueExceptionWhenSubclassFieldsAreInvalid() {
        assertThrows(InvalidFieldValueException.class, () ->
                new Manager("Марія", "Коваль", "Керівник", 40000.0, 38, Department.IT,
                        10000.0, 28, 0, "Alpha"));

        assertThrows(InvalidFieldValueException.class, () ->
                new ContractEmployee("Олег", "Бойко", "Тестувальник", 20000.0, 30, Department.SUPPORT,
                        120, "TechStaff"));

        assertThrows(InvalidFieldValueException.class, () ->
                new Intern("Андрій", "Шевченко", "Стажист", 12000.0, 20, Department.IT,
                        "", 3, "Ярослав Мудрий"));
    }

    @Test
    void shouldThrowObjectNotFoundExceptionWhenDeletingNonExistingObject() {
        Company company = new Company("TechNova Ukraine");
        Employee missing = new FullTimeEmployee("Петро", "Іваненко", "Аналітик",
                28000.0, 29, Department.FINANCE, 4000.0, 24);

        assertThrows(ObjectNotFoundException.class, () -> company.delete(missing));
        assertThrows(ObjectNotFoundException.class, () -> company.delete(null));
    }

    @Test
    void shouldThrowObjectNotFoundExceptionWhenUpdatingNonExistingObject() {
        Company company = new SampleData().company();
        Employee stranger = new Intern("Петро", "Іваненко", "Стажист",
                10000.0, 21, Department.HR, "СумДУ", 3, "Марія Коваль");
        Employee replacement = new Intern("Петро", "Іваненко", "Молодший спеціаліст",
                15000.0, 21, Department.HR, "СумДУ", 6, "Марія Коваль");

        assertThrows(ObjectNotFoundException.class, () -> company.update(stranger, replacement));
        assertThrows(ObjectNotFoundException.class, () -> company.update(null, replacement));
        assertThrows(InvalidFieldValueException.class, () -> company.update(company.getEmployees().get(0), null));
    }

    @Test
    void shouldThrowDuplicateObjectExceptionWhenAddingDuplicateNewEmployee() {
        Company company = new Company("TechNova Ukraine");
        Employee first = new FullTimeEmployee("Ярослав", "Мудрий", "Розробник",
                30000.0, 25, Department.IT, 5000.0, 24);
        Employee duplicate = new FullTimeEmployee("Ярослав", "Мудрий", "Розробник",
                30000.0, 25, Department.IT, 5000.0, 24);

        company.addNewEmployee(first, 1);

        assertThrows(DuplicateObjectException.class, () -> company.addNewEmployee(duplicate, 1));
    }
}