package xml;
/**
 *
  * @author - Stuartdd
 */
public class XmlToObjectException extends RuntimeException {

    public XmlToObjectException(String string) {
        super(string);
    }

    public XmlToObjectException(String string, Throwable thrwbl) {
        super(string, thrwbl);
    }
    
}
