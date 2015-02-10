/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package esgi.archimed.datasources;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mike
 */
public class SQLDatasource implements Datasource {
    
    private final String host;
    private final int port;
    private final String login;
    private final String password;
    private final String database;
    private final String name;
    private boolean available;
    private final PropertyChangeSupport pcs;
    
    public SQLDatasource (String host, int port, String login, String password, String database) {
        this.name = "SQL datasource";
        this.host = host;
        this.port = port;
        this.login = login;
        this.password = password;
        this.database = database;
        this.available = true;
        this.pcs = new PropertyChangeSupport(this);
    }
    
    public SQLDatasource (String name, String host, int port, String login, String password, String database) {
        this.name = name;
        this.host = host;
        this.port = port;
        this.login = login;
        this.password = password;
        this.database = database;
        this.available = true;
        this.pcs = new PropertyChangeSupport(this);
    }

    @Override
    public String getName() {
        return this.name;
    }
    
    public String getHost () {
        return this.host;
    }
    
    public int getPort () {
        return this.port;
    }
    
    public String getLogin () {
        return this.login;
    }
    
    public String getPassword () {
        return this.password;
    }
    
    public String getDatabase () {
        return this.database;
    }

    @Override
    public boolean isAvailable() {
        return this.available;
    }
    
    @Override
    public void setAvailable (boolean available) {
        this.available = available;
        this.pcs.firePropertyChange("available", this, available);
    }
    
    @Override
    public boolean handle (String request) {
        try {
            String url = "jdbc:mysql://"+this.host+":"+this.port+"/"+this.database;
            Connection connection = DriverManager.getConnection(url, this.login, this.password);
            try {
                DatabaseMetaData md = connection.getMetaData();
                ResultSet rs = md.getTables(null, null, "%", null);
                while (rs.next()) {
                    if (rs.getString(3).equals(request)) {
                        return true;
                    }
                }
            } catch (SQLException ex) {
                Logger.getLogger(SQLDatasource.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException e) {
            System.out.println("probleme connexion");
            System.out.println(e.getMessage());
        }
        return false;
    }

    @Override
    public Object execute(String request) {
        this.pcs.firePropertyChange("processing", this, true);
        ResultSet result = null;
        try {
            String url = "jdbc:mysql://"+this.host+":"+this.port+"/"+this.database;
            Connection connection = DriverManager.getConnection(url, this.login, this.password);
            try {
                Statement statement = connection.createStatement() ;
                result = statement.executeQuery(request);
            } catch (SQLException ex) {
                Logger.getLogger(SQLDatasource.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException e) {
            System.out.println("probleme connexion");
            System.out.println(e.getMessage());
        }
        this.pcs.firePropertyChange("processing", this, false);
        return result;
    }
    
    @Override
    public void addListener(PropertyChangeListener listener) {
        this.pcs.addPropertyChangeListener(listener);
    }
    
    
}
