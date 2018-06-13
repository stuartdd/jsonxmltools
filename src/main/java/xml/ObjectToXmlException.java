package xml;
/**
 *
  * @author - Stuartdd
 */
public class ObjectToXmlException extends RuntimeException {

    public ObjectToXmlException(String string) {
        super(string);
    }

    public ObjectToXmlException(String string, Throwable thrwbl) {
        super(string, thrwbl);
    }
    
}
