import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class JsonStorage implements DataStorage {

    private final String fileName;
    private final EmployeeMapper mapper = new EmployeeMapper();

    public JsonStorage(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public String getFileName() {
        return fileName;
    }

    @Override
    public ArrayList<Employee> load() {
        ArrayList<Employee> list = new ArrayList<Employee>();
        File file = new File(fileName);
        if (!file.exists()) {
            System.out.println("Файл " + fileName + " не знайдено. Колекція порожня.");
            return list;
        }

        int skipped = 0;
        try (Reader reader = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8)) {
            JsonElement root = JsonParser.parseReader(reader);
            if (root == null || root.isJsonNull()) {
                System.out.println("Файл " + fileName + " порожній. Колекція порожня.");
                return list;
            }
            if (!root.isJsonArray()) {
                System.out.println("Помилка: корінь JSON має бути масивом. Колекція порожня.");
                return list;
            }

            JsonArray array = root.getAsJsonArray();
            for (int i = 0; i < array.size(); i++) {
                try {
                    list.add(mapper.fromFields(toFieldMap(array.get(i))));
                } catch (IllegalArgumentException e) {
                    skipped++;
                    System.out.println("Елемент №" + (i + 1) + " пропущено: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("Помилка читання файлу " + fileName + ": " + e.getMessage());
        } catch (JsonParseException e) {
            System.out.println("Помилка обробки JSON у файлі " + fileName + ": " + e.getMessage());
        }

        System.out.println("З файлу " + fileName + " прочитано об'єктів: " + list.size()
                + (skipped > 0 ? ", пропущено некоректних елементів: " + skipped : ""));
        return list;
    }

    @Override
    public void save(ArrayList<Employee> employees) {
        JsonArray array = new JsonArray();
        for (Employee e : employees) {
            JsonObject object = new JsonObject();
            for (Map.Entry<String, Object> entry : mapper.toFields(e).entrySet()) {
                Object value = entry.getValue();
                if (value instanceof Number) {
                    object.addProperty(entry.getKey(), (Number) value);
                } else {
                    object.addProperty(entry.getKey(), String.valueOf(value));
                }
            }
            array.add(object);
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
        try (Writer writer = new OutputStreamWriter(new FileOutputStream(fileName), StandardCharsets.UTF_8)) {
            gson.toJson(array, writer);
            System.out.println("У файл " + fileName + " записано об'єктів: " + employees.size());
        } catch (IOException e) {
            System.out.println("Помилка запису у файл " + fileName + ": " + e.getMessage());
        }
    }

    private HashMap<String, String> toFieldMap(JsonElement element) {
        if (element == null || !element.isJsonObject()) {
            throw new IllegalArgumentException("елемент масиву не є JSON-об'єктом");
        }
        HashMap<String, String> fields = new HashMap<String, String>();
        for (Map.Entry<String, JsonElement> entry : element.getAsJsonObject().entrySet()) {
            JsonElement value = entry.getValue();
            if (value != null && value.isJsonPrimitive()) {
                fields.put(entry.getKey(), value.getAsString());
            }
        }
        return fields;
    }
}