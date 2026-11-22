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
                    line = line.substring(1);
                }
                String trimmed = line.trim();
                if (trimmed.isEmpty() || trimmed.startsWith("#")) {
                    continue;
                }
                try {
                    HashMap<String, String> fields = parseLine(trimmed);
                    if ("Company".equals(fields.get("type"))) {
                        continue;
                    }
                    list.add(mapper.fromFields(fields));
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
            for (int i = 0; i < employees.size(); i++) {
                Employee e = employees.get(i);
                writer.write(buildLine(mapper.toFields(e)));
                writer.newLine();
            }
            System.out.println("У файл " + fileName + " записано об'єктів: " + employees.size());
        } catch (IOException e) {
            System.out.println("Помилка запису у файл " + fileName + ": " + e.getMessage());
        }
    }

    /**
     * Зчитує з файлу параметри компанії та колекцію працівників із кількістю,
     * додаючи кожного працівника через company.addNewEmployee(emp, quantity).
     */
    public Company loadCompany() {
        File file = new File(fileName);
        if (!file.exists()) {
            throw new IllegalStateException("Файл " + fileName + " не знайдено.");
        }

        Company company = null;
        ArrayList<Employee> pendingEmployees = new ArrayList<Employee>();
        ArrayList<Integer> pendingQuantities = new ArrayList<Integer>();
        int skipped = 0;

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            String line;
            int number = 0;
            while ((line = reader.readLine()) != null) {
                number++;
                if (number == 1 && line.startsWith("\uFEFF")) {
                    line = line.substring(1);
                }
                String trimmed = line.trim();
                if (trimmed.isEmpty() || trimmed.startsWith("#")) {
                    continue;
                }

                try {
                    HashMap<String, String> fields = parseLine(trimmed);
                    String type = fields.get("type");
                    if ("Company".equals(type)) {
                        company = parseCompany(fields);
                    } else {
                        Employee emp = mapper.fromFields(fields);
                        int qty = parseQuantity(fields);
                        if (company != null) {
                            company.addNewEmployee(emp, qty);
                        } else {
                            pendingEmployees.add(emp);
                            pendingQuantities.add(Integer.valueOf(qty));
                        }
                    }
                } catch (IllegalArgumentException e) {
                    skipped++;
                    System.out.println("Рядок " + number + " пропущено: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            throw new IllegalStateException("Помилка читання файлу " + fileName + ": " + e.getMessage(), e);
        }

        if (company == null) {
            throw new IllegalStateException("У файлі " + fileName + " відсутній запис type=Company.");
        }

        for (int i = 0; i < pendingEmployees.size(); i++) {
            company.addNewEmployee(pendingEmployees.get(i), pendingQuantities.get(i).intValue());
        }

        System.out.println("З файлу " + fileName + " завантажено компанію «" + company.getName()
                + "», унікальних позицій: " + company.getEmployees().size()
                + ", загальний штат: " + company.getTotalHeadcount()
                + (skipped > 0 ? ", пропущено некоректних рядків: " + skipped : ""));
        return company;
    }

    /**
     * Зберігає дані компанії та всі записи працівників із їхньою кількістю у файл.
     */
    public void saveCompany(Company company) {
        if (company == null) {
            throw new IllegalArgumentException("Об'єкт Company не може бути null.");
        }

        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(fileName), StandardCharsets.UTF_8))) {
            writer.write("# Один рядок - один об'єкт: пари ключ=значення, розділені символом крапки з комою");
            writer.newLine();

            LinkedHashMap<String, Object> companyFields = new LinkedHashMap<String, Object>();
            companyFields.put("type", "Company");
            companyFields.put("name", company.getName());
            companyFields.put("city", company.getCity());
            companyFields.put("foundedYear", Integer.valueOf(company.getFoundedYear()));
            writer.write(buildLine(companyFields));
            writer.newLine();

            ArrayList<Employee> employees = company.getEmployees();
            ArrayList<Integer> quantities = company.getQuantities();

            for (int i = 0; i < employees.size(); i++) {
                LinkedHashMap<String, Object> fields = mapper.toFields(employees.get(i));
                fields.put("quantity", quantities.get(i));
                writer.write(buildLine(fields));
                writer.newLine();
            }

            System.out.println("У файл " + fileName + " збережено компанію «" + company.getName()
                    + "» та позицій працівників: " + employees.size());
        } catch (IOException e) {
            System.out.println("Помилка запису у файл " + fileName + ": " + e.getMessage());
        }
    }

    private Company parseCompany(HashMap<String, String> fields) {
        String name = fields.get("name");
        String city = fields.get("city");
        String yearRaw = fields.get("foundedYear");
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("відсутнє поле «name» для Company");
        }
        if (city == null || city.isBlank()) {
            throw new IllegalArgumentException("відсутнє поле «city» для Company");
        }
        if (yearRaw == null || yearRaw.isBlank()) {
            throw new IllegalArgumentException("відсутнє поле «foundedYear» для Company");
        }
        int foundedYear;
        try {
            foundedYear = Integer.parseInt(yearRaw.trim());
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("поле «foundedYear» має бути цілим числом");
        }
        return new Company(name, city, foundedYear);
    }

    private int parseQuantity(HashMap<String, String> fields) {
        String raw = fields.get("quantity");
        if (raw == null || raw.isBlank()) {
            return 1;
        }
        try {
            int qty = Integer.parseInt(raw.trim());
            if (qty <= 0) {
                throw new IllegalArgumentException("поле «quantity» має бути більшим за 0");
            }
            return qty;
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("поле «quantity» має бути цілим числом, а не «" + raw + "»");
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
            return;
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