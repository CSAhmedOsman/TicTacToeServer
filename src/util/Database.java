/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author w
 */

//TODO SINGLETON
public class Database {
    private Connection connection;
    
    public Database() {
        try {
            //DriverManager.registerDriver(new ClientDriver());
            connection = DriverManager.getConnection("",
                "root", "root");
        } catch (SQLException ex) {
            handleExeption(ex);
        }
    }
    
    private void handleExeption(SQLException ex) {
        Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
    }
    
    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            System.out.println("Failed to close the connection");
        }
    }
    
    public Connection getConnection(){
        return connection;
    }
}

