package config;

/**
 *
 * @author - Stuartdd
 */
public class JsonConfigException extends RuntimeException {

    public JsonConfigException(String string) {
        super(string);
    }

    public JsonConfigException(String string, Throwable thrwbl) {
        super(string, thrwbl);
    }

}
