package dev.mv.engine.parsing;

import dev.mv.engine.exceptions.Exceptions;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;

public class XMLParser implements Parser {
    private Element root;
    private NodeList nodeList;

    private Element current;
    private int currentIdx = 0;

    public XMLParser() {}

    public XMLParser(InputStream stream) {
        load(stream);
    }

    public XMLParser(Element root, NodeList nodes) {
        this.root = root;
        this.nodeList = nodes;
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                this.current = (Element) node;
                break;
            }
        }
    }

    @Override
    public void load(InputStream stream) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(stream);
            document.getDocumentElement().normalize();
            root = current = document.getDocumentElement();
            nodeList = null;

        } catch (Exception e) {
            Exceptions.send(e);
        }
    }

    @Override
    public String root() {
        return root.getTagName();
    }

    @Override
    public boolean advance() {
        if (nodeList == null) return false;
        if (currentIdx + 1 >= nodeList.getLength()) return false;
        while (true) {
            Node node = nodeList.item(currentIdx++);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                current = (Element) node;
                break;
            }
        }

        return true;
    }

    @Override
    public int count() {
        return nodeList.getLength();
    }

    @Override
    public Parser inner() {
        return new XMLParser(
                root,
                current.getChildNodes()
        );
    }

    @Override
    public boolean hasInner() {
        return current.hasChildNodes();
    }

    @Override
    public String text() {
        return current.getTextContent();
    }

    @Override
    public String current() {
        return current.getTagName();
    }

    @Override
    public String attrib(String name) {
        if (!current.hasAttribute(name)) return null;
        return current.getAttribute(name);
    }

    @Override
    public Parser copy() {
        XMLParser parser = new XMLParser();
        parser.currentIdx = currentIdx;
        parser.current = current;
        parser.root = root;
        parser.nodeList = nodeList;

        return parser;
    }
}
