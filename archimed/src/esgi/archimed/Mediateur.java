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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

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
        adapter.removeAllDatasource();
        this.pcs.firePropertyChange("removeAdapter", adapter, null);
    }
    
    public void removeAdapter (Adapter adapter) {
        this.adapters.remove(adapter);
        this.pcs.firePropertyChange("removeAdapter", null, null);
    }
    
    public void addListener(PropertyChangeListener listener) {
        this.pcs.addPropertyChangeListener(listener);
    }
    
    public void runRequest (String request) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();
            Element parent = doc.createElement("results");
            doc.appendChild(parent);
            for (Adapter adapter : this.adapters) {
                new RequestThread(request, adapter, parent, doc).start();
            }
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(Mediateur.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public class RequestThread extends Thread {
        
        private final String request;
        private final Adapter adapter;
        private final Element parent;
        private final Document document;
        
        public RequestThread (String request, Adapter adapter, Element parent, Document document) {
            this.adapter = adapter;
            this.parent = parent;
            this.request = request;
            this.document = document;
        }
        
        @Override
        public void run() {
            Element element = this.document.createElement("adapter");
            element.setAttribute("nom", this.adapter.getName());
            this.adapter.parse(this.request, element, this.document);
            Mediateur.this.refresh(this.document, this.parent, element);
        }
    }
    
    public synchronized void refresh (Document document, Element parent, Element fils) {
        parent.appendChild(fils);
        Mediateur.this.pcs.firePropertyChange("writeResult", document, null);
    }
    
    
}
