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
    public boolean addDatasource(Datasource datasource) {
        if (datasource instanceof XMLDatasource) {
            this.sources.add((XMLDatasource)datasource);
            this.pcs.firePropertyChange("addDatasource", this, datasource);
            return true;
        }
        return false;
    }

    @Override
    public void parse(String xpath) {
        
    }
    
    @Override
    public void addListener(PropertyChangeListener listener) {
        this.pcs.addPropertyChangeListener(listener);
    }
    
}
