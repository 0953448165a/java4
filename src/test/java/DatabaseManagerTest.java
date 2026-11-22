import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DatabaseManagerTest {

    @Test
    public void testMissingOrInvalidConfigThrowsException(@TempDir Path tempDir) throws IOException {
        assertThrows(IllegalArgumentException.class, () -> new DatabaseManager(""));
        assertThrows(IllegalStateException.class, () -> new DatabaseManager(tempDir.resolve("missing.properties").toString()));

        Path incompleteConfig = tempDir.resolve("bad.properties");
        Files.write(incompleteConfig, Arrays.asList(
                "db.url=jdbc:h2:mem:test_bad",
                "db.user=sa"
        ), StandardCharsets.UTF_8);

        assertThrows(IllegalArgumentException.class, () -> new DatabaseManager(incompleteConfig.toString()));
    }

    @Test
    public void testInsertAllEmployeeTypes(@TempDir Path tempDir) throws IOException, SQLException {
        Path configFile = tempDir.resolve("test_db.properties");
        Files.write(configFile, Arrays.asList(
                "db.url=jdbc:h2:mem:java4_lab12;MODE=PostgreSQL;DATABASE_TO_LOWER=TRUE;DB_CLOSE_DELAY=-1",
                "db.user=sa",
                "db.password=sa"
        ), StandardCharsets.UTF_8);

        DatabaseManager dbManager = new DatabaseManager(configFile.toString());
        assertTrue(dbManager.testConnection());
        dbManager.executeSchemaFile("schema.sql");

        Employee base = new Employee("Іван", "Петренко", "Менеджер", 10000.0, 40, Department.SALES);
        Employee fullTime = new FullTimeEmployee("Ярослав", "Мудрий", "Розробник", 30000.0, 25, Department.IT, 5000.0, 24);
        Employee contract = new ContractEmployee("Олег", "Бойко", "Тестувальник", 20000.0, 30, Department.SUPPORT, 6, "TechStaff");
        Employee manager = new Manager("Марія", "Коваль", "Керівник відділу", 40000.0, 38, Department.IT, 10000.0, 28, 5, "Alpha");
        Employee intern = new Intern("Андрій", "Шевченко", "Стажист", 12000.0, 20, Department.IT, "СумДУ", 3, "Ярослав Мудрий");

        assertTrue(dbManager.insertEmployee(base, 1));
        assertTrue(dbManager.insertEmployee(fullTime, 2));
        assertTrue(dbManager.insertEmployee(contract, 1));
        assertTrue(dbManager.insertEmployee(manager, 1));
        assertTrue(dbManager.insertEmployee(intern, 3));

        try (Connection conn = dbManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM employees ORDER BY id")) {

            assertTrue(rs.next());
            assertEquals("Employee", rs.getString("type"));
            assertEquals("Іван", rs.getString("first_name"));
            assertNull(rs.getObject("bonus"));

            assertTrue(rs.next());
            assertEquals("FullTimeEmployee", rs.getString("type"));
            assertEquals(5000.0, rs.getDouble("bonus"), 0.001);
            assertEquals(24, rs.getInt("vacation_days"));
            assertEquals(2, rs.getInt("quantity"));

            assertTrue(rs.next());
            assertEquals("ContractEmployee", rs.getString("type"));
            assertEquals(6, rs.getInt("contract_months"));
            assertEquals("TechStaff", rs.getString("agency"));

            assertTrue(rs.next());
            assertEquals("Manager", rs.getString("type"));
            assertEquals(5, rs.getInt("team_size"));
            assertEquals("Alpha", rs.getString("project_name"));

            assertTrue(rs.next());
            assertEquals("Intern", rs.getString("type"));
            assertEquals("СумДУ", rs.getString("university"));
            assertEquals(3, rs.getInt("internship_months"));
            assertEquals("Ярослав Мудрий", rs.getString("mentor_name"));
        }
    }
}