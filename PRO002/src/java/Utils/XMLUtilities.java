/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author Hau
 */
public class XMLUtilities implements Serializable {
    public static Document parseDomFromFile(String xmlFilePath) 
            throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        
        DocumentBuilder builder = factory.newDocumentBuilder();
        
        Document doc = builder.parse(xmlFilePath);
        
        return doc;
    }
    
    
    public static XPath getXpath() {
        XPathFactory factory = XPathFactory.newInstance();
        XPath xpath = factory.newXPath();
        return xpath;
    }
    
    
    public static void transformDOMToStream(Node node, String xmlOutputFilePath) 
            throws TransformerConfigurationException, TransformerException {
        Source src = new DOMSource(node);
        
        File file = new File(xmlOutputFilePath);
        Result rs = new StreamResult(file);
        
        TransformerFactory transFac = TransformerFactory.newInstance();
        
        Transformer transformer = transFac.newTransformer();
        
        
        transformer.transform(src, rs);
    }
    
    public static Element createNode(Document doc, String elementName,
            String textContent, Map<String, String> attributes) {
        if (doc != null) {
            Element result = doc.createElement(elementName);
            
            if (attributes != null) {
                for (Map.Entry<String, String> attr : attributes.entrySet()) {
                    result.setAttribute(attr.getKey(), attr.getValue());
                }
            }
            
            if (textContent != null) {
                result.setTextContent(textContent);
            }
            
            return result;
        }
        
        return null;
    }
    
    public static void parseFileToSAX(String xmlFilePath, DefaultHandler handler) 
            throws ParserConfigurationException, SAXException, IOException {
        SAXParserFactory parserFac = SAXParserFactory.newInstance();
        SAXParser parser = parserFac.newSAXParser();

        parser.parse(xmlFilePath, handler);
    }
    
    
    public static XMLStreamReader parseFileToStAXCursor(InputStream is) 
            throws XMLStreamException {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        
        XMLStreamReader reader = factory.createXMLStreamReader(is);
        
        return reader;
    }
    
    
    public static String getAttributeValue(String elementName,
            String namespaceURI,
            String attributeName, XMLStreamReader reader) 
            throws XMLStreamException {
        if (reader == null) {
            return null;
        }
        
        while (reader.hasNext()) {
            int cursor = reader.getEventType();
            if (cursor == XMLStreamConstants.START_ELEMENT) {
                String tagName = reader.getLocalName();
                if (tagName.equals(elementName)) {
                    String result = reader.getAttributeValue(namespaceURI, 
                            attributeName);
                    return result;
                }
            }
            reader.next();
        }
        
        return null;
    }
    
    
    public static String getTextContent(String elementName, XMLStreamReader reader)
            throws XMLStreamException {
        if (reader == null) {
            return null;
        }
        
        while (reader.hasNext()) {
            int cursor = reader.getEventType();
            if (cursor == XMLStreamConstants.START_ELEMENT) {
                String tagName = reader.getLocalName();
                if (tagName.equals(elementName)) {
                    reader.next();
                    String result = reader.getText();
                    reader.nextTag(); // go to end Element;
                    
                    return result;
                }
            }
            reader.next();
        }
        
        return null;
    }
    
    
    
    
}

