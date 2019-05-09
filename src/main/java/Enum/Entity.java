package Enum;


/**
 *
 * @author Matthias
 */
public enum Entity {
    date("sys-date"), time("sys-time"), company("company");

    private String name;

    private Entity(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
