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
            "type=Employee;firstName=Іван;lastName=Петренко;position=Менеджер;salary=10000.0;age=40;department=SALES";

    @TempDir
    Path tempDir;

    private void write(Path path, String text) throws IOException {
        Files.write(path, text.getBytes(StandardCharsets.UTF_8));
    }

    private String read(Path path) throws IOException {
        return new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
    }

    @Test
    void shouldSaveAndLoadAllTypes() {
        TextStorage storage = new TextStorage(tempDir.resolve("input.txt").toString());
        ArrayList<Employee> original = new SampleData().employees();

        storage.save(original);
        ArrayList<Employee> loaded = storage.load();

        assertEquals(5, loaded.size());
        assertEquals(original, loaded);
    }

    @Test
    void shouldReturnEmptyListWhenFileMissing() {
        TextStorage storage = new TextStorage(tempDir.resolve("missing.txt").toString());
        assertTrue(storage.load().isEmpty());
    }

    @Test
    void shouldSkipInvalidLines() throws IOException {
        Path file = tempDir.resolve("input.txt");
        write(file, VALID + "\n"
                + "type=Unknown;firstName=A\n"
                + "type=Employee;firstName=Іван\n"
                + "garbage\n"
                + "type=Employee;firstName=Іван;lastName=Петренко;position=Менеджер;salary=10000.0;age=abc;department=SALES\n"
                + "type=Employee;firstName=Іван;lastName=Петренко;position=Менеджер;salary=10000.0;age=10;department=SALES\n"
                + "type=Employee;firstName=Іван;lastName=Петренко;position=Менеджер;salary=10000.0;age=40;department=NOWHERE\n");

        ArrayList<Employee> loaded = new TextStorage(file.toString()).load();

        assertEquals(1, loaded.size());
        assertEquals("Іван", loaded.get(0).getFirstName());
    }

    @Test
    void shouldSkipCommentsAndEmptyLines() throws IOException {
        Path file = tempDir.resolve("input.txt");
        write(file, "# коментар\n\n" + VALID + "\n   \n" + VALID + "\n");

        assertEquals(2, new TextStorage(file.toString()).load().size());
    }

    @Test
    void shouldRoundTripSpecialCharacters() {
        TextStorage storage = new TextStorage(tempDir.resolve("input.txt").toString());
        ArrayList<Employee> original = new ArrayList<Employee>();
        original.add(new SampleData().managerWithSpecialChars());

        storage.save(original);
        ArrayList<Employee> loaded = storage.load();

        assertEquals(original, loaded);
        assertEquals("Alpha; v=2\\beta", ((Manager) loaded.get(0)).getProjectName());
    }

    @Test
    void shouldWriteTypeForEveryObject() throws IOException {
        Path file = tempDir.resolve("input.txt");
        new TextStorage(file.toString()).save(new SampleData().employees());

        String text = read(file);
        assertTrue(text.contains("type=Employee;"));
        assertTrue(text.contains("type=FullTimeEmployee;"));
        assertTrue(text.contains("type=ContractEmployee;"));
        assertTrue(text.contains("type=Manager;"));
        assertTrue(text.contains("type=Intern;"));
    }
}