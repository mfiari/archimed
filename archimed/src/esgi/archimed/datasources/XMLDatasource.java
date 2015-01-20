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
    
    private final String url;
    private final String name;
    
    public XMLDatasource (String url) {
        this.url = url;
        this.name = "XML datasource";
    }
    
    public XMLDatasource (String name, String url) {
        this.url = url;
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public boolean isAvailable() {
        return true;
    }

    @Override
    public void execute(String request) {
        
    }
    
}
