/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package esgi.archimed.datasources;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 *
 * @author mike
 */
public class XMLDatasource implements Datasource {
    
    private final String url;
    private final String name;
    private boolean available;
    private final PropertyChangeSupport pcs;
    
    public XMLDatasource (String url) {
        this.url = url;
        this.name = "XML datasource";
        this.available = true;
        this.pcs = new PropertyChangeSupport(this);
    }
    
    public XMLDatasource (String name, String url) {
        this.url = url;
        this.name = name;
        this.available = false;
        this.pcs = new PropertyChangeSupport(this);
    }

    @Override
    public String getName() {
        return this.name;
    }
    
    public String getUrl () {
        return this.url;
    }

    @Override
    public boolean isAvailable() {
        return this.available;
    }
    
    @Override
    public void setAvailable (boolean available) {
        this.available = available;
        this.pcs.firePropertyChange("available", this, available);
    }
    
    @Override
    public boolean handle (String request) {
        return true;
    }

    @Override
    public Object execute(String request) {
        try {
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            Document document = builder.parse(new FileInputStream(this.url));
            XPath xPath =  XPathFactory.newInstance().newXPath();
            return xPath.compile(request).evaluate(document, XPathConstants.NODESET);
        } catch (ParserConfigurationException | SAXException | IOException | XPathExpressionException e) {
            Logger.getLogger(XMLDatasource.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }
    
    @Override
    public void addListener(PropertyChangeListener listener) {
        this.pcs.addPropertyChangeListener(listener);
    }
    
}
