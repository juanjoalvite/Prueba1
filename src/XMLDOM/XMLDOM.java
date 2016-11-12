/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package XMLDOM;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.w3c.dom.Element;

/**
 *
 * @author juanj
 */
public final class XMLDOM {

    public File f = new File("src/XMLDOM/EjemploXML.xml");

    public XMLDOM() {

        System.out.println("Mostrar XML:");
        System.out.println(showDoc(openXmlDom(f)));
        System.out.println("-------------------------------------------------------------------");
        
        System.out.println("Modificar XML:");
        appendNode((openXmlDom(f)), "JJ4", "AG4");
        System.out.println(showDoc(openXmlDom(f)));
        System.out.println("-------------------------------------------------------------------");
        
        System.out.println("Eliminar XML:");
        removeNodeDoc((openXmlDom(f)), 3);
        System.out.println(showDoc(openXmlDom(f)));
        System.out.println("-------------------------------------------------------------------");
    }

    public Document openXmlDom(File f) {
        Document doc = null;
        //Create DocumentBuilderFactory object
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        //Don't load comments
        factory.setIgnoringComments(true);
        //Ignore white spaces
        factory.setIgnoringElementContentWhitespace(true);
        //Create DocumentBuilder to load DOM tree into it
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            doc = builder.parse(f);
        } catch (ParserConfigurationException | SAXException | IOException e) {
        }
        return doc;
    }

    public String showDoc(Document doc) {

        String out = "";
        Node n;
        NodeList nodelist = doc.getElementsByTagName("PERSONA");

        //Loop to read every item of node list
        for (int i = 0; i < nodelist.getLength(); i++) {

            //Get set of items inside CD
            n = nodelist.item(i);

            //Check Node type is Element
            if (n.getNodeType() == Node.ELEMENT_NODE) {
                //Cast to Element to use getElementsByTagName method
                Element el = (Element) n;
                //Use .item(0).getAttributes() to get atributes;
                out = out + "Index: " + i + "\r\n";
                out = out + "Nombre: " + el.getElementsByTagName("NOMBRE").item(0).getTextContent() + "\r\n";
                out = out + "Apellidos: " + el.getElementsByTagName("APELLIDOS").item(0).getTextContent() + "\r\n";
                //Get other tags... 
            }
        }
        return out;
    }

    public void appendNode(Document doc, String nombre, String apellidos) {
        Node nNombre = doc.createElement("NOMBRE");
        Node nNombre_text = doc.createTextNode(nombre);
        nNombre.appendChild(nNombre_text);
        Node nApellido = doc.createElement("APELLIDOS");
        Node nApellido_text = doc.createTextNode(apellidos);
        nApellido.appendChild(nApellido_text);
        //...

        Node nPERSONA = doc.createElement("PERSONA");
        nPERSONA.appendChild(nNombre);
        nPERSONA.appendChild(nApellido);

        //Add element to document doc
        Node root = doc.getDocumentElement();
        root.appendChild(nPERSONA);
        writeDocToXML(doc, f);
    }

    public void removeNodeDoc(Document doc, Integer index) {
        Node n;
        NodeList nodelist = doc.getElementsByTagName("PERSONA");

//Loop to read every item of node list
        for (int i = 0; i < nodelist.getLength(); i++) {

//Get set of items inside CD
            n = nodelist.item(i);
//Check Node type is Element
            if (n.getNodeType() == Node.ELEMENT_NODE) {
//Cast to Element
                Element el = (Element) n;
//remove child
                if (index.equals(i)) {
                    el.getParentNode().removeChild(el);
                }
            }
        }
        writeDocToXML(doc, f);
    }

    public void writeDocToXML(Document doc, File f) {
        //Create format
        OutputFormat format = new OutputFormat(doc);
        //Indenting output (sagnat, tabuladors...)
        format.setIndenting(true);
        try {
            XMLSerializer serializer = new XMLSerializer(new FileOutputStream(f), format);
            serializer.serialize(doc);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
