import java.util.ArrayList;

public interface DataStorage {

    ArrayList<Employee> load();

    void save(ArrayList<Employee> employees);

    String getFileName();
}