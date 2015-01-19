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
    
    private String host;
    private int port;
    private String login;
    private String password;
    
    public SQLDatasource (String host, int port, String login, String password) {
        this.host = host;
        this.port = port;
        this.login = login;
        this.password = password;
    }

    @Override
    public boolean isAvailable() {
        return true;
    }

    @Override
    public void execute(String request) {
        
    }
    
    
}
