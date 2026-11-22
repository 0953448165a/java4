import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CompanyTest {

    @Test
    public void testAddNewEmployeeAndIncrementQuantity() {
        Company company = new Company("TechNova Ukraine", "Київ", 2018);

        Employee dev1 = new FullTimeEmployee(
                "Ярослав", "Мудрий", "Розробник",
                30000.0, 25, Department.IT, 5000.0, 24);
        Employee dev1Duplicate = new FullTimeEmployee(
                "Ярослав", "Мудрий", "Розробник",
                30000.0, 25, Department.IT, 5000.0, 24);
        Employee intern = new Intern(
                "Андрій", "Шевченко", "Стажист",
                12000.0, 20, Department.IT, "СумДУ", 3, "Ярослав Мудрий");

        company.addNewEmployee(dev1, 2);
        company.addNewEmployee(dev1Duplicate, 3);
        company.addNewEmployee(intern, 1);

        assertEquals(2, company.getEmployees().size());
        assertEquals(5, company.getEmployeeQuantity(dev1));
        assertEquals(1, company.getEmployeeQuantity(intern));
        assertEquals(6, company.getTotalHeadcount());
    }

    @Test
    public void testValidationErrors() {
        assertThrows(IllegalArgumentException.class, () -> new Company("", "Київ", 2020));
        assertThrows(IllegalArgumentException.class, () -> new Company("TechNova", "", 2020));
        assertThrows(IllegalArgumentException.class, () -> new Company("TechNova", "Київ", 1700));

        Company company = new Company("TechNova", "Київ", 2020);
        Employee emp = new Employee("Іван", "Петренко", "Менеджер", 10000.0, 40, Department.SALES);

        assertThrows(IllegalArgumentException.class, () -> company.addNewEmployee(null, 1));
        assertThrows(IllegalArgumentException.class, () -> company.addNewEmployee(emp, 0));
        assertThrows(IllegalArgumentException.class, () -> company.addNewEmployee(emp, -3));
    }

    @Test
    public void testCompanySearchMethods() {
        Company company = new Company("TechNova Ukraine", "Київ", 2018);

        Employee sales = new Employee("Іван", "Петренко", "Менеджер", 10000.0, 40, Department.SALES);
        Employee dev = new FullTimeEmployee("Ярослав", "Мудрий", "Розробник", 30000.0, 25, Department.IT, 5000.0, 24);
        Employee manager = new Manager("Марія", "Коваль", "Керівник", 40000.0, 38, Department.IT, 10000.0, 28, 5, "Alpha");

        company.addNewEmployee(sales, 2);
        company.addNewEmployee(dev, 4);
        company.addNewEmployee(manager, 1);

        ArrayList<Employee> byLastName = company.searchByLastName("мудр");
        assertEquals(1, byLastName.size());
        assertEquals(dev, byLastName.get(0));

        ArrayList<Employee> byDepartment = company.searchByDepartment(Department.IT);
        assertEquals(2, byDepartment.size());

        ArrayList<Employee> bySalary = company.searchBySalaryRange(25000.0, 45000.0);
        assertEquals(2, bySalary.size());
    }

    @Test
    public void testLoadAndSaveCompanyWithTextStorage(@TempDir Path tempDir) throws IOException {
        Path file = tempDir.resolve("company_input.txt");
        Files.write(file, Arrays.asList(
                "type=Company;name=TechNova Ukraine;city=Київ;foundedYear=2018",
                "type=FullTimeEmployee;firstName=Ярослав;lastName=Мудрий;position=Розробник;salary=30000.0;age=25;department=IT;bonus=5000.0;vacationDays=24;quantity=2",
                "type=FullTimeEmployee;firstName=Ярослав;lastName=Мудрий;position=Розробник;salary=30000.0;age=25;department=IT;bonus=5000.0;vacationDays=24;quantity=3",
                "type=Intern;firstName=Андрій;lastName=Шевченко;position=Стажист;salary=12000.0;age=20;department=IT;university=СумДУ;internshipMonths=3;mentorName=Ярослав Мудрий;quantity=1"
        ), StandardCharsets.UTF_8);

        TextStorage storage = new TextStorage(file.toString());
        Company loaded = storage.loadCompany();

        assertEquals("TechNova Ukraine", loaded.getName());
        assertEquals("Київ", loaded.getCity());
        assertEquals(2018, loaded.getFoundedYear());
        assertEquals(2, loaded.getEmployees().size());
        assertEquals(6, loaded.getTotalHeadcount());

        Path savedFile = tempDir.resolve("company_saved.txt");
        TextStorage outStorage = new TextStorage(savedFile.toString());
        outStorage.saveCompany(loaded);

        Company reloaded = outStorage.loadCompany();
        assertEquals(loaded.getName(), reloaded.getName());
        assertEquals(loaded.getTotalHeadcount(), reloaded.getTotalHeadcount());
        assertEquals(loaded.calculateTotalMonthlyPayroll(), reloaded.calculateTotalMonthlyPayroll(), 0.001);
    }
}