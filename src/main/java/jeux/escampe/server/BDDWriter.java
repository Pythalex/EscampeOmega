package jeux.escampe.server;

import java.util.ArrayList;
import java.util.List;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class BDDWriter{

    String path;
    boolean usingConnection = false;
    Connection conn;

    public BDDWriter(String path){
        this.path = path;
    }

    private Connection connect(String path) {
        Connection conn = null;
 
        try {
            String url = "jdbc:sqlite:" + path;
 
            conn = DriverManager.getConnection(url);
 
        } catch (SQLException e) {
            e.printStackTrace();
        } 
 
        return conn;
    }

    public Boolean insertMatchResult(String winner, String loser){
        String query = "insert into match(winner, loser) values(?,?)";
 
        boolean failed = false;
 
        if (usingConnection) {
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, winner);
            pstmt.setString(2, loser);
            pstmt.executeUpdate();
    
            } catch (SQLException e){
                e.printStackTrace();
                failed = true;
            } catch (NullPointerException e){
                System.err.println("Connection could not be opened, insert statement aborted.");
                failed = true;
            }
        } else {
            try (Connection conn = connect(path);
                PreparedStatement pstmt = conn.prepareStatement(query)) {
    
                pstmt.setString(1, winner);
                pstmt.setString(2, loser);
                pstmt.executeUpdate();
    
            } catch (SQLException e){
                e.printStackTrace();
                failed = true;
            } catch (NullPointerException e){
                System.err.println("Connection could not be opened, insert statement aborted.");
                failed = true;
            }
        }
 
        return !failed;
    }

    public boolean useConnection(){
        conn = connect(path);
        return conn != null;
    }

    public boolean closeConnection(){
        try {
            conn.close();
            return true;
        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }
}