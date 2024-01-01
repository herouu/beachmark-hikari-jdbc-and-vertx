package io.github.herouu.beachmark;

import com.alibaba.druid.pool.DruidDataSource;
import com.github.jasync.sql.db.mysql.MySQLConnection;
import com.github.jasync.sql.db.pool.ConnectionPool;
import com.zaxxer.hikari.HikariDataSource;
import io.vertx.core.Vertx;
import io.vertx.sqlclient.Pool;
import io.vertx.sqlclient.SqlClient;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.BenchmarkParams;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.sql.SQLException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;


@Threads(1)
@State(Scope.Thread)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
@Warmup(iterations = 10, time = 5)
@Measurement(iterations = 5, time = 5)
@Fork(value = 1, jvmArgs = {"-Xms6g", "-Xmx6g", "-Xmn6g"})
public class BenchMark {

    ConnectionPool<MySQLConnection> jasyncPool;
    private SqlClient sqlClient;
    private Pool init;
    private Vertx vertx;
    private HikariDataSource hikariDataSource;
    private DruidDataSource druidDataSource;

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(BenchMark.class.getSimpleName())
                .forks(1)
                .result("result.json")
                .resultFormat(ResultFormatType.JSON)
                .build();

        new Runner(opt).run();
    }

    @Setup(Level.Trial)
    public void setup(BenchmarkParams params) {
        vertx = Vertx.vertx();
        init = VertxBenchmark.init(vertx);
        jasyncPool = JasyncSqlBenchmark.init();
        hikariDataSource = HikariCpBenchmark.init();
        druidDataSource = DruidBenchmark.init();
    }

    @TearDown
    public void finish() {
        vertx.close();
        init.close();
        jasyncPool.disconnect();
        hikariDataSource.close();
        druidDataSource.close();
    }

    @Benchmark
    public void hikariCp(Blackhole bh) {
        try {
            HikariCpBenchmark.query(hikariDataSource);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Benchmark
    public void druid(Blackhole bh) {
        try {
            DruidBenchmark.query(druidDataSource);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // @Benchmark
    public void jasyncSqlBenchmark() {
        try {
            JasyncSqlBenchmark.query(jasyncPool);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    // @Benchmark
    public void vertx(Blackhole bh) {
        VertxBenchmark.query(vertx, init);
    }
}
