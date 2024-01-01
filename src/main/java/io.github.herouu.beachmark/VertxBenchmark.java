package io.github.herouu.beachmark;

import cn.hutool.core.lang.Console;
import io.vertx.core.Vertx;
import io.vertx.mysqlclient.MySQLBuilder;
import io.vertx.mysqlclient.MySQLConnectOptions;
import io.vertx.sqlclient.Pool;
import io.vertx.sqlclient.PoolOptions;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;

import java.util.concurrent.TimeUnit;


public class VertxBenchmark {

    public static void query(Vertx vertx, Pool pool) {
        // Get a connection from the pool

        vertx.executeBlocking(call -> pool.getConnection().compose(conn -> {
            System.out.println("Got a connection from the pool");
            // All operations execute on the same connection
            return conn
                    .query("SELECT * FROM users WHERE id='julien'")
                    .execute()
                    .onComplete(ar -> {
                        // Release the connection to the pool
                        RowSet<Row> result = ar.result();
                        for (Row row : result) {
                            Console.log(row.getInteger("id"), row.getString("username"));
                        }
                        conn.close();
                    });
        }).onComplete(ar -> {
            if (ar.succeeded()) {
                System.out.println("Done");
            } else {
                System.out.println("Something went wrong " + ar.cause().getMessage());
            }
        }));

    }

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        Pool init = init(vertx);
        query(vertx, init);
    }

    public static Pool init(Vertx vertx) {

        MySQLConnectOptions connectOptions = new MySQLConnectOptions()
                .setPort(4406)
                .setHost("127.0.0.1")
                .setDatabase("test")
                .setUser("root")
                .setPassword("123456").setPipeliningLimit(16);

        PoolOptions poolOptions = new PoolOptions()
                .setMaxSize(10)
                .setEventLoopSize(10)
                // .setMaxWaitQueueSize(1000)
                .setIdleTimeout(10)
                .setConnectionTimeout(10)
                .setConnectionTimeoutUnit(TimeUnit.SECONDS)
                .setMaxLifetimeUnit(TimeUnit.SECONDS);

        return MySQLBuilder
                .pool()
                .with(poolOptions)
                .connectingTo(connectOptions)
                .using(vertx)
                .build();
    }
}
