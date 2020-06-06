package xml;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class XmlUtils {

    private static final ObjectMapper staticObjectMapper = createBasicMapper();
    private static final ObjectMapper staticXmlMapper = createMapper(false, false);
    private static final ObjectMapper staticXmlMapperFormatted = createMapper(true, false);
    private static final ObjectMapper staticXmlMapperIgnoreEmpty = createMapper(false, true);

    /**
     * Convert an object in to a XML String representation
     *
     * @param o - The object to convert
     * @return - The XML String
     */
    public static String toXmlFormatted(Object o) {

        try {
            return staticXmlMapperFormatted.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            throw new ObjectToXmlException(o.toString(), e);
        }
    }
    
    /**
     * Convert an object in to a XML String representation
     *
     * @param o - The object to convert
     * @return - The XML String
     */
    public static String toXml(Object o, String rootName) {
        try {
            return staticXmlMapper.writer().withRootName(rootName).writeValueAsString(o);
        } catch (JsonProcessingException e) {
            throw new ObjectToXmlException(o.toString(), e);
        }
    }

    public static String toXml(Object o) {
        return toXml(o, false);
    }

    /**
     * Convert an object in to a XML String representation
     *
     * @param o - The object to convert
     * @param ignoreEmpty - pass true if empty["" and NULL] fields needs to be
     * skipped during Serialization
     * @return - The XML String
     */
    public static String toXml(Object o, boolean ignoreEmpty) {
        try {
            if (ignoreEmpty) {
                return staticXmlMapperIgnoreEmpty.writeValueAsString(o);
            }
            return staticXmlMapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            throw new ObjectToXmlException(o.toString(), e);
        }
    }

    /**
     * Convert a XML string in to an object of a given class
     *
     * @param o - The Class to create and populate from the XML
     * @param xml - The XML
     * @return - The fully populated object
     */
    public static Object toObject(Class o, String xml) {
        try {
            return staticObjectMapper.readValue(xml, o);
        } catch (IOException e) {
            throw new XmlToObjectException(o.toString(), e);
        }
    }

    public static <T extends Object> T toObject(TypeReference<T> type, String xml) {
        try {
            return staticObjectMapper.readValue(xml, type);
        } catch (IOException e) {
            throw new XmlToObjectException(type.getType().getClass().getName(), e);
        }
    }

    public static ObjectMapper createMapper(boolean formatted, boolean ignoreEmpty) {
        XmlMapper xmlMapper = createBasicMapper();
        xmlMapper.configure(JsonGenerator.Feature.AUTO_CLOSE_TARGET, false);
        xmlMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        if (formatted) {
            xmlMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        }
        if (ignoreEmpty) {
            xmlMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        }
        return xmlMapper;
    }

    public static XmlMapper createBasicMapper() {
        XmlMapper xmlMapper = new XmlMapper();
        xmlMapper.registerModule(new JodaModule());
        return xmlMapper;
    }


}
