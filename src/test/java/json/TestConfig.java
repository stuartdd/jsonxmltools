package json;

import config.Config;
import config.ConfigData;
import config.Functions;
import config.Heating;
import config.JsonConfigException;
import config.Resources;
import java.io.File;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author stuart
 */
public class TestConfig {

    @Test()
    public void testFunctions() {
        Functions functions = (Functions) Config.configFromJsonFile(Functions.class, new File("testData/Functions.json"));
        assertFunctions(functions);
    }

    @Test()
    public void testConfigDataFile() {
        ConfigData config = (ConfigData) Config.configFromJsonFile(ConfigData.class, new File("testData/ConfigDataFile.json"));
        Assert.assertEquals("ConfigDataFile.json", config.getName());
        Assert.assertTrue(config.isValidated());
        assertResources(config.getResources());
        assertFunctions(config.getFunctions());
    }

    private void assertFunctions(Functions f) {
        Assert.assertEquals(false, f.isEchoScriptOutput());
        Assert.assertEquals(200000, f.getPoleForTime());
        List<String> listDs = f.getCommands().get("ds");
        Assert.assertEquals(2, listDs.size());
        Assert.assertEquals("diskStatus", listDs.get(0));
        Assert.assertNotNull(listDs);
        List<String> listLog = f.getCommands().get("log");
        Assert.assertEquals(4, listLog.size());
        Assert.assertEquals("rsyncLogs", listLog.get(0));
        Assert.assertNotNull(listLog);
    }

    private void assertResources(Resources r) {
        assertHeating(r.getHeating());
        Assert.assertEquals(20, r.getHistoryMaxLen());
        Assert.assertEquals("/Server/web", r.getRoot());
        Assert.assertEquals(2, r.getUsers().size());
        Assert.assertEquals("/shares/stuart", r.getUsers().get("stuart"));
        Assert.assertEquals("/shares/shared", r.getUsers().get("shared"));
        Assert.assertEquals(2, r.getLocations().size());
        Assert.assertEquals("/logs", r.getLocations().get("logs"));
        Assert.assertEquals("/logs/cache", r.getLocations().get("cache"));
    }

    private void assertHeating(Heating h) {
        Assert.assertEquals(3600, h.getSyncAfter());
    }

    @Test(expected = JsonConfigException.class)
    public void testToConfigWrongFile() {
        Config.configFromJsonFile(ConfigData.class, new File("testData/TestConfig.json"));
    }

    @Test()
    public void testToConfigFileNotFound() {
        assertException("TestConfigNotFound", "Failed to parse JSON File");
    }

    @Test()
    public void testToConfigFileNoName() {
        assertException("ConfigDataFileNoName", "ConfigData name is null");
    }

    private void assertException(String file, String contains) {
        try {
            Config.configFromJsonFile(ConfigData.class, new File("testData/"+file+".json"));
            Assert.fail();
        } catch (JsonConfigException ce) {
            if (!ce.getMessage().contains(contains)) {
                String error = "Exception ["+ce.getMessage()+"] no:["+contains+"]";
                System.out.println(error);
                Assert.fail(error);
            }
            
        }
    }
}
