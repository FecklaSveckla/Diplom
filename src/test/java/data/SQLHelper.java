package data;

import lombok.*;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;



public class SQLHelper {
    private static QueryRunner runner = new QueryRunner();

    private static final String url = System.getProperty("db.url");

    private SQLHelper(){};

    @SneakyThrows
    public static Connection getConn(){
        return DriverManager.getConnection(url, "app", "pass");
    }

    @SneakyThrows
    public static void cleanTables() {
        var connection = getConn();
        runner.execute(connection, "DELETE FROM credit_request_entity");
        runner.execute(connection, "DELETE FROM payment_entity");
        runner.execute(connection, "DELETE FROM order_entity");
    }

    @SneakyThrows
    public static String getPaymentStatus() {
        var status = "SELECT status FROM payment_entity";
        try (var conn = getConn()) {
            return runner.query(conn, status, new ScalarHandler<>());
        }
    }

    @SneakyThrows
    public static String getCreditStatus() {
        var status = "SELECT status FROM credit_request_entity";
        try (var conn = getConn()) {
            return runner.query(conn, status, new ScalarHandler<>());
        }
    }
}
