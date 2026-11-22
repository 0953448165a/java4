import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Separator;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;

public class MainApp extends Application {

    private static final String DATA_FILE = "input.txt";
    private final TextStorage storage = new TextStorage(DATA_FILE);
    private Company company;

    private final ObservableList<String> shortItems = FXCollections.observableArrayList();
    private ListView<String> collectionListView;
    private Label summaryLabel;

    private ComboBox<String> typeBox;
    private TextField firstNameField;
    private TextField lastNameField;
    private TextField positionField;
    private TextField salaryField;
    private TextField ageField;
    private ComboBox<Department> departmentBox;
    private TextField quantityField;
    private Label extraLabel1;
    private TextField extraField1;
    private Label extraLabel2;
    private TextField extraField2;
    private Label addStatusLabel;

    private TextField uuidSearchField;
    private Label searchStatusLabel;
    private TextArea detailsArea;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        initCompany();

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(14));

        Label headerLabel = new Label("Управління працівниками компанії (UUID + JavaFX GUI) — Мудрий Ярослав, ІН-43-5");
        headerLabel.setStyle("-fx-font-size: 15px; -fx-font-weight: bold;");
        BorderPane.setMargin(headerLabel, new Insets(0, 0, 12, 0));
        root.setTop(headerLabel);

        VBox leftPane = buildAddFormPane();
        VBox centerPane = buildCollectionPane();
        VBox rightPane = buildSearchPane();

        HBox mainContent = new HBox(14, leftPane, centerPane, rightPane);
        HBox.setHgrow(centerPane, Priority.ALWAYS);
        HBox.setHgrow(rightPane, Priority.ALWAYS);
        root.setCenter(mainContent);

        refreshCollectionView();

        Scene scene = new Scene(root, 1220, 620);
        primaryStage.setTitle("Лабораторна робота №16 — UUID + JavaFX GUI");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void initCompany() {
        File file = new File(DATA_FILE);
        if (file.exists()) {
            try {
                company = storage.loadCompany();
                return;
            } catch (Exception ignored) {
                // Якщо файл пошкоджений або порожній — створюємо базову компанію
            }
        }
        company = new Company("TechNova Ukraine", "Суми", 2020);
        company.addNewEmployee(new FullTimeEmployee("Ярослав", "Мудрий", "Розробник",
                30000.0, 25, Department.IT, 5000.0, 24), 2);
        company.addNewEmployee(new ContractEmployee("Олег", "Бойко", "Тестувальник",
                20000.0, 30, Department.SUPPORT, 6, "TechStaff"), 1);
        company.addNewEmployee(new Manager("Марія", "Коваль", "Керівник відділу",
                40000.0, 38, Department.IT, 10000.0, 28, 5, "Alpha"), 1);
        company.addNewEmployee(new Intern("Андрій", "Шевченко", "Стажист",
                12000.0, 20, Department.IT, "СумДУ", 3, "Ярослав Мудрий"), 3);
        storage.saveCompany(company);
    }

    private VBox buildAddFormPane() {
        VBox box = new VBox(8);
        box.setPrefWidth(340);
        box.setPadding(new Insets(10));
        box.setStyle("-fx-border-color: #cbd5e1; -fx-border-radius: 6; -fx-background-color: #f8fafc;");

        Label title = new Label("1. Додати новий об'єкт");
        title.setStyle("-fx-font-size: 13px; -fx-font-weight: bold;");

        GridPane grid = new GridPane();
        grid.setHgap(8);
        grid.setVgap(7);

        typeBox = new ComboBox<String>(FXCollections.observableArrayList(
                "FullTimeEmployee", "ContractEmployee", "Manager", "Intern"));
        typeBox.setValue("FullTimeEmployee");
        typeBox.setMaxWidth(Double.MAX_VALUE);

        firstNameField = new TextField();
        firstNameField.setPromptText("Наприклад: Іван");

        lastNameField = new TextField();
        lastNameField.setPromptText("Наприклад: Петренко");

        positionField = new TextField();
        positionField.setPromptText("Наприклад: Інженер");

        salaryField = new TextField();
        salaryField.setPromptText("Наприклад: 25000");

        ageField = new TextField();
        ageField.setPromptText("18..70");

        departmentBox = new ComboBox<Department>(FXCollections.observableArrayList(Department.values()));
        departmentBox.setValue(Department.IT);
        departmentBox.setMaxWidth(Double.MAX_VALUE);

        quantityField = new TextField("1");

        extraLabel1 = new Label("Премія:");
        extraField1 = new TextField("3000");

        extraLabel2 = new Label("Відпустка (дн.):");
        extraField2 = new TextField("24");

        typeBox.setOnAction(event -> updateExtraFields());

        grid.add(new Label("Тип об'єкта:"), 0, 0);
        grid.add(typeBox, 1, 0);
        grid.add(new Label("Ім'я:"), 0, 1);
        grid.add(firstNameField, 1, 1);
        grid.add(new Label("Прізвище:"), 0, 2);
        grid.add(lastNameField, 1, 2);
        grid.add(new Label("Посада:"), 0, 3);
        grid.add(positionField, 1, 3);
        grid.add(new Label("Зарплата:"), 0, 4);
        grid.add(salaryField, 1, 4);
        grid.add(new Label("Вік:"), 0, 5);
        grid.add(ageField, 1, 5);
        grid.add(new Label("Відділ:"), 0, 6);
        grid.add(departmentBox, 1, 6);
        grid.add(new Label("Кількість:"), 0, 7);
        grid.add(quantityField, 1, 7);
        grid.add(extraLabel1, 0, 8);
        grid.add(extraField1, 1, 8);
        grid.add(extraLabel2, 0, 9);
        grid.add(extraField2, 1, 9);

        Button addButton = new Button("Додати");
        addButton.setMaxWidth(Double.MAX_VALUE);
        addButton.setStyle("-fx-font-weight: bold;");
        addButton.setOnAction(event -> handleAddEmployee());

        addStatusLabel = new Label("UUID присвоюється автоматично при створенні.");
        addStatusLabel.setWrapText(true);
        addStatusLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #334155;");

        box.getChildren().addAll(title, new Separator(), grid, addButton, addStatusLabel);
        return box;
    }

    private void updateExtraFields() {
        String selectedType = typeBox.getValue();
        if ("FullTimeEmployee".equals(selectedType)) {
            extraLabel1.setText("Премія:");
            extraField1.setText("3000");
            extraLabel2.setText("Відпустка (дн.):");
            extraField2.setText("24");
        } else if ("ContractEmployee".equals(selectedType)) {
            extraLabel1.setText("Контракт (міс.):");
            extraField1.setText("6");
            extraLabel2.setText("Агенція:");
            extraField2.setText("TechStaff");
        } else if ("Manager".equals(selectedType)) {
            extraLabel1.setText("Команда (осіб):");
            extraField1.setText("4");
            extraLabel2.setText("Проєкт:");
            extraField2.setText("Phoenix");
        } else if ("Intern".equals(selectedType)) {
            extraLabel1.setText("Університет:");
            extraField1.setText("СумДУ");
            extraLabel2.setText("Ментор:");
            extraField2.setText("Ярослав Мудрий");
        }
    }

    private void handleAddEmployee() {
        try {
            String firstName = firstNameField.getText();
            String lastName = lastNameField.getText();
            String position = positionField.getText();
            double salary = Double.parseDouble(salaryField.getText().trim().replace(',', '.'));
            int age = Integer.parseInt(ageField.getText().trim());
            Department department = departmentBox.getValue();
            int quantity = Integer.parseInt(quantityField.getText().trim());

            String selectedType = typeBox.getValue();
            Employee created;
            if ("FullTimeEmployee".equals(selectedType)) {
                double bonus = Double.parseDouble(extraField1.getText().trim().replace(',', '.'));
                int vacation = Integer.parseInt(extraField2.getText().trim());
                created = new FullTimeEmployee(firstName, lastName, position, salary, age, department, bonus, vacation);
            } else if ("ContractEmployee".equals(selectedType)) {
                int months = Integer.parseInt(extraField1.getText().trim());
                String agency = extraField2.getText();
                created = new ContractEmployee(firstName, lastName, position, salary, age, department, months, agency);
            } else if ("Manager".equals(selectedType)) {
                int teamSize = Integer.parseInt(extraField1.getText().trim());
                String project = extraField2.getText();
                created = new Manager(firstName, lastName, position, salary, age, department,
                        5000.0, 28, teamSize, project);
            } else {
                String university = extraField1.getText();
                String mentor = extraField2.getText();
                created = new Intern(firstName, lastName, position, salary, age, department,
                        university, 3, mentor);
            }

            company.addEmployee(created, quantity);
            storage.saveCompany(company);
            refreshCollectionView();

            uuidSearchField.setText(created.getUuid().toString());
            addStatusLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #15803d;");
            addStatusLabel.setText("Додано! Автоматичний UUID:\n" + created.getUuid());

            firstNameField.clear();
            lastNameField.clear();
            positionField.clear();
            salaryField.clear();
            ageField.clear();
        } catch (NumberFormatException ex) {
            addStatusLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #b91c1c;");
            addStatusLabel.setText("Помилка: перевірте числові поля (зарплата, вік, кількість).");
        } catch (IllegalArgumentException ex) {
            addStatusLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #b91c1c;");
            addStatusLabel.setText("Помилка: " + ex.getMessage());
        }
    }

    private VBox buildCollectionPane() {
        VBox box = new VBox(8);
        box.setPrefWidth(430);
        box.setPadding(new Insets(10));
        box.setStyle("-fx-border-color: #cbd5e1; -fx-border-radius: 6; -fx-background-color: #ffffff;");

        Label title = new Label("2. Колекція об'єктів (короткий вивід)");
        title.setStyle("-fx-font-size: 13px; -fx-font-weight: bold;");

        summaryLabel = new Label();
        summaryLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #475569;");

        collectionListView = new ListView<String>(shortItems);
        VBox.setVgrow(collectionListView, Priority.ALWAYS);

        collectionListView.getSelectionModel().selectedIndexProperty().addListener((obs, oldIdx, newIdx) -> {
            int idx = newIdx.intValue();
            ArrayList<Employee> list = company.getEmployees();
            if (idx >= 0 && idx < list.size()) {
                uuidSearchField.setText(list.get(idx).getUuid().toString());
            }
        });

        Label hint = new Label("Підказка: натисніть на елемент списку, щоб підставити його UUID у пошук.");
        hint.setWrapText(true);
        hint.setStyle("-fx-font-size: 11px; -fx-text-fill: #64748b;");

        box.getChildren().addAll(title, summaryLabel, collectionListView, hint);
        return box;
    }

    private void refreshCollectionView() {
        shortItems.clear();
        ArrayList<Employee> list = company.getEmployees();
        for (int i = 0; i < list.size(); i++) {
            Employee emp = list.get(i);
            shortItems.add(emp.toShortString());
        }
        summaryLabel.setText("Компанія: " + company.getName()
                + " | Унікальних позицій: " + company.getUniqueEmployeesCount()
                + " | Всього працівників: " + company.getTotalHeadcount());
    }

    private VBox buildSearchPane() {
        VBox box = new VBox(8);
        box.setPrefWidth(400);
        box.setPadding(new Insets(10));
        box.setStyle("-fx-border-color: #cbd5e1; -fx-border-radius: 6; -fx-background-color: #f8fafc;");

        Label title = new Label("3. Пошук об'єкта за UUID");
        title.setStyle("-fx-font-size: 13px; -fx-font-weight: bold;");

        uuidSearchField = new TextField();
        uuidSearchField.setPromptText("Введіть UUID (напр. 550e8400-e29b-41d4-a716-446655440000)");
        HBox.setHgrow(uuidSearchField, Priority.ALWAYS);

        Button findButton = new Button("Знайти");
        findButton.setStyle("-fx-font-weight: bold;");
        findButton.setOnAction(event -> handleSearchByUuid());

        HBox searchRow = new HBox(8, uuidSearchField, findButton);
        searchRow.setAlignment(Pos.CENTER_LEFT);

        searchStatusLabel = new Label("Введіть UUID та натисніть «Знайти».");
        searchStatusLabel.setWrapText(true);
        searchStatusLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #334155;");

        detailsArea = new TextArea();
        detailsArea.setEditable(false);
        detailsArea.setWrapText(true);
        detailsArea.setPromptText("Тут відобразиться повна інформація про знайдений об'єкт...");
        VBox.setVgrow(detailsArea, Priority.ALWAYS);

        box.getChildren().addAll(title, new Separator(), searchRow, searchStatusLabel, detailsArea);
        return box;
    }

    private void handleSearchByUuid() {
        String rawUuid = uuidSearchField.getText();
        try {
            Employee found = company.searchByUuid(rawUuid);
            if (found == null) {
                searchStatusLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #b45309; -fx-font-weight: bold;");
                searchStatusLabel.setText("Об'єкт з UUID «" + rawUuid.trim() + "» не знайдено.");
                detailsArea.clear();
                return;
            }

            searchStatusLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #15803d; -fx-font-weight: bold;");
            searchStatusLabel.setText("Об'єкт успішно знайдено!");
            detailsArea.setText(formatFullEmployeeDetails(found));
        } catch (IllegalArgumentException ex) {
            searchStatusLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #b91c1c; -fx-font-weight: bold;");
            searchStatusLabel.setText("Помилка формату UUID: " + ex.getMessage());
            detailsArea.clear();
        }
    }

    private String formatFullEmployeeDetails(Employee e) {
        StringBuilder sb = new StringBuilder();
        sb.append("UUID: ").append(e.getUuid()).append("\n");
        sb.append("Тип працівника: ").append(e.getEmployeeType())
                .append(" (").append(e.getClass().getSimpleName()).append(")\n");
        sb.append("Ім'я: ").append(e.getFirstName()).append("\n");
        sb.append("Прізвище: ").append(e.getLastName()).append("\n");
        sb.append("Посада: ").append(e.getPosition()).append("\n");
        sb.append("Відділ: ").append(e.getDepartment().getTitle())
                .append(" (").append(e.getDepartment().name()).append(")\n");
        sb.append("Вік: ").append(e.getAge()).append("\n");
        sb.append(String.format("Базова зарплата: %.2f%n", e.getSalary()));
        sb.append(String.format("Місячна виплата на 1 працівника: %.2f%n", e.calculateMonthlyPay()));
        sb.append("Кількість у штаті компанії: ").append(company.getEmployeeQuantity(e)).append("\n");

        if (e instanceof Manager) {
            Manager m = (Manager) e;
            sb.append(String.format("Щомісячна премія: %.2f%n", m.getBonus()));
            sb.append("Днів відпустки: ").append(m.getVacationDays()).append("\n");
            sb.append("Розмір команди: ").append(m.getTeamSize()).append("\n");
            sb.append("Назва проєкту: ").append(m.getProjectName()).append("\n");
            sb.append(String.format("Надбавка за керування: %.2f%n", m.getManagementAllowance()));
        } else if (e instanceof FullTimeEmployee) {
            FullTimeEmployee f = (FullTimeEmployee) e;
            sb.append(String.format("Щомісячна премія: %.2f%n", f.getBonus()));
            sb.append("Днів відпустки: ").append(f.getVacationDays()).append("\n");
        } else if (e instanceof ContractEmployee) {
            ContractEmployee c = (ContractEmployee) e;
            sb.append("Строк контракту (міс.): ").append(c.getContractMonths()).append("\n");
            sb.append("Агенція: ").append(c.getAgency()).append("\n");
            sb.append(String.format("Загальна вартість контракту: %.2f%n", c.getTotalContractCost()));
        } else if (e instanceof Intern) {
            Intern in = (Intern) e;
            sb.append("Університет: ").append(in.getUniversity()).append("\n");
            sb.append("Тривалість стажування (міс.): ").append(in.getInternshipMonths()).append("\n");
            sb.append("Ментор: ").append(in.getMentorName()).append("\n");
            sb.append(String.format("Виплата за стажування: %.2f%n", in.getTotalStipend()));
        }
        return sb.toString();
    }
}