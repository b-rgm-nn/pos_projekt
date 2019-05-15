package Enum;


/**
 *
 * @author Matthias
 */
public enum Intent {
    query_single_value("query_single_value"), query_multiple_values("query_multiple_values");
    private String name;

    private Intent(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
