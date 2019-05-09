
/**
 *
 * @author Matthias
 */
public enum Intent {
    query_single_value("query_single_value");
    private String name;

    private Intent(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
