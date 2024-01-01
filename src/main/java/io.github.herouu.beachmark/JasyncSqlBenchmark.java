package io.github.herouu.beachmark;

import com.github.jasync.sql.db.Connection;
import com.github.jasync.sql.db.ConnectionPoolConfiguration;
import com.github.jasync.sql.db.ConnectionPoolConfigurationBuilder;
import com.github.jasync.sql.db.QueryResult;
import com.github.jasync.sql.db.mysql.MySQLConnection;
import com.github.jasync.sql.db.mysql.MySQLConnectionBuilder;
import com.github.jasync.sql.db.pool.ConnectionPool;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * <p>
 *
 * </p>
 *
 * @author fuqiang
 * @since 2024-01-02
 */
public class JasyncSqlBenchmark {


    public static void main(String[] args) {


        ;

    }

    public static ConnectionPool<MySQLConnection> init() {
        ConnectionPoolConfigurationBuilder builder = new ConnectionPoolConfigurationBuilder("127.0.0.1", 4406,
                "test", "root", "123456");
        builder.setMinIdleConnections(10);
        builder.setMaxActiveConnections(10);
        ConnectionPoolConfiguration build = builder.build();
        return MySQLConnectionBuilder.createConnectionPool(build);
    }


    public static void query(ConnectionPool<MySQLConnection> pool) throws ExecutionException, InterruptedException {
        Connection connection = pool.connect().get();
        CompletableFuture<QueryResult> future = connection.sendPreparedStatement("select * from users where id = 1");
        future.get();
    }
}
