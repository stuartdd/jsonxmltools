package json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ValueNode;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author Stuartdd
 */
public class JsonUtils {

    private final static ObjectMapper OBJECT_MAPPER = createMapper(false, false);
    private final static ObjectMapper OBJECT_MAPPER_MIN = createMapper(false, true);
    private final static ObjectMapper OBJECT_MAPPER_FORMATTED = createMapper(true, false);

    /**
     * Create an object from some JSON
     *
     * MyBean myBean = (MyBean) JsonUtils.beanFromJson(MyBean.class, TEST_JSON);
     *
     * @param beanType The class of the object
     * @param json The string containing the JSON
     * @return The object (will need casting)
     */
    public static Object beanFromJson(Class beanType, String json) {
        if (json == null) {
            throw new JsonToObjectException("Json cannot be null");
        }
        try {
            return OBJECT_MAPPER.readValue(json, beanType);
        } catch (IOException ex) {
            throw new JsonToObjectException("Failed to parse JSON to a Bean Object", ex);
        }
    }

    /**
     * Create an object from some JSON in a stream
     *
     * MyBean myBean = (MyBean) JsonUtils.beanFromJson(MyBean.class,
     * MyBean.class.getResource("/TestConfig.json").openStream());
     *
     * @param beanType The class of the object
     * @param jsonStream The Json stream
     * @return The object (will need casting)
     */
    public static Object beanFromJson(Class beanType, InputStream jsonStream) {
        if (jsonStream == null) {
            throw new JsonToObjectException("Json stream cannot be null");
        }
        try {
            return OBJECT_MAPPER.readValue(jsonStream, beanType);
        } catch (IOException ex) {
            throw new JsonToObjectException("Failed to parse JSON stream to a Bean Object", ex);
        }
    }

    /**
     * Create an object from some JSON in a file
     *
     * MyBean myBean = (MyBean) JsonUtils.beanFromJson(MyBean.class, new
     * File("testData/TestConfig.json"));
     *
     * @param beanType The class of the object
     * @param jsonFile The file containing the JSON
     * @return The object (will need casting)
     */
    public static Object beanFromJson(Class beanType, File jsonFile) {
        if (jsonFile == null) {
            throw new JsonToObjectException("Json file cannot be null");
        }
        try {
            return OBJECT_MAPPER.readValue(jsonFile, beanType);
        } catch (IOException ex) {
            throw new JsonToObjectException("Failed to parse JSON File [" + jsonFile.getAbsolutePath() + "] to a Bean Object", ex);
        }
    }

    /**
     * Create an Map from some JSON
     *
     * Map<String, Object> map = JsonUtils.mapFromJson(MyBean.class, TEST_JSON);
     *
     * @param json The string containing the JSON
     * @return The map
     */
    public static Map<String, Object> mapFromJson(String json) {
        if (json == null) {
            throw new JsonToObjectException("Json cannot be null");
        }
        try {
            return OBJECT_MAPPER.readValue(json, Map.class);
        } catch (IOException ex) {
            throw new JsonToObjectException("Failed to parse JSON to a map", ex);
        }
    }

    /**
     * Create an Map from some JSON from a file
     *
     * Map<String, Object> map = JsonUtils.mapFromJson(MyBean.class, TEST_JSON);
     *
     * @param jsonFile The File containing the JSON
     * @return The map
     */
    public static Map<String, Object> mapFromJson(File jsonFile) {
        if (jsonFile == null) {
            throw new JsonToObjectException("jsonFile cannot be null");
        }
        try {
            return OBJECT_MAPPER.readValue(jsonFile, Map.class);
        } catch (IOException ex) {
            throw new JsonToObjectException("Failed to parse JSON to a map", ex);
        }
    }

    /**
     * Create an Map from some JSON via a stream
     *
     * Map<String, Object> map = JsonUtils.mapFromJson(MyBean.class, TEST_JSON);
     *
     * @param jsonStream The Stream containing the JSON
     * @return The map
     */
    public static Map<String, Object> mapFromJson(InputStream jsonStream) {
        if (jsonStream == null) {
            throw new JsonToObjectException("jsonStream cannot be null");
        }
        try {
            return OBJECT_MAPPER.readValue(jsonStream, Map.class);
        } catch (IOException ex) {
            throw new JsonToObjectException("Failed to parse JSON Stream to a map", ex);
        }
    }

    public static String toJsonFormatted(Object bean) {
        try {
            return OBJECT_MAPPER_FORMATTED.writeValueAsString(bean);
        } catch (IOException ex) {
            throw new ObjectToJsonException("Failed to convert object of type [" + bean.getClass().getName() + "] to JSON String", ex);
        }
    }

    public static String toJson(Object bean) {
        try {
            return OBJECT_MAPPER.writeValueAsString(bean);
        } catch (IOException ex) {
            throw new ObjectToJsonException("Failed to convert object of type [" + bean.getClass().getName() + "] to JSON String", ex);
        }
    }

    public static String toJsonMin(Object bean) {
        try {
            return OBJECT_MAPPER_MIN.writeValueAsString(bean);
        } catch (IOException ex) {
            throw new ObjectToJsonException("Failed to convert object of type [" + bean.getClass().getName() + "] to JSON String", ex);
        }
    }
    
    public static Map<String, String> flatMap(String json) {
        Map<String, String> map = new HashMap<>();
        try {
            addKeys("", new ObjectMapper().readTree(json), map);
        } catch (IOException e) {
            throw new JsonToObjectException("Could not flatten JSON text",e);
        }
        return map;
    }

    private static void addKeys(String currentPath, JsonNode jsonNode, Map<String, String> map) {
        if (jsonNode.isObject()) {
            ObjectNode objectNode = (ObjectNode) jsonNode;
            Iterator<Map.Entry<String, JsonNode>> iter = objectNode.fields();
            String pathPrefix = currentPath.isEmpty() ? "" : currentPath + ".";

            while (iter.hasNext()) {
                Map.Entry<String, JsonNode> entry = iter.next();
                addKeys(pathPrefix + entry.getKey(), entry.getValue(), map);
            }
        } else if (jsonNode.isArray()) {
            ArrayNode arrayNode = (ArrayNode) jsonNode;
            for (int i = 0; i < arrayNode.size(); i++) {
                addKeys(currentPath + "[" + i + "]", arrayNode.get(i), map);
            }
        } else if (jsonNode.isValueNode()) {
            ValueNode valueNode = (ValueNode) jsonNode;
            map.put(currentPath, valueNode.asText());
        }
    }

    private static ObjectMapper createMapper(boolean formatted, boolean ignoreEmpty) {
        ObjectMapper jsonMapper = new ObjectMapper();
        jsonMapper.configure(JsonGenerator.Feature.AUTO_CLOSE_TARGET, false);
        if (formatted) {
            jsonMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        }
        if (ignoreEmpty) {
            jsonMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        }
        return jsonMapper;
    }

}
