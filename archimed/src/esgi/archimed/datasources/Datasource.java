/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package esgi.archimed.datasources;

/**
 *
 * @author mike
 */
public interface Datasource {
    
    public boolean isAvailable ();
    
    public void execute (String request);
    
}
