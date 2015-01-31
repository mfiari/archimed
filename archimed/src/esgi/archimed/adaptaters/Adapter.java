/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package esgi.archimed.adaptaters;

import esgi.archimed.datasources.Datasource;
import java.beans.PropertyChangeListener;
import java.util.List;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author mike
 */
public interface Adapter {
    
    public String getName ();
    
    public List<Datasource> getDatasources ();
    
    public boolean addDatasource (Datasource dataSource);
    
    public void removeDatasource (int index);
    
    public void removeDatasource (Datasource dataSource);
    
    public void removeAllDatasource ();
    
    public boolean handleRequest (String xpath);
    
    public void parse (String xpath, Element parent, Document doc);
    
    public void addListener(PropertyChangeListener listener);
    
}
