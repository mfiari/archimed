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
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

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
    public String getAdresse () {
        return "localhost";
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
        this.pcs.firePropertyChange("removeDatasource", this, datasource);
    }
    
    @Override
    public void removeDatasource (Datasource dataSource) {
        if (dataSource instanceof SQLDatasource) {
            this.sources.remove((SQLDatasource)dataSource);
            this.pcs.firePropertyChange("removeDatasource", this, dataSource);
        }
    }
    
    @Override
    public void removeAllDatasource () {
        while (!this.sources.isEmpty()) {
            Datasource datasource = this.sources.remove(0);
            this.pcs.firePropertyChange("removeDatasource", this, datasource);
        }
    }
    
    @Override
    public boolean handleRequest (String xpath) {
        String [] noeuds = xpath.split("/");
        if (noeuds.length < 2) {
            return false;
        } else {
            String racine = noeuds[1];
            for (SQLDatasource datasource : this.sources) {
                if (datasource.handle(racine)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void parse(String xpath, Element parent, Document doc) {
        String [] noeuds = xpath.split("/");
        String sql = "";
        String racine = "";
        String element = "ligne";
        String field = null;
        if (noeuds.length < 2) {
            System.out.println("Mauvaise requête xpath");
        } else if (noeuds.length == 2) {
            racine = noeuds[1];
            sql = "SELECT * FROM " + racine;
        } else if (noeuds.length == 3) {
            racine = noeuds[1];
            element = noeuds[2];
            sql = "SELECT * FROM " + racine;
        } else if (noeuds.length == 4) {
            racine = noeuds[1];
            element = noeuds[2];
            field = noeuds[3];
            sql = "SELECT "+field+" FROM " + racine;
        }
        Element produits = doc.createElement(racine);
        for (SQLDatasource datasource : sources) {
            if (datasource.isAvailable()) {
                if (datasource.handle(racine)) {
                    try {
                        ResultSet result = (ResultSet) datasource.execute(sql);
                        if (result != null) {
                            ResultSetMetaData rsmd = result.getMetaData();
                            int columnCount = rsmd.getColumnCount();
                            while (result.next()) {
                                Element elmt;
                                if (field == null) {
                                    elmt = doc.createElement(element);
                                    for (int i = 1 ; i <= columnCount ; i++) {
                                        String columnName = rsmd.getColumnName(i);
                                        String columnValue = result.getString(columnName);
                                        elmt.setAttribute(columnName, columnValue);
                                    }
                                } else {
                                    elmt = doc.createElement(field);
                                    elmt.appendChild(doc.createTextNode(result.getString(field)));
                                }
                                produits.appendChild(elmt);
                            }
                        }
                    } catch (SQLException ex) {
                        Logger.getLogger(SQLAdapter.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            } else {
                Element dts = doc.createElement("datasource");
                dts.setAttribute("nom", datasource.getName());
                dts.appendChild(doc.createTextNode("datasource not available"));
                produits.appendChild(dts);
            }
        }
        parent.appendChild(produits);
    }
    
    @Override
    public void addListener(PropertyChangeListener listener) {
        this.pcs.addPropertyChangeListener(listener);
    }
 
}