package io.github.herouu.beachmark;

import com.alibaba.druid.pool.DruidDataSource;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class DruidBenchmark {


    public static DruidDataSource init() {
        DruidDataSource ds = new DruidDataSource();
        ds.setUrl("jdbc:mysql://127.0.0.1:4406/test");
        ds.setUsername("root");
        ds.setPassword("123456");
        ds.setMinIdle(10);
        ds.setMaxActive(10);
        return ds;
    }

    public static void query(DruidDataSource dataSource) throws SQLException {
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
