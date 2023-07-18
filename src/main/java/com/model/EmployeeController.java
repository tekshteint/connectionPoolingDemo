package com.model;

import java.sql.*;
import java.util.List;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
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

    @Autowired
    public EmployeeController(DataSource datasource) {
        this.datasource = datasource;
    }

    @GetMapping("/nativeSQL")
    public String testNativeSQL() throws InterruptedException {
        Connection connection = getConnection();
        executeSQLNativeSelect(connection);
        try {
            Thread.sleep(800);
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
    public Connection getConnection() {
        try {
            return datasource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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
    public void nativeHibernateQuery() {
        try {
            String sqlStatement = "SELECT * FROM employees";
            Query query = entityManager.createNativeQuery(sqlStatement);
            @SuppressWarnings("unchecked")
            List<Object[]> resultList = query.getResultList();
            Thread.sleep(800);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}