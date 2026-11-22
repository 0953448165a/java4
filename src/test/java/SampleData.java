import java.util.ArrayList;

class SampleData {

    ArrayList<Employee> employees() {
        ArrayList<Employee> list = new ArrayList<Employee>();
        list.add(new FullTimeEmployee("Ярослав", "Мудрий", "Розробник", 30000, 25, Department.IT, 5000, 24));
        list.add(new ContractEmployee("Олег", "Бойко", "Тестувальник", 20000, 30, Department.SUPPORT, 6, "TechStaff"));
        list.add(new Manager("Марія", "Коваль", "Керівник відділу", 40000, 38, Department.IT, 10000, 28, 5, "Alpha"));
        list.add(new Intern("Андрій", "Шевченко", "Стажист", 12000, 20, Department.IT, "СумДУ", 3, "Ярослав Мудрий"));
        return list;
    }

    Company company() {
        Company company = new Company("TechNova Ukraine");
        ArrayList<Employee> list = employees();
        company.addEmployee(list.get(0), 2);
        company.addEmployee(list.get(1), 1);
        company.addEmployee(list.get(2), 1);
        company.addEmployee(list.get(3), 3);
        return company;
    }

    Manager managerWithSpecialChars() {
        return new Manager("Марія", "Коваль", "Керівник відділу", 40000, 38, Department.IT,
                10000, 28, 5, "Alpha; v=2\\beta");
    }
}