package xml;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.nio.charset.Charset;

/**
 * Created by Stuart on 1/27/15.
 * <p/>
 * Generates a name value pair structure of Map<String, List<Composite>> map,
 * much like a DOM, only easier to navigate and search.
 */
public class MappedXml extends DefaultHandler {

    private StringBuilder chars = new StringBuilder();
    private Map<String, List<Composite>> map = new HashMap<>();
    private XNodes nodes;

    private final KeyValuePair keyValuePair;
    private String previousLocalName = "";
    private String previousKey = "";

    public MappedXml(String xml, KeyValuePair keyValuePair) {

        this.keyValuePair = (keyValuePair != null) ? keyValuePair : KeyValuePair.createDefault();

        Charset charset = Charset.forName("UTF-8");
        if ((xml == null) || (xml.trim().length() == 0)) {
            throw new ParseXmlException("Failed to parse XML. Input is null or empty");
        }
        ByteArrayInputStream stream = null;
        try {
            SAXParserFactory spf = SAXParserFactory.newInstance();
            spf.setNamespaceAware(true);
            SAXParser parser = spf.newSAXParser();
            
            stream = new ByteArrayInputStream(xml.getBytes(charset));
            parser.parse(stream, this);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new ParseXmlException("Failed to parse response", e);
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    throw new ParseXmlException("Could not close input stream", e); // NOSONAR no way around this one!
                }
            }
        }
    }

    @Override
    public void startDocument() throws SAXException {
        map.clear();
        chars.setLength(0);
        nodes = new XNodes();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        chars.setLength(0);
        nodes.push(uri, qName, localName, attributes);
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (chars.length() > 0) {
            String value = chars.toString().trim();
            if ((value.length() > 0) || (nodes.getLast().getAttributes() != null)) {
                String key = nodes.getPath();
                List<Composite> v = map.get(key);
                if (v == null) {
                    v = new ArrayList<>();
                    map.put(key, v);
                }

                // Adds an empty value element if it is missing from the XML (when processing parallel arrays)
                addMissingValueInArray(localName);

                v.add(new Composite(value, nodes.getLast().getAttributes()));

                chars.setLength(0);

                previousKey = key;
                previousLocalName = localName;
            }
        }
        nodes.pop();
    }

    private void addMissingValueInArray(String localName) {

        if(previousLocalName.equals(keyValuePair.getKeyName()) && !localName.equals(keyValuePair.getValueName()) ) {
            String keyOfValue = previousKey.substring(0, previousKey.toCharArray().length - keyValuePair.getKeyName().length()) + keyValuePair.getValueName();
            List<Composite> listOfValues = map.get(keyOfValue);
            if (listOfValues == null) {
                listOfValues = new ArrayList<>();
                map.put(keyOfValue, listOfValues);
            }
            listOfValues.add(new Composite("", null));
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        chars.append(ch, start, length);
    }

    public Map<String, String> getMap() {
        Map<String, String> resp = new HashMap<>();
        for (Map.Entry<String, List<Composite>> ent : map.entrySet()) {
            int size = ent.getValue().size();
            switch (size) {
                case 1:
                    if (ent.getValue().get(0).getValue().trim().length() > 0) {
                        resp.put(ent.getKey(), ent.getValue().get(0).getValue());
                    }
                    if (ent.getValue().get(0).getAttributes() != null) {
                        addAttributes(resp, ent.getKey(), ent.getValue().get(0).getAttributes());
                    }
                    break;
                case 0:
                    break;
                default:
                    for (int i = 0; i < size; i++) {
                        if (ent.getValue().get(i).getValue().trim().length() > 0) {
                            resp.put(ent.getKey() + "[" + i + "]", ent.getValue().get(i).getValue());
                        }
                        if (ent.getValue().get(0).getAttributes() != null) {
                            addAttributes(resp, ent.getKey() + "[" + i + "]", ent.getValue().get(i).getAttributes());
                        }
                    }
            }
        }
        return resp;
    }

    public String getMapString() {
        Map<String, String> sorted = new TreeMap<>(getMap());
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> ent : sorted.entrySet()) {
            sb.append(ent.getKey()).append('=').append(ent.getValue()).append("\n");
        }
        return sb.toString();
    }

    private void addAttributes(Map<String, String> resp, String key, Map<String, String> attributes) {
        for (Map.Entry<String, String> s : attributes.entrySet()) {
            resp.put(key + ".{" + s.getKey() + "}", s.getValue());
        }
    }

    private static class Composite {

        private final String value;
        private final Map<String, String> attributes;

        public Composite(String value, Map<String, String> attributes) {
            this.value = value;
            if (attributes == null) {
                this.attributes = new HashMap<>();
            } else {
                this.attributes = attributes;
            }
        }

        public String getValue() {
            return value;
        }

        public Map<String, String> getAttributes() {
            return attributes;
        }

    }
}
