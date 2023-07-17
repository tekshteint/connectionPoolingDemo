package com.model;

import java.sql.*;
import java.util.List;
import java.util.Map;

import com.db.PostgresConnect;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.hibernate.*;
import org.hibernate.cfg.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import io.opentelemetry.instrumentation.annotations.WithSpan;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;

@RestController
public class EmployeeController {

    @Autowired
    private final DataSource datasource;
    @Autowired
    private EmployeeRepository employeeRepository;
    @PersistenceContext
    private EntityManager entityManager;
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
    public String testNativeSQL() throws InterruptedException {
        Connection connection = getConnection();
        executeSQLNativeSelect(connection);

        try {
            Thread.sleep(200);
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return "nativeSQL";
    }

    @GetMapping("/hibernate")
    public String testHibernateSQL() {
        nativeHibernateQuery();
        return "hibernate";
    }

    @WithSpan
    public void executeSQLNativeSelect(Connection connection) {
        try {
            Statement statement = connection.createStatement();
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM employee");
            ps.execute();

        } catch (SQLException e) {
            e.printStackTrace();
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

/*    @WithSpan
    public void nativeHibernateQuery() {
        try {
            String sqlStatement = "SELECT * FROM owners";
            Query query = entityManager.createNativeQuery(sqlStatement);
            @SuppressWarnings("unchecked")
            List<Object[]> resultList = query.getResultList();
            Thread.sleep(200);
        } catch (InterruptedException e){
            throw new RuntimeException(e);
        }*/


    @WithSpan
    public void nativeHibernateQuery() {
        try {
            String sqlStatement = "SELECT * FROM employees";
            Query query = entityManager.createNativeQuery(sqlStatement);
            @SuppressWarnings("unchecked")
            List<Object[]> resultList = query.getResultList();
            Thread.sleep(200);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }



}


