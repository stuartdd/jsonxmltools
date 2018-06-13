package xml;

public class KeyValuePair {

    private final static String DEFAULT_KEY_NAME = "name";
    private final static String DEFAULT_VALUE_NAME = "value";

    private final String keyName;
    private final String valueName;

    public KeyValuePair(String keyName, String valueName) {
        this.keyName = keyName;
        this.valueName = valueName;
    }

    public static KeyValuePair createDefault() {
        return new KeyValuePair(DEFAULT_KEY_NAME, DEFAULT_VALUE_NAME);
    }

    public String getKeyName() {
        return keyName;
    }

    public String getValueName() {
        return valueName;
    }
}
