/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package esgi.archimed.datasources;

import java.beans.PropertyChangeListener;

/**
 *
 * @author mike
 */
public interface Datasource {
    
    public boolean isAvailable ();
    
    public void setAvailable (boolean available);
    
    public String getName();
    
    public boolean handle (String request);
    
    public Object execute (String request);
    
    public void addListener(PropertyChangeListener listener);
    
}
