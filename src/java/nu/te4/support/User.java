/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nu.te4.support;

import bycrypttest.BCrypt;
import com.mysql.jdbc.Connection;
import java.io.StringReader;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Base64;
import java.util.List;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.ws.rs.core.HttpHeaders;


public class User {
    
    public static boolean Authoricate(HttpHeaders httpHeaders){
        try {
            List<String> authHeader = httpHeaders.getRequestHeader(httpHeaders.AUTHORIZATION);
            String header = authHeader.get(0);
            header = header.substring(header.indexOf(" ")+1);
            byte[] decode = Base64.getDecoder().decode(header);
            String userPass = new String(decode);
            System.out.println(userPass);
            //plocka ut anv och lösenord
            String username = userPass.substring(0,userPass.indexOf(":"));
            String password = userPass.substring(userPass.indexOf(":")+1);
            
            Connection connection = ConnectionFactory.make("127.0.0.1");
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM users WHERE username =?");
            stmt.setString(1, username);
            ResultSet data = stmt.executeQuery();
            data.next();
            String hasedPass = data.getString("password");
            connection.close();
            System.out.println(username);
            System.out.println(password);
            return BCrypt.checkpw(password, hasedPass);
        } catch (Exception e) {
            System.out.println("Error: "+e.getMessage());
        }
        return false;
    }
    
    public static boolean createUser(String body){
        
       JsonReader jsonReader = Json.createReader(new StringReader(body));
       JsonObject data = jsonReader.readObject();
       jsonReader.close();
       
       String username = data.getString("username");
       String password = data.getString("password");
        
       String hashpw = BCrypt.hashpw(password, BCrypt.gensalt());
        try {
            Connection connection = ConnectionFactory.make("127.0.0.1");
            PreparedStatement stmt = connection.prepareStatement("INSERT INTO users VALUES(NULL,?,?)");
            stmt.setString(1, username);
            stmt.setString(2, hashpw);
            stmt.executeUpdate();
            connection.close();
            return true;
        } catch (Exception e) {
            System.out.println(e+ "kommer hit");
        }
        return false;
    }
    
}
