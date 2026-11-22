import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class TextStorageTest {

    private static final String VALID =
            "type=FullTimeEmployee;firstName=Ярослав;lastName=Мудрий;position=Розробник;salary=30000.0;age=25;department=IT;bonus=5000.0;vacationDays=24";

    @TempDir
    Path tempDir;

    private void write(Path path, String text) throws IOException {
        Files.write(path, text.getBytes(StandardCharsets.UTF_8));
    }

    @Test
    void shouldSaveAndLoadCompany() {
        TextStorage storage = new TextStorage(tempDir.resolve("input.txt").toString());
        Company original = new SampleData().company();

        storage.saveCompany(original);
        Company loaded = storage.loadCompany();

        assertEquals(original, loaded);
    }

    @Test
    void shouldSaveAndLoadAllSubtypes() {
        TextStorage storage = new TextStorage(tempDir.resolve("input.txt").toString());
        ArrayList<Employee> original = new SampleData().employees();

        storage.save(original);
        ArrayList<Employee> loaded = storage.load();

        assertEquals(4, loaded.size());
        assertEquals(original, loaded);
    }

    @Test
    void shouldSkipAbstractOrInvalidLines() throws IOException {
        Path file = tempDir.resolve("input.txt");
        write(file, VALID + "\n"
                + "type=Employee;firstName=Іван;lastName=Петренко;position=Менеджер;salary=10000.0;age=40;department=SALES\n"
                + "type=Unknown;firstName=A\n"
                + "garbage\n");

        ArrayList<Employee> loaded = new TextStorage(file.toString()).load();

        assertEquals(1, loaded.size());
        assertEquals("Мудрий", loaded.get(0).getLastName());
    }
}