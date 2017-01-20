
package nu.te4.support;


import com.mysql.jdbc.Connection;
import java.sql.DriverManager;


public class ConnectionFactory {
    
    public static Connection make(String server) throws Exception{
        
        if (server.equals("127.0.0.1")) {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = (Connection)DriverManager.getConnection("jdbc:mysql://localhost/recipiedatabas","root","");
            return connection;
        }
        return null;
    }
}
