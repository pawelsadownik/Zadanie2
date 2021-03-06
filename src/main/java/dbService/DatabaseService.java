package dbService;

import Model.Application;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseService {

    public Statement createTable;

    Connection connection = null;
    private String createTableSql = "CREATE TABLE users("
            + "id INT GENERATED BY DEFAULT AS IDENTITY,"
            + "email VARCHAR (20),"
            + "password VARCHAR (20),"
            + "role VARCHAR (20))"
            + "";
    private String insertSql = "INSERT INTO USERS ( email, password, role) VALUES (?,?,?)";
    private String selectAllSql = "SELECT * FROM users";
    private String updateSql = "UPDATE USERS SET role=? WHERE email = ?";
    private String selectSql = "SELECT role FROM USERS WHERE email = ?";

    private PreparedStatement insert;
    private PreparedStatement selectAll;
    private PreparedStatement update;
    private PreparedStatement select;


    public DatabaseService() {
        int result = 0;

        try {
            connection = DriverManager.getConnection("jdbc:hsqldb:hsql://localhost/workdb","SA", "");
            createTable = connection.createStatement();
            createTable();
            insert = connection.prepareStatement(insertSql);
            update = connection.prepareStatement(updateSql);

            selectAll = connection.prepareStatement(selectAllSql);
            select = connection.prepareStatement(selectSql);

            //result = createTable.executeUpdate(createTableSql);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    public  List<Application> getAll() {
        List<Application> result = new ArrayList<Application>();
        try {
            ResultSet rs = selectAll.executeQuery();
            while (rs.next()) {
                Application c = new Application();
                c.setId(rs.getInt("id"));
                c.setEmail(rs.getString("email"));
                c.setPassword(rs.getString("password"));
                c.setRole(rs.getString("role"));

                result.add(c);
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
    public String getRoleByEmail(String email){

        String userRole = "";

        try{
            select.setString(1, email);

            ResultSet rs = select.executeQuery();
            boolean roleExists = rs.next();

            if (roleExists){
                userRole = rs.getString("role");
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userRole;
    }


    public void add(Application Application) {
        try {

            insert.setString(1, Application.getEmail());
            insert.setString(2, Application.getPassword());
            insert.setString(3, Application.getRole());
            insert.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void update(String email, String role) {
        try {
            update.setString(1,  role);
            update.setString(2, email.trim());
            update.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void createTable() {
        try {

            ResultSet rs = connection.getMetaData().getTables(null, null, null, null);
            boolean tableExists = false;
            while (rs.next()) {
                if (rs.getString("TABLE_NAME").equalsIgnoreCase("users")) {
                    tableExists = true;
                    break;
                }
            }
            if (!tableExists) {
                createTable.executeUpdate(createTableSql);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
