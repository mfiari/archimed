/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package esgi.archimed;

import esgi.archimed.adaptaters.Adapter;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mike
 */
public class Mediateur {
    
    private final List<Adapter> adapters;
    private final PropertyChangeSupport pcs;
    
    public Mediateur () {
        this.adapters = new ArrayList<>();
        this.pcs = new PropertyChangeSupport(this);
    }
    
    public List<Adapter> getAdapters () {
        return this.adapters;
    }
    
    public void addAdapter (Adapter adapter) {
        this.adapters.add(adapter);
        this.pcs.firePropertyChange("addAdapter", adapter, null);
    }
    
    public void removeAdapter (int index) {
        Adapter adapter = this.adapters.remove(index);
        this.pcs.firePropertyChange("removeAdapter", adapter, null);
    }
    
    public void removeAdapter (Adapter adapter) {
        this.adapters.remove(adapter);
        this.pcs.firePropertyChange("removeAdapter", null, null);
    }
    
    public void addListener(PropertyChangeListener listener) {
        this.pcs.addPropertyChangeListener(listener);
    }
    
    
}
