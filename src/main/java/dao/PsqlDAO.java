package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by Maciej Jankowicz on 28.06.18, 13:29
 * Contact: mj6367@gmail.com
 */
public class PsqlDAO {

//    Connection

    public Connection establishConnection()throws SQLException{
        try {
            Class.forName("org.postgresql.Driver");
        }catch(ClassNotFoundException cnfe){
            cnfe.printStackTrace();
        }

        Connection connection = null;
        connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/userDatabase", "DBdao", "database");
        if(connection!=null){
            System.out.println("Connected to database");
            return connection;
        }
        else{
            System.out.println("Error connecting to database");
            return null;
        }
    }
}
