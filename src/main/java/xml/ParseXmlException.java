package xml;
/**
 *
  * @author - Stuart Davies
 */
public class ParseXmlException extends RuntimeException {

    public ParseXmlException(String string) {
        super(string);
    }

    public ParseXmlException(String string, Throwable thrwbl) {
        super(string, thrwbl);
    }
    
}
