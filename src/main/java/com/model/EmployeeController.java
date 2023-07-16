package com.model;

import java.sql.*;
import java.util.Set;

import com.db.PostgresConnect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import io.opentelemetry.instrumentation.annotations.WithSpan;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;

@RestController
public class EmployeeController {

    @Autowired
    private final DataSource datasource;
    private boolean setup = false;

    @Autowired
    public EmployeeController(DataSource datasource) {
        this.datasource = datasource;
    }


    @GetMapping("/")
    public void setupDB(){
        PostgresConnect pg = new PostgresConnect();
        Connection conn = pg.getConnection();
        pg.createTable(conn);
        try {
            pg.populateTable(conn);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/nativeSQL")
    public String testNativeSQL() {
        System.out.print("Before execution: ");
        printStatus();

        Connection connection = getConnection();
        System.out.println();
		executeSQLNativeSelect(connection);

        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        System.out.println("After execution: ");
        printStatus();

        return "nativeSQL";
    }

    @GetMapping("/nativeSQLNew")
    public String testNativeSQLNew() {
        System.out.print("Before execution: ");
        printStatus();

        executeSQLNativeSelectNew();
        System.out.println("After execution: ");
        printStatus();

        return "nativeSQLNew";
    }



    @WithSpan
    public synchronized void executeSQLNativeSelectNew() {
        try (Connection connection = datasource.getConnection()){
            //Statement statement = connection.createStatement();
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM employee");
            ps.execute();
            Thread.sleep(200);

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void printStatus() {
        System.out.println(Thread.currentThread().getId() + ": " + Thread.currentThread().getState());
    }

    @WithSpan
    public synchronized void executeSQLNativeSelect(Connection connection) {
        try {
            Statement statement = connection.createStatement();
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM employee");
            ps.execute();
            Thread.sleep(200);

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @WithSpan
    public Connection getConnection() {
        try {
            return datasource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @WithSpan
    public synchronized void DBCP_insert_New(){
        try (Connection connection = datasource.getConnection()) {
            Statement statement = connection.createStatement();
            PreparedStatement ps = connection.prepareStatement("INSERT INTO employees (first_name, last_name) VALUES (?,?)");
            ps.setString(1, "firstName");
            ps.setString(2, "lastName");
            ps.execute();
            Thread.sleep(200);

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @WithSpan
    public synchronized void DBCP_insert(Connection connection){
        try {
            Statement statement = connection.createStatement();
            PreparedStatement ps = connection.prepareStatement("INSERT INTO employees (first_name, last_name) VALUES (?,?)");
            ps.setString(1, "firstName");
            ps.setString(2, "lastName");
            ps.execute();
            Thread.sleep(200);

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}


