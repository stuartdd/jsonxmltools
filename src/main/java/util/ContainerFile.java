/*
 * Author Stuart Davies (802996013)
 * Local Test Server development 15/09/2017
 * BT eCommerce 
 */
package util;

/**
 * Utility class used by FileUtils for files in a Jar or Zip
 * 
 * @author - Stuart Davies
 */
public class ContainerFile {

    private final String container;
    private final String name;
/**
 * Create with the path (container) and name 
 * 
 * @param container
 * @param name 
 */
    public ContainerFile(String container, String name) {
        this.container = container;
        this.name = name;
    }

    public String getContainer() {
        return container;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "JAR=" + container + " --> " + name;
    }

}
