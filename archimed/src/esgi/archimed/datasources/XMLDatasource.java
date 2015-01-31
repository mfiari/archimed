/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package esgi.archimed.datasources;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

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
        return false;
    }

    @Override
    public Object execute(String request) {
        return null;
    }
    
    @Override
    public void addListener(PropertyChangeListener listener) {
        this.pcs.addPropertyChangeListener(listener);
    }
    
}
