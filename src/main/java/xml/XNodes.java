package xml;

import org.xml.sax.Attributes;

/**
 *
 * @author - Stuart Davies
 */
public class XNodes {

    XNode root;
    XNode last;

    public XNodes() {
        root = new XNode();
        last = root;
    }

    
    public XNode getRoot() {
        return root;
    }

    public String getPath() {
        StringBuilder sb = new StringBuilder();
        XNode n = last;
        while (n.hasPrev()) {
            sb.insert(0,n.getLocalName()+".");
            n = n.getPrev();
        }
        if (sb.length() > 0) {
            sb.setLength(sb.length()-1);
        }
        return sb.toString();
    }
    
    public XNode getLast() {
        return last;
    }

    public XNode push(String uri, String qName, String localName, Attributes attributes) {
        XNode n = new XNode(uri, qName, localName, attributes, last);
        last.setNext(n);
        last = n;
        return n;
    }

    public XNode pop() {
        XNode n = last;
        if (n.hasPrev()) {
            last = n.getPrev();
            last.setNext(null);
            return n.clear();
        }
        return null;
    }

}
