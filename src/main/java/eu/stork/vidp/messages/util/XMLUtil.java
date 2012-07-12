/**
 * 
 */
package eu.stork.vidp.messages.util;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Helper class for XML processing
 * @author bzwattendorfer
 *
 */
public class XMLUtil {
	
	/**
	 * Transforms a string representation to a DOM representation
	 * @param xmlString XML as string
	 * @return DOM representation of String
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public static Element stringToDOM(String xmlString) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
	   
	    DocumentBuilder builder = dbf.newDocumentBuilder();
		
		Reader reader = new StringReader(xmlString);
		InputSource src = new InputSource(reader);
		Document domDoc = builder.parse(src);
		return domDoc.getDocumentElement();
	}
   
	/**
	 * Creates a new and empty XML document
	 * @return New XML document
	 * @throws ParserConfigurationException
	 */
   public static Document createNewDocument() throws ParserConfigurationException {
	   DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
	   
	    DocumentBuilder builder = dbf.newDocumentBuilder();
	    return builder.newDocument();
   }
   
   /**
    * Transforms an XML to a String
    * @param node XML node
    * @return String represenation of XML
    */
   public static String printXML(Node node) {
        TransformerFactory tfactory = TransformerFactory.newInstance();
        Transformer serializer;
        try {
            serializer = tfactory.newTransformer();
            
            serializer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            serializer.setOutputProperty(OutputKeys.ENCODING,"UTF-8");
            
            StringWriter output = new StringWriter();
            serializer.transform(new DOMSource(node), new StreamResult(output));
            return output.toString();
        } catch (TransformerException e) {
            
            throw new RuntimeException(e);
        }
    }
	
   /**
    * Writes an XML element to a given file
    * @param doc XML element
    * @param filename Filename of the file where to write XML
    */
	public static void writeXmlFile(Element doc, String filename) { 
		try { 
			 
			Source source = new DOMSource(doc); 				
			File file = new File(filename); 
			Result result = new StreamResult(file); 

			Transformer xformer = TransformerFactory.newInstance().newTransformer(); 
			xformer.transform(source, result); 
			} catch (Exception e) { 
				throw new RuntimeException(e);
			}  
	} 
	
	/**
	 * Gets the first text value of a NodeList
	 * @param nList NodeList
	 * @return first text value of a NodeList
	 */
	public static String getFirstTextValueFromNodeList(NodeList nList) {
		if (nList != null && nList.getLength() != 0) {
			return nList.item(0).getTextContent();
		}
		return null;
	}
	
	/**
	 * Gets the first element of a Node
	 * @param parent Node
	 * @return first element of a Node
	 */
	public static Element getFirstElement(Node parent) {
	    Node n = parent.getFirstChild();
	    while (n != null &&  n.getNodeType() !=  Node.ELEMENT_NODE) {
	        n = n.getNextSibling();
	    }
	    if (n == null) {
	        return null;
	    }
	    return (Element)n;
	}
	


}
