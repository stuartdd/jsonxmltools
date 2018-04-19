package json;
/**
 *
  * @author - Stuartdd
 */
public class JsonToObjectException extends RuntimeException {

    public JsonToObjectException(String string) {
        super(string);
    }

    public JsonToObjectException(String string, Throwable thrwbl) {
        super(string, thrwbl);
    }
    
}
