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
public class XMLDatasource implements Datasource {
    
    private String url;
    
    public XMLDatasource (String url) {
        this.url = url;
    }

    @Override
    public boolean isAvailable() {
        return true;
    }

    @Override
    public void execute(String request) {
        
    }
    
}
