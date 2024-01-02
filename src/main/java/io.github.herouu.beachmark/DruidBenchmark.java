package io.github.herouu.beachmark;

import com.alibaba.druid.pool.DruidDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class DruidBenchmark {


    public static DruidDataSource init() {
        DruidDataSource ds = new DruidDataSource();
        ds.setUrl(BaseBenchmark.JDBC_URL);
        ds.setUsername(BaseBenchmark.USER_NAME);
        ds.setPassword(BaseBenchmark.PASSWORD);
        ds.setMinIdle(10);
        ds.setMaxActive(10);
        return ds;
    }


    public static void query(DruidDataSource dataSource) {
        try (Connection con = dataSource.getConnection();
             PreparedStatement pst = con.prepareStatement(BaseBenchmark.SQL_QUERY);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                // Console.log(rs.getString(1),rs.getString(2));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws SQLException {
        query(init());
    }

}
