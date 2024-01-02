package io.github.herouu.beachmark;

import io.vertx.core.Vertx;
import io.vertx.mysqlclient.MySQLBuilder;
import io.vertx.mysqlclient.MySQLConnectOptions;
import io.vertx.sqlclient.Pool;
import io.vertx.sqlclient.PoolOptions;

import java.util.concurrent.ExecutionException;


public class VertxBenchmark {

    public static void query(Pool pool) throws ExecutionException, InterruptedException {
        pool.getConnection().compose(conn -> conn.query("SELECT * FROM users WHERE id = 1")
                .execute().onComplete(ar -> {
                    conn.close();
                })).toCompletionStage().toCompletableFuture().get();
    }

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        Pool init = init(vertx);
        try {
            query(init);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static Pool init(Vertx vertx) {

        MySQLConnectOptions connectOptions = new MySQLConnectOptions()
                .setPort(BaseBenchmark.PORT)
                .setHost(BaseBenchmark.HOST)
                .setDatabase(BaseBenchmark.DB)
                .setUser(BaseBenchmark.USER_NAME)
                .setPassword(BaseBenchmark.PASSWORD)
                .setPipeliningLimit(16);

        PoolOptions poolOptions = new PoolOptions()
                .setMaxSize(10);

        return MySQLBuilder
                .pool()
                .with(poolOptions)
                .connectingTo(connectOptions)
                .using(vertx)
                .build();
    }
}
