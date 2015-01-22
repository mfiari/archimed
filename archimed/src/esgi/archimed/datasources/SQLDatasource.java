/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package esgi.archimed.datasources;

import java.sql.Connection;
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
    
    public SQLDatasource (String host, int port, String login, String password, String database) {
        this.name = "SQL datasource";
        this.host = host;
        this.port = port;
        this.login = login;
        this.password = password;
        this.database = database;
    }
    
    public SQLDatasource (String name, String host, int port, String login, String password, String database) {
        this.name = name;
        this.host = host;
        this.port = port;
        this.login = login;
        this.password = password;
        this.database = database;
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
    public Object execute(String request) {
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
        return result;
    }
    
    
}
