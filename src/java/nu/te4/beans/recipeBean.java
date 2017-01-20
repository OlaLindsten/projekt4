/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nu.te4.beans;

import com.mysql.jdbc.Connection;
import java.io.StringReader;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.ejb.Stateless;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonReader;
import nu.te4.support.ConnectionFactory;

@Stateless
public class recipeBean {

    public JsonArray getRecipe() {

        try {
            Connection connection = ConnectionFactory.make("127.0.0.1");
            Statement stmt = connection.createStatement();
            String sql = "SELECT * FROM info_recipe";
            ResultSet data = stmt.executeQuery(sql);
            //arraybuilder
            System.out.println("hejehejehehjehejbeowbg");
            JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
            while (data.next()) {
                int id_rec = data.getInt("recipe_id");
                String name = data.getString("recipe_name");
                String des = data.getString("recipe_description");
                String ins = data.getString("recipe_instruction");
                String user = data.getString("username");
                String cat = data.getString("category_name");


                jsonArrayBuilder.add(Json.createObjectBuilder()
                        .add("recipe_id", id_rec)
                        .add("recipe_name", name)
                        .add("recipe_description", des)
                        .add("recipe_instruction", ins)
                        .add("username", user)
                        .add("category_name", cat).build());
            }
            connection.close();
            return jsonArrayBuilder.build();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }
    
    public JsonArray getIngredient(){
       
                try {
            Connection connection = ConnectionFactory.make("127.0.0.1");
            Statement stmt = connection.createStatement();
            String sql = "SELECT * FROM ingredient";
            ResultSet data = stmt.executeQuery(sql);
            //arraybuilder
            System.out.println("hejehejehehjehejbeowbg");
            JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
            while (data.next()) {
                String id_rec = data.getString("ingredient_name");

                jsonArrayBuilder.add(Json.createObjectBuilder()
                        .add("ingredient_name", id_rec).build());
            }
            connection.close();
            return jsonArrayBuilder.build();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }


    public boolean addRecipe(String body) {
        JsonReader jsonReader = Json.createReader((new StringReader(body)));
        JsonObject data = jsonReader.readObject();
        jsonReader.close();

        String rec_name = data.getString("recipe_name");
        String rec_desc = data.getString("recipe_description");
        String rec_inst = data.getString("recipe_instruction");

        try {
            Connection connection = ConnectionFactory.make("127.0.0.1");
            PreparedStatement stmt = connection.prepareStatement("INSERT INTO recipe VALUES(NULL,?,?,?)");
            stmt.setString(1, rec_name);
            stmt.setString(2, rec_desc);
            stmt.setString(3, rec_inst);
            stmt.executeUpdate();
            connection.close();
            return true;
        } catch (Exception e) {
            System.out.println(e + "hej");
            return false;
        }

    }

    public boolean addIngredient(String body) {
        JsonReader jsonReader = Json.createReader((new StringReader(body)));
        JsonObject data = jsonReader.readObject();
        jsonReader.close();

        String ing_name = data.getString("ingredient_name");

        try {
            Connection connection = ConnectionFactory.make("127.0.0.1");
            PreparedStatement stmt = connection.prepareStatement("INSERT INTO ingredient VALUES(NULL,?)");
            stmt.setString(1, ing_name);
            stmt.executeUpdate();
            connection.close();
            return true;
        } catch (Exception e) {
            System.out.println(e + "hej");
            return false;
        }

    }

    public boolean addRec_ing(String body) {
        JsonReader jsonReader = Json.createReader((new StringReader(body)));
        JsonObject data = jsonReader.readObject();
        jsonReader.close();

        int id_rec = data.getInt("recipe_ID");
        int id_ing = data.getInt("ingredient_ID");
        String amo = data.getString("amount");
        try {
            Connection connection = ConnectionFactory.make("127.0.0.1");
            PreparedStatement stmt = connection.prepareStatement("INSERT INTO recipe_ingredient VALUES(?,?,?)");
            stmt.setInt(1, id_rec);
            stmt.setInt(2, id_ing);
            stmt.setString(3, amo);
            stmt.executeUpdate();
            connection.close();
            return true;
        } catch (Exception e) {
            System.out.println(e + "hej");
            return false;
        }

    }

    public boolean deleteRecipe(int id) {
        try {
            Connection connection = ConnectionFactory.make("127.0.0.1");
            PreparedStatement stmt = connection.prepareStatement("DELETE FROM recipe WHERE recipe_id =?");
            stmt.setInt(1, id);
            stmt.executeUpdate();
            connection.close();
            return true;
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteIngredient(int id) {
        try {
            Connection connection = ConnectionFactory.make("127.0.0.1");
            PreparedStatement stmt = connection.prepareStatement("DELETE FROM ingredient WHERE ingredient_id =?");
            stmt.setInt(1, id);
            stmt.executeUpdate();
            connection.close();
            return true;
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
            return false;
        }
    }

    public boolean updateRecipe(String body) {

        JsonReader jsonReader = Json.createReader(new StringReader(body));

        JsonObject data = jsonReader.readObject();
        jsonReader.close();

        System.out.println("2");
        String rec_nam = data.getString("recipe_name");
        String rec_des = data.getString("recipe_description");
        String rec_ins = data.getString("recipe_instruction");
        int rec_cat = data.getInt("recipe_category");
        int rec_id = data.getInt("recipe_id");

        System.out.println("3");
        try {
            Connection connection = ConnectionFactory.make("127.0.0.1");
            PreparedStatement stmt = connection.prepareStatement("UPDATE recipe SET recipe_name = ?, recipe_description = ?, recipe_instruction = ?, recipe_category =? WHERE recipe_id =?");


            stmt.setString(1, rec_nam);
            stmt.setString(2, rec_des);
            stmt.setString(3, rec_ins);
            stmt.setInt(4, rec_cat);
            stmt.setInt(5, rec_id);

            stmt.executeUpdate();
            connection.close();
            System.out.println("6");
            return true;

        } catch (Exception e) {
            return false;
        }
    }

    public boolean updateIngredient(String body) {

        JsonReader jsonReader = Json.createReader(new StringReader(body));

        JsonObject data = jsonReader.readObject();
        jsonReader.close();

        System.out.println("2");
        String ing_nam = data.getString("ingredient_name");
        int ing_id = data.getInt("ingredient_id");
        System.out.println("3");
        try {
            Connection connection = ConnectionFactory.make("127.0.0.1");
            PreparedStatement stmt = connection.prepareStatement("UPDATE ingredient SET ingredient_name = ? WHERE ingredient_id =?");

            System.out.println("har skickat sql fråga 5");

            stmt.setString(1, ing_nam);
            stmt.setInt(2, ing_id);

            stmt.executeUpdate();
            connection.close();
            System.out.println("6");
            return true;

        } catch (Exception e) {
            return false;
        }
    }
}