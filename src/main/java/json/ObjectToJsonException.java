package json;
/**
 *
  * @author - Stuartdd
 */
public class ObjectToJsonException extends RuntimeException {

    public ObjectToJsonException(String string) {
        super(string);
    }

    public ObjectToJsonException(String string, Throwable thrwbl) {
        super(string, thrwbl);
    }
    
}
