/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package esgi.archimed.adaptaters;

import esgi.archimed.datasources.SQLDatasource;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mike
 */
public class SQLAdapter implements Adapter {
    
    private List<SQLDatasource> sources;
    
    public SQLAdapter () {
        this.sources = new ArrayList<>();
    }
    
    private void addDatasource (SQLDatasource datasource) {
        this.sources.add(datasource);
    }

    @Override
    public void parse(String xpath) {
        
    }
 
}