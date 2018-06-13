package xml;

import java.util.HashMap;
import java.util.Map;
import org.xml.sax.Attributes;

/**
 *
 * @author - Stuart Davies
 */
public class XNode {

    private final String uri;
    private final String localName;
    private final String qName;
    private final Map<String, String> attributes;
    private XNode next;
    private XNode prev;

    public XNode() {
        this.uri = null;
        this.localName = null;
        this.qName = null;
        this.attributes = null;
        this.prev = null;
        this.next = null;
    }

    public XNode(String uri, String qName, String localName, Attributes attributes, XNode prev) {
        this.uri = uri;
        this.localName = localName;
        this.qName = qName;
        this.attributes = new HashMap<>();
        if ((attributes != null) && (attributes.getLength() > 0)) {
            for (int i = 0; i < attributes.getLength(); i++) {
                this.attributes.put(attributes.getLocalName(i), attributes.getValue(i));
            }
        }
        this.prev = prev;
    }

    public XNode getNext() {
        return next;
    }

    public XNode setNext(XNode next) {
        this.next = next;
        return next;
    }

    public XNode getPrev() {
        return prev;
    }

    public void setPrev(XNode prev) {
        this.prev = prev;
    }

    public boolean hasPrev() {
        return prev != null;
    }

    public String getUri() {
        return uri;
    }

    public String getLocalName() {
        return localName;
    }

    public String getQName() {
        return qName;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public XNode clear() {
        next = null;
        prev = null;
        return this;
    }

}
