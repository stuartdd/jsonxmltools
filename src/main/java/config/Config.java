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
        return returnValidated(cfg);
    }

    public static Object configFromJsonFile(Class clazz, File json) {
        Object cfg;
        try {
            cfg = JsonUtils.beanFromJson(clazz, json);
        } catch (JsonToObjectException ex) {
            ex.printStackTrace();
            throw new JsonConfigException("Config load Failed:" + ex.getMessage(), ex);
        }
        return returnValidated(cfg);
    }

    public static Object configFromJsonStream(Class clazz, InputStream json) {
        Object cfg;
        try {
            cfg = JsonUtils.beanFromJson(clazz, json);
        } catch (JsonToObjectException ex) {
            throw new JsonConfigException("Config load Failed:" + ex.getMessage(), ex);
        }
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
}
