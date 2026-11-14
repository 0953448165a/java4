import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class JsonStorageTest {

    private static final String VALID =
            "{\"type\":\"Employee\",\"firstName\":\"Іван\",\"lastName\":\"Петренко\","
                    + "\"position\":\"Менеджер\",\"salary\":10000.0,\"age\":40,\"department\":\"SALES\"}";

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
        JsonStorage storage = new JsonStorage(tempDir.resolve("input.json").toString());
        ArrayList<Employee> original = new SampleData().employees();

        storage.save(original);
        ArrayList<Employee> loaded = storage.load();

        assertEquals(5, loaded.size());
        assertEquals(original, loaded);
    }

    @Test
    void shouldReturnEmptyListWhenFileMissing() {
        JsonStorage storage = new JsonStorage(tempDir.resolve("missing.json").toString());
        assertTrue(storage.load().isEmpty());
    }

    @Test
    void shouldSkipInvalidElements() throws IOException {
        Path file = tempDir.resolve("input.json");
        write(file, "[" + VALID + ","
                + "{\"type\":\"Unknown\",\"firstName\":\"A\"},"
                + "{\"type\":\"Employee\",\"firstName\":\"Іван\"},"
                + "5,"
                + "{\"type\":\"Employee\",\"firstName\":\"Іван\",\"lastName\":\"Петренко\","
                + "\"position\":\"Менеджер\",\"salary\":10000.0,\"age\":10,\"department\":\"SALES\"}]");

        ArrayList<Employee> loaded = new JsonStorage(file.toString()).load();

        assertEquals(1, loaded.size());
        assertEquals("Петренко", loaded.get(0).getLastName());
    }

    @Test
    void shouldReturnEmptyListForInvalidJson() throws IOException {
        Path file = tempDir.resolve("input.json");
        write(file, "{ this is not json");

        assertTrue(new JsonStorage(file.toString()).load().isEmpty());
    }

    @Test
    void shouldReturnEmptyListWhenRootIsNotArray() throws IOException {
        Path file = tempDir.resolve("input.json");
        write(file, "{\"type\":\"Employee\"}");

        assertTrue(new JsonStorage(file.toString()).load().isEmpty());
    }

    @Test
    void shouldRoundTripSpecialCharacters() {
        JsonStorage storage = new JsonStorage(tempDir.resolve("input.json").toString());
        ArrayList<Employee> original = new ArrayList<Employee>();
        original.add(new SampleData().managerWithSpecialChars());

        storage.save(original);

        assertEquals(original, storage.load());
    }

    @Test
    void shouldWriteTypeField() throws IOException {
        Path file = tempDir.resolve("input.json");
        new JsonStorage(file.toString()).save(new SampleData().employees());

        String text = read(file);
        assertTrue(text.contains("\"type\": \"Manager\""));
        assertTrue(text.contains("\"type\": \"Intern\""));
        assertTrue(text.contains("\"teamSize\": 5"));
    }
}