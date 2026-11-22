import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.Properties;

public class DatabaseManager {

    private static final String INSERT_SQL =
            "INSERT INTO employees ("
                    + "type, first_name, last_name, position, salary, age, department, quantity, "
                    + "bonus, vacation_days, contract_months, agency, team_size, project_name, "
                    + "university, internship_months, mentor_name"
                    + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private final String url;
    private final String user;
    private final String password;

    public DatabaseManager(String configFilePath) {
        if (configFilePath == null || configFilePath.isBlank()) {
            throw new IllegalArgumentException("Шлях до конфігураційного файлу БД не може бути порожнім.");
        }

        Properties properties = new Properties();
        try (InputStreamReader reader = new InputStreamReader(
                new FileInputStream(configFilePath.trim()), StandardCharsets.UTF_8)) {
            properties.load(reader);
        } catch (IOException e) {
            throw new IllegalStateException(
                    "Не вдалося прочитати конфігураційний файл БД «" + configFilePath + "»: " + e.getMessage(), e);
        }

        this.url = requireProperty(properties, "db.url", configFilePath);
        this.user = requireProperty(properties, "db.user", configFilePath);
        this.password = requireProperty(properties, "db.password", configFilePath);
    }

    private static String requireProperty(Properties props, String key, String filePath) {
        String value = props.getProperty(key);
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(
                    "У файлі «" + filePath + "» відсутній або порожній параметр «" + key + "».");
        }
        return value.trim();
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    /** Перевіряє підключення до бази даних при запуску програми. */
    public boolean testConnection() {
        try (Connection connection = getConnection()) {
            System.out.println("Підключення до бази даних успішно встановлено: " + url);
            return true;
        } catch (SQLException e) {
            System.out.println("Помилка підключення до БД (" + url + "): " + e.getMessage());
            return false;
        }
    }

    /** Виконує SQL-скрипт створення таблиці з файлу schema.sql. */
    public void executeSchemaFile(String schemaFilePath) {
        StringBuilder sqlBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(schemaFilePath), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String trimmed = line.trim();
                if (!trimmed.isEmpty() && !trimmed.startsWith("--")) {
                    sqlBuilder.append(line).append('\n');
                }
            }
        } catch (IOException e) {
            throw new IllegalStateException("Помилка читання SQL-файлу «" + schemaFilePath + "»: " + e.getMessage(), e);
        }

        String[] statements = sqlBuilder.toString().split(";");
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            for (int i = 0; i < statements.length; i++) {
                String sql = statements[i].trim();
                if (!sql.isEmpty()) {
                    statement.execute(sql);
                }
            }
        } catch (SQLException e) {
            System.out.println("Помилка виконання SQL-схеми: " + e.getMessage());
        }
    }

    /** Зберігає створеного працівника в таблицю employees за допомогою PreparedStatement. */
    public boolean insertEmployee(Employee emp) {
        return insertEmployee(emp, 1);
    }

    /** Зберігає створеного працівника із зазначеною кількістю в таблицю employees. */
    public boolean insertEmployee(Employee emp, int quantity) {
        if (emp == null) {
            throw new IllegalArgumentException("Об'єкт працівника для збереження в БД не може бути null.");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Кількість працівників має бути більшою за 0.");
        }

        String discriminatorType = emp.getClass().getSimpleName();

        Double bonus = null;
        Integer vacationDays = null;
        Integer contractMonths = null;
        String agency = null;
        Integer teamSize = null;
        String projectName = null;
        String university = null;
        Integer internshipMonths = null;
        String mentorName = null;

        // Manager перевіряється раніше за FullTimeEmployee, бо є його підкласом
        if (emp instanceof Manager) {
            Manager m = (Manager) emp;
            bonus = Double.valueOf(m.getBonus());
            vacationDays = Integer.valueOf(m.getVacationDays());
            teamSize = Integer.valueOf(m.getTeamSize());
            projectName = m.getProjectName();
        } else if (emp instanceof FullTimeEmployee) {
            FullTimeEmployee f = (FullTimeEmployee) emp;
            bonus = Double.valueOf(f.getBonus());
            vacationDays = Integer.valueOf(f.getVacationDays());
        } else if (emp instanceof ContractEmployee) {
            ContractEmployee c = (ContractEmployee) emp;
            contractMonths = Integer.valueOf(c.getContractMonths());
            agency = c.getAgency();
        } else if (emp instanceof Intern) {
            Intern in = (Intern) emp;
            university = in.getUniversity();
            internshipMonths = Integer.valueOf(in.getInternshipMonths());
            mentorName = in.getMentorName();
        }

        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(INSERT_SQL)) {

            ps.setString(1, discriminatorType);
            ps.setString(2, emp.getFirstName());
            ps.setString(3, emp.getLastName());
            ps.setString(4, emp.getPosition());
            ps.setDouble(5, emp.getSalary());
            ps.setInt(6, emp.getAge());
            ps.setString(7, emp.getDepartment().name());
            ps.setInt(8, quantity);

            setNullableDouble(ps, 9, bonus);
            setNullableInt(ps, 10, vacationDays);
            setNullableInt(ps, 11, contractMonths);
            setNullableString(ps, 12, agency);
            setNullableInt(ps, 13, teamSize);
            setNullableString(ps, 14, projectName);
            setNullableString(ps, 15, university);
            setNullableInt(ps, 16, internshipMonths);
            setNullableString(ps, 17, mentorName);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Запис успішно збережено в БД (таблиця employees, type=" + discriminatorType + ").");
                return true;
            }
            return false;
        } catch (SQLException e) {
            System.out.println("Помилка SQL під час вставки запису в БД: " + e.getMessage());
            return false;
        }
    }

    private void setNullableDouble(PreparedStatement ps, int index, Double value) throws SQLException {
        if (value == null) {
            ps.setNull(index, Types.DOUBLE);
        } else {
            ps.setDouble(index, value.doubleValue());
        }
    }

    private void setNullableInt(PreparedStatement ps, int index, Integer value) throws SQLException {
        if (value == null) {
            ps.setNull(index, Types.INTEGER);
        } else {
            ps.setInt(index, value.intValue());
        }
    }

    private void setNullableString(PreparedStatement ps, int index, String value) throws SQLException {
        if (value == null) {
            ps.setNull(index, Types.VARCHAR);
        } else {
            ps.setString(index, value);
        }
    }
}