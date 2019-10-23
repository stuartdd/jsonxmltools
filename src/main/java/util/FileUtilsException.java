/*
 * Author Stuart Davies (802996013)
 * Local Test Server development 15/09/2017
 * BT eCommerce 
 */
package util;

/**
 * Specific un-checked exception for FileUtils
 * 
 * @author - Stuart Davies
 */
public class FileUtilsException extends RuntimeException {

    public FileUtilsException(String string) {
        super(string);
    }

    public FileUtilsException(String string, Throwable thrwbl) {
        super(string, thrwbl);
    }

}
