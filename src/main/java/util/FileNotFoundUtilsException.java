/*
 * Author Stuart Davies (802996013)
 * Local Test Server development 15/09/2017
 * BT eCommerce 
 */
package util;

/**
 * Used by FileUtils to report and exception
 * 
 * @author - Stuart Davies
 */
public class FileNotFoundUtilsException extends FileUtilsException {

    public FileNotFoundUtilsException(String string) {
        super(string);
    }

    public FileNotFoundUtilsException(String string, Throwable thrwbl) {
        super(string, thrwbl);
    }

}
