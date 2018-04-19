/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package json;

import config.Config;
import config.ConfigData;
import config.Functions;
import config.JsonConfigException;
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
        System.out.println(config);
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

    @Test(expected = JsonConfigException.class)
    public void testToConfigWrongFile() {
        Config.configFromJsonFile(ConfigData.class, new File("testData/TestConfig.json"));
    }

    @Test(expected = JsonConfigException.class)
    public void testToConfigFileNotFound() {
        Config.configFromJsonFile(ConfigData.class, new File("testData/TestConfigNotFound.json"));
    }

}
