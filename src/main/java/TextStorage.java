import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class TextStorage implements DataStorage {

    private final String fileName;
    private final EmployeeMapper mapper = new EmployeeMapper();

    public TextStorage(String fileName) {
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
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            String line;
            int number = 0;
            while ((line = reader.readLine()) != null) {
                number++;
                if (number == 1 && line.startsWith("\uFEFF")) {
                    line = line.substring(1); // прибираємо BOM, якщо файл збережено з ним
                }
                String trimmed = line.trim();
                if (trimmed.isEmpty() || trimmed.startsWith("#")) {
                    continue; // порожні рядки та коментарі ігноруються
                }
                try {
                    list.add(mapper.fromFields(parseLine(trimmed)));
                } catch (IllegalArgumentException e) {
                    skipped++;
                    System.out.println("Рядок " + number + " пропущено: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("Помилка читання файлу " + fileName + ": " + e.getMessage());
        }

        System.out.println("З файлу " + fileName + " прочитано об'єктів: " + list.size()
                + (skipped > 0 ? ", пропущено некоректних рядків: " + skipped : ""));
        return list;
    }

    @Override
    public void save(ArrayList<Employee> employees) {
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(fileName), StandardCharsets.UTF_8))) {
            writer.write("# Один рядок - один об'єкт: пари ключ=значення, розділені символом крапки з комою");
            writer.newLine();
            for (Employee e : employees) {
                writer.write(buildLine(mapper.toFields(e)));
                writer.newLine();
            }
            System.out.println("У файл " + fileName + " записано об'єктів: " + employees.size());
        } catch (IOException e) {
            System.out.println("Помилка запису у файл " + fileName + ": " + e.getMessage());
        }
    }

    private String buildLine(LinkedHashMap<String, Object> fields) {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, Object> entry : fields.entrySet()) {
            if (!first) {
                sb.append(';');
            }
            first = false;
            sb.append(escape(entry.getKey())).append('=').append(escape(String.valueOf(entry.getValue())));
        }
        return sb.toString();
    }

    private String escape(String s) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            if (ch == '\\' || ch == ';' || ch == '=') {
                sb.append('\\');
            }
            sb.append(ch);
        }
        return sb.toString();
    }

    /** Розбирає рядок виду key=value;key=value з урахуванням екранування. */
    private HashMap<String, String> parseLine(String line) {
        HashMap<String, String> fields = new HashMap<String, String>();
        StringBuilder key = new StringBuilder();
        StringBuilder value = new StringBuilder();
        boolean readingKey = true;
        boolean escaped = false;

        for (int i = 0; i < line.length(); i++) {
            char ch = line.charAt(i);
            if (escaped) {
                if (readingKey) {
                    key.append(ch);
                } else {
                    value.append(ch);
                }
                escaped = false;
            } else if (ch == '\\') {
                escaped = true;
            } else if (ch == '=' && readingKey) {
                readingKey = false;
            } else if (ch == ';') {
                putPair(fields, key, value, readingKey);
                key.setLength(0);
                value.setLength(0);
                readingKey = true;
            } else if (readingKey) {
                key.append(ch);
            } else {
                value.append(ch);
            }
        }
        if (escaped) {
            throw new IllegalArgumentException("рядок закінчується символом екранування");
        }
        putPair(fields, key, value, readingKey);
        return fields;
    }

    private void putPair(HashMap<String, String> fields, StringBuilder key, StringBuilder value,
                         boolean readingKey) {
        String k = key.toString().trim();
        if (k.isEmpty() && value.length() == 0) {
            return; // порожній фрагмент (наприклад, зайва крапка з комою в кінці)
        }
        if (readingKey) {
            throw new IllegalArgumentException("поле «" + k + "» не має значення (немає знака =)");
        }
        if (k.isEmpty()) {
            throw new IllegalArgumentException("значення без назви поля");
        }
        fields.put(k, value.toString());
    }
}