/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package esgi.archimed.adaptaters;

import esgi.archimed.datasources.Datasource;
import java.beans.PropertyChangeListener;

/**
 *
 * @author mike
 */
public interface Adapter {
    
    public String getName ();
    
    public void parse (String xpath);
    
    public boolean addDatasource (Datasource dataSource);
    
    public void addListener(PropertyChangeListener listener);
    
}
