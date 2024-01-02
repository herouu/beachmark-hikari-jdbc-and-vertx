package io.github.herouu.beachmark;

/**
 * <p>
 *
 * </p>
 *
 * @author fuqiang
 * @since 2024-01-02
 */
public interface BaseBenchmark {

    String SQL_QUERY = "select * from users where id = 1";

    String JDBC_URL = "jdbc:mysql://127.0.0.1:4406/test";

    String USER_NAME = "root";

    String PASSWORD = "123456";

    String DB = "test";

    String HOST = "127.0.0.1";

    int PORT = 4406;

}
