package com.db;

import java.sql.*;

public class PostgresConnect {
    public PostgresConnect(){}

    public Connection getConnection(){
        String DB_URL = "jdbc:postgresql://localhost:5432/employees";
        String DB_USER = "postgres";
        String DB_PWD = "admin";
        Connection connection = null;
        boolean tableExists = false;
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PWD);
            //tableExists = tableExistsSQL(connection, "employees");
        } catch (Exception e){
            e.printStackTrace();
            System.err.println("line 24");
            System.err.println(e.getClass().getName()+": "+e.getMessage());
        } finally {
            System.out.println("Opened database successfully");
        }
        return connection;
    }

    boolean tableExistsSQL(Connection connection, String tableName) throws SQLException {
        String query = "SELECT EXISTS (" +
                "SELECT 1 " +
                "FROM pg_tables " +
                "WHERE tablename = ?" +
                ")";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, tableName);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                resultSet.next();
                boolean exists = resultSet.getBoolean(1);
                System.out.println("Table exists: " + exists);
                return exists;
            }
        }
    }



    public void createTable(Connection c) {
        Statement stmt = null;
        try {
            stmt = c.createStatement();
            String sql = "CREATE TABLE EMPLOYEE " +
                    "(ID INT PRIMARY KEY     NOT NULL," +
                    " email             CHAR(50), " +
                    " firstName           TEXT    NOT NULL, " +
                    " lastName            TEXT     NOT NULL)";
            stmt.executeUpdate(sql);
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.err.println("line 61");
        } finally {
            System.out.println("Table created successfully");
        }
    }

    public void populateTable(Connection c) throws SQLException {
        Statement stmt = c.createStatement();
        String sql = "INSERT INTO EMPLOYEE (ID,email,firstName,lastName) "
                + "VALUES (1, 'johndoe@gmail.com', 'John', 'Doe');";
        stmt.executeUpdate(sql);

        sql = "INSERT INTO EMPLOYEE (ID,email,firstName,lastName) "
                + "VALUES (2, 'janesmith@gmail.com', 'Jane', 'Smith');";
        stmt.executeUpdate(sql);

        sql = "INSERT INTO EMPLOYEE (ID,email,firstName,lastName) "
                + "VALUES (3, 'barakobama@aol.com', 'Barak', 'Obama');";
        stmt.executeUpdate(sql);

        sql = "INSERT INTO EMPLOYEE (ID,email,firstName,lastName) "
                + "VALUES (4, 'bobsmith@yahoo.com', 'Bob', 'Smith');";
        stmt.executeUpdate(sql);

    }
}