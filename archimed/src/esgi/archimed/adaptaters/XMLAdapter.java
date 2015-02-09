/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package esgi.archimed.adaptaters;

import esgi.archimed.datasources.Datasource;
import esgi.archimed.datasources.XMLDatasource;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author mike
 */
public class XMLAdapter implements Adapter {
    
    private final List<XMLDatasource> sources;
    private final String name;
    private final PropertyChangeSupport pcs;
    
    public XMLAdapter () {
        this.sources = new ArrayList<>();
        this.name = "XML Adapter";
        this.pcs = new PropertyChangeSupport(this);
    }
    
    public XMLAdapter (String name) {
        this.sources = new ArrayList<>();
        this.name = name;
        this.pcs = new PropertyChangeSupport(this);
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public List<Datasource> getDatasources () {
        List<Datasource> datasources = new ArrayList<>();
        for (XMLDatasource xmlDatasource : sources) {
            datasources.add(xmlDatasource);
        }
        return datasources;
    }

    @Override
    public boolean addDatasource(Datasource datasource) {
        if (datasource instanceof XMLDatasource) {
            this.sources.add((XMLDatasource)datasource);
            this.pcs.firePropertyChange("addDatasource", this, datasource);
            return true;
        }
        return false;
    }
    
    @Override
    public void removeDatasource (int index) {
        Datasource datasource = this.sources.remove(index);
        this.pcs.firePropertyChange("removeDatasource", this, datasource);
    }
    
    @Override
    public void removeDatasource (Datasource dataSource) {
        if (dataSource instanceof XMLDatasource) {
            this.sources.remove((XMLDatasource)dataSource);
            this.pcs.firePropertyChange("removeDatasource", this, dataSource);
        }
    }
    
    @Override
    public void removeAllDatasource () {
        while (!this.sources.isEmpty()) {
            Datasource datasource = this.sources.remove(0);
            this.pcs.firePropertyChange("removeDatasource", this, datasource);
        }
    }
    
    @Override
    public boolean handleRequest (String xpath) {
        String [] noeuds = xpath.split("/");
        if (noeuds.length < 2) {
            return false;
        } else {
            String racine = noeuds[1];
            for (XMLDatasource datasource : this.sources) {
                if (datasource.isAvailable() && datasource.handle(racine)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void parse(String xpath, Element parent, Document doc) {
        for (XMLDatasource datasource : sources) {
            if (datasource.isAvailable() && datasource.handle(xpath)) {
                NodeList nodeList = (NodeList)datasource.execute(xpath);
                if (nodeList != null) {
                    for (int i = 0; i < nodeList.getLength(); i++) {
                        Node node = doc.importNode(nodeList.item(i), true);
                        parent.appendChild(node);
                    }
                }
            }
        }
    }
    
    @Override
    public void addListener(PropertyChangeListener listener) {
        this.pcs.addPropertyChangeListener(listener);
    }
    
}
