/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package esgi.archimed.adaptaters;

import esgi.archimed.datasources.Datasource;
import esgi.archimed.datasources.SQLDatasource;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mike
 */
public class SQLAdapter implements Adapter {
    
    private final List<SQLDatasource> sources;
    private final String name;
    private final PropertyChangeSupport pcs;
    
    public SQLAdapter () {
        this.sources = new ArrayList<>();
        this.name = "SQL Adapter";
        this.pcs = new PropertyChangeSupport(this);
    }
    
    public SQLAdapter (String name) {
        this.sources = new ArrayList<>();
        this.name = name;
        this.pcs = new PropertyChangeSupport(this);
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public List<Datasource> getDatasources () {
        List<Datasource> datasources = new ArrayList<>();
        for (SQLDatasource sQLDatasource : sources) {
            datasources.add(sQLDatasource);
        }
        return datasources;
    }
    
    @Override
    public boolean addDatasource(Datasource datasource) {
        if (datasource instanceof SQLDatasource) {
            this.sources.add((SQLDatasource)datasource);
            this.pcs.firePropertyChange("addDatasource", this, datasource);
            return true;
        }
        return false;
    }
    
    @Override
    public void removeDatasource (int index) {
        Datasource datasource = this.sources.remove(index);
        this.pcs.firePropertyChange("removeDatasource", datasource, null);
    }
    
    @Override
    public void removeDatasource (Datasource dataSource) {
        if (dataSource instanceof SQLDatasource) {
            this.sources.remove((SQLDatasource)dataSource);
            this.pcs.firePropertyChange("removeDatasource", dataSource, null);
        }
    }
    
    @Override
    public void removeAllDatasource () {
        while (!this.sources.isEmpty()) {
            Datasource datasource = this.sources.remove(0);
            this.pcs.firePropertyChange("removeDatasource", datasource, null);
        }
    }

    @Override
    public void parse(String xpath) {
        
    }
    
    @Override
    public void addListener(PropertyChangeListener listener) {
        this.pcs.addPropertyChangeListener(listener);
    }
 
}