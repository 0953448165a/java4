
public enum Department {

    
    IT("ІТ"),

   
    HR("Кадри"),

    
    FINANCE("Фінанси"),

    
    SALES("Продажі"),

   
    SUPPORT("Підтримка");

    private final String title;

    
    Department(String title) {
        this.title = title;
    }

    
    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return title;
    }
}