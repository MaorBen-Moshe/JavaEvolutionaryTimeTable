package utils.infoModels;

public class PropertyInfoObject {
    private final String name;
    private final String value;

    public PropertyInfoObject(String name, String value){
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }
}