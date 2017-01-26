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

    //hämtar alla recept
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
                String img = data.getString("image");

                System.out.println("test2");

                jsonArrayBuilder.add(Json.createObjectBuilder()
                        .add("recipe_id", id_rec)
                        .add("recipe_name", name)
                        .add("recipe_description", des)
                        .add("recipe_instruction", ins)
                        .add("username", user)
                        .add("category_name", cat)
                        .add("image", img).build());

                System.out.println("test3");
            }
            connection.close();
            return jsonArrayBuilder.build();
        } catch (Exception ex) {
            System.out.println(ex.getMessage() + "fel här");
        }
        return null;
    }

    //hämtar ingredients
    public JsonArray getIngredient() {

        try {
            Connection connection = ConnectionFactory.make("127.0.0.1");
            Statement stmt = connection.createStatement();
            String sql = "SELECT * FROM ingredient";
            ResultSet data = stmt.executeQuery(sql);
            //arraybuilder
            System.out.println("hejehejehehjehejbeowbg");
            JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
            while (data.next()) {
                int ing_id = data.getInt("ingredient_id");
                String id_rec = data.getString("ingredient_name");

                jsonArrayBuilder.add(Json.createObjectBuilder()
                        .add("ingredient_id", ing_id)
                        .add("ingredient_name", id_rec).build());
            }
            connection.close();
            return jsonArrayBuilder.build();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }

    //hämtar ett specifikt id
    public JsonArray getRecipeId(int id) {
        try {
            System.out.println("hejs");
            Connection connection = ConnectionFactory.make("127.0.0.1");
            String sql = "SELECT * FROM info_recipe WHERE recipe_id = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet data = stmt.executeQuery();
            JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
            while (data.next()) {
                int id_rec = data.getInt("recipe_id");
                String name = data.getString("recipe_name");
                String des = data.getString("recipe_description");
                String ins = data.getString("recipe_instruction");
                String user = data.getString("username");
                String cat = data.getString("category_name");
                String img = data.getString("image");

                jsonArrayBuilder.add(Json.createObjectBuilder()
                        .add("recipe_id", id_rec)
                        .add("recipe_name", name)
                        .add("recipe_description", des)
                        .add("recipe_instruction", ins)
                        .add("username", user)
                        .add("category_name", cat)
                        .add("image", img).build());

            }
            connection.close();
            System.out.println("hej2");
            return jsonArrayBuilder.build();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    //hämtar ingredienser och amount
    public JsonArray getRecipeIngredients(int id) {
        try {
            Connection connection = ConnectionFactory.make("127.0.0.1");
            Statement stmt = connection.createStatement();
            String sql = "SELECT amount,(SELECT ingredient_name FROM ingredient where ingredient_id = recipe_ingredient.ingredient_ID) AS inge FROM recipe_ingredient WHERE recipe_ingredient.recipe_ID = " + id;
            ResultSet data = stmt.executeQuery(sql);
            //arraybuilder
            JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
            while (data.next()) {
                System.out.println("ingredient_name");
                System.out.println("amount");
                System.out.println("inge");
                String amount = data.getString("amount");
                String name = data.getString("inge");

                jsonArrayBuilder.add(Json.createObjectBuilder()
                        .add("amount", amount)
                        .add("inge", name).build());
            }
            connection.close();
            return jsonArrayBuilder.build();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }

    //hämtar kategorier
    public JsonArray getCategory() {

        try {
            Connection connection = ConnectionFactory.make("127.0.0.1");
            Statement stmt = connection.createStatement();
            String sql = "SELECT * FROM category";
            ResultSet data = stmt.executeQuery(sql);
            //arraybuilder
            System.out.println("fgfggfg");
            JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
            while (data.next()) {
                int cat_id = data.getInt("category_id");
                String cat_name = data.getString("category_name");

                jsonArrayBuilder.add(Json.createObjectBuilder()
                        .add("category_id", cat_id)
                        .add("category_name", cat_name).build());
            }
            connection.close();
            return jsonArrayBuilder.build();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }

    //lägger till recept
    public boolean addRecipe(String body) {
        JsonReader jsonReader = Json.createReader((new StringReader(body)));
        JsonObject data = jsonReader.readObject();
        jsonReader.close();

        System.out.println("1");
        
        String rec_name = data.getString("recipe_name");
        String rec_desc = data.getString("recipe_description");
        String rec_inst = data.getString("recipe_instruction");
        int rec_aut = data.getInt("recipe_author");
        int rec_cat = data.getInt("recipe_category");
        String rec_img = data.getString("image");

        System.out.println("recipe_author");
        
        try {
            Connection connection = ConnectionFactory.make("127.0.0.1");
            PreparedStatement stmt = connection.prepareStatement("INSERT INTO recipe VALUES(NULL,?,?,?,?,?,?)");
            stmt.setString(1, rec_name);
            stmt.setString(2, rec_desc);
            stmt.setString(3, rec_inst);
            stmt.setInt(4, rec_aut);
            stmt.setInt(5, rec_cat);
            stmt.setString(6, rec_img);
            
            System.out.println("3");
            
            stmt.executeUpdate();
            connection.close();
            return true;
        } catch (Exception e) {
            System.out.println(e + "hej");
            return false;
        }

    }

    //Lägger till ingredient
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
    //Lägger till amount
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
        //tar bort recept
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
    //tar bort ettett visst id --> används ej
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
    //uppdaterar ett recept -> används inte
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
    //uppdaterar ingredients -> används inte
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
