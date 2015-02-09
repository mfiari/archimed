/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package esgi.archimed.utils;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 *
 * @author mike
 */
public class DTDValidator {
    
    public boolean validate(String dtdPath, String xmlPath) {
        try {
            File xmlwithDtd = new File("XmlWithDTD.xml");

            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();

            transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, dtdPath);

            transformer.transform(new StreamSource(xmlPath), new StreamResult(xmlwithDtd));

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

            factory.setValidating(true);
            DocumentBuilder builder = factory.newDocumentBuilder();

            // Set the error handler
            builder.setErrorHandler(new org.xml.sax.ErrorHandler() {
                @Override
                public void fatalError(SAXParseException spex) {
                    System.out.println("fatal error : " + spex.getMessage());
                }
                @Override
                public void error(SAXParseException spex) throws SAXException {
                    throw spex;
                }
                @Override
                public void warning(SAXParseException spex) {
                    System.out.println("warning : " + spex.getMessage());
                }
            });

            builder.parse(xmlwithDtd);
            xmlwithDtd.delete();
        } catch (TransformerException | ParserConfigurationException | SAXException | IOException e) {
            return false;
        }
        return true;
    }
    
}
