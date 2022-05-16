package chatgui.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.List;
import java.io.FileReader;

import org.json.simple.JSONObject;
import org.json.simple.parser.*;

public class ChatAppSQL {

    // General
    public List<String> connectAndExecuteQuery(String query) {
        ResultSet result = executeQueryAndReturnResult(query);
        return getAttributes(result);
    }

    public List<String> connectAndGetAttributes(String attributes, String table) {
        ResultSet result = connectAndSelectAttributes(attributes, table);
        return getAttributes(result);
    }

    public List<String> connectAndGetAttributesWithCondition(String attributes, String table, String condition)  {
        ResultSet result = connectAndSelectAttributes(attributes, table, condition);
        return getAttributes(result);
    }

    public List<ArrayList<String>> connectAndGetAttributesWithCondition(String attributes, String table, String condition, boolean transaction)  {
        ResultSet result = connectAndSelectAttributes(attributes, table, condition);
        return getTransactions(result);
    }

    public void connectAndSetAttributesWithCondition(String table, String attributes, String values, String condition) {
        connectAndSetAttributes(table, attributes, values, condition);
    }

    public void connectAndInsertValues(String table, String values) {
        String query = String.format("insert into %s values(%s)", table, values);
        executeQuery(query);
    }

    public void connectAndInsertSpecificAttributes(String table, String attributes, String values) {
        String query = String.format("insert into %s (%s) values(%s)", table, attributes, values);
        executeQuery(query);
    }

    // Private Methods
    private void connectAndSetAttributes(String table, String attributes, String values, String condition)  {
        String query = String.format("update %s set %s = %s where %s", table, attributes, values, condition);
        executeQuery(query);
    }

    private ResultSet connectAndSelectAttributes(String attributes, String table)  {
        String query = String.format("select %s from %s", attributes, table);
        return executeQueryAndReturnResult(query);
    }

    private ResultSet connectAndSelectAttributes(String attributes, String table, String condition)  {
        String query = String.format("select %s from %s where %s", attributes, table, condition);
        return executeQueryAndReturnResult(query);
    }

    private void executeQuery(String query) {
        Statement statement = connect();
        System.out.println(query);
        try {
            statement.executeUpdate(query);
        }
        catch(Exception e){
            return;
        }
    }

    private ResultSet executeQueryAndReturnResult(String query)  {
        Statement statement = connect();
        System.out.println(query);
        try {
            ResultSet result = statement.executeQuery(query);
            return result;
        }
        catch(Exception e){
            return null;
        }
    }

    private List<ArrayList<String>> getTransactions(ResultSet result) {
        List<ArrayList<String>> transactions = new ArrayList<ArrayList<String>>();
        try{
            java.sql.ResultSetMetaData rsmd = result.getMetaData();
            int column_count = rsmd.getColumnCount();

            ArrayList<String> each_transaction = new ArrayList<>();
            // Read each row of data
            while(result.next()){
                for(int i = 1; i <= column_count; ++i) {
                    each_transaction.add(result.getString(i));
                }
                ArrayList<String> cloned_list = new ArrayList<String>(each_transaction);
                transactions.add(cloned_list);
                each_transaction.clear();
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return transactions;
    }

    private List<String> getAttributes(ResultSet result) {
        List<String> data = new ArrayList<>();
        try{
            java.sql.ResultSetMetaData rsmd = result.getMetaData();
            int column_count = rsmd.getColumnCount();

            // Read each row of data
            while(result.next()){
                for(int i = 1; i <= column_count; ++i) {
                    data.add(result.getString(i));
                }
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    private Statement connect() {

        String url = "NULL";
        String user = "NULL";
        String password = "NULL";

        try {
            Object obj = new JSONParser().parse(new FileReader("config/login.json"));
            JSONObject jo = (JSONObject) obj;
            url = (String) jo.get("url");
            user = (String) jo.get("user");
            password = (String) jo.get("password");
        } catch(Exception e){
            e.printStackTrace();
        }

        check_for_driver_class();

        try {
            Connection con = DriverManager.getConnection(url, user, password);
            Statement statement = con.createStatement();
            return statement;
        } catch(SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    private void check_for_driver_class() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } 
        catch(ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}