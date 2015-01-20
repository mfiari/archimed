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
public class SQLDatasource implements Datasource {
    
    private final String host;
    private final int port;
    private final String login;
    private final String password;
    private final String name;
    
    public SQLDatasource (String host, int port, String login, String password) {
        this.name = "SQL datasource";
        this.host = host;
        this.port = port;
        this.login = login;
        this.password = password;
    }
    
    public SQLDatasource (String name, String host, int port, String login, String password) {
        this.name = name;
        this.host = host;
        this.port = port;
        this.login = login;
        this.password = password;
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
