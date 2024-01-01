package io.github.herouu.beachmark;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class HikariCpBenchmark {


    public static HikariDataSource init() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://127.0.0.1:4406/test");
        config.setUsername("root");
        config.setPassword("123456");
        config.setMinimumIdle(10);
        return new HikariDataSource(config);
    }

    public static void query(HikariDataSource dataSource) throws SQLException {
        String SQL_QUERY = "select * from users where id = 1";
        try (Connection con = dataSource.getConnection();
             PreparedStatement pst = con.prepareStatement(SQL_QUERY);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                // Console.log(rs.getString(1),rs.getString(2));
            }
        }
    }

    public static void main(String[] args) throws SQLException {
        query(init());
    }

}
