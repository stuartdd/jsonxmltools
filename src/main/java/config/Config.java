package config;

import java.io.File;
import java.io.InputStream;
import json.JsonToObjectException;
import json.JsonUtils;

/**
 *
 * @author Stuartdd
 */
public class Config {

    public static Object configFromJson(Class clazz, String json) {
        Object cfg;
        try {
            cfg = JsonUtils.beanFromJson(clazz, json);
        } catch (JsonToObjectException ex) {
            throw new JsonConfigException("Config load Failed:" + ex.getMessage(), ex);
        }
        loaded(cfg);
        return returnValidated(cfg);
    }

    public static Object configFromJsonFile(Class clazz, File jsonFile) {
        Object cfg;
        try {
            cfg = JsonUtils.beanFromJson(clazz, jsonFile);
        } catch (JsonToObjectException ex) {
            throw new JsonConfigException("Config load Failed:" + ex.getMessage(), ex);
        }
        loaded(cfg);
        return returnValidated(cfg);
    }
    
    public static Object configFromJsonFile(Class clazz, String jsonFileName) {
        File jsonFile = new File(jsonFileName);
        if (jsonFile.exists()) {
            return configFromJsonFile(clazz, jsonFile);
        }
        throw new JsonConfigException("JSON File "+jsonFile.getAbsolutePath()+" does not exist");
    }

    public static Object configFromJsonStream(Class clazz, InputStream json) {
        Object cfg;
        try {
            cfg = JsonUtils.beanFromJson(clazz, json);
        } catch (JsonToObjectException ex) {
            throw new JsonConfigException("Config load Failed:" + ex.getMessage(), ex);
        }
        loaded(cfg);
        return returnValidated(cfg);
    }

    private static Object returnValidated(Object cfg) {
        if (cfg instanceof Validatable) {
            String reason = ((Validatable) cfg).validate(cfg);
            if (reason != null) {
                throw new JsonConfigException("Configuration validation failed. Reason:" + reason);
            }
        }
        return cfg;
    }
    
    private static void loaded(Object cfg) {
        if (cfg instanceof Loadable) {
            ((Loadable) cfg).loaded(cfg);
         }
    }
}
