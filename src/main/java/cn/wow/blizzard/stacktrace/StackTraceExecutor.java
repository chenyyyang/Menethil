package cn.wow.blizzard.stacktrace;

import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class StackTraceExecutor {
    static final Logger logger = LoggerFactory.getLogger(StackTraceExecutor.class);

    static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5, 5, 100,
            TimeUnit.HOURS,
            new SynchronousQueue<>(),
            new ThreadPoolExecutor.CallerRunsPolicy());

    static HikariDataSource hikariDataSource = new HikariDataSource();

    static AtomicBoolean initOK = new AtomicBoolean(false);

    static {
        String url = "jdbc:mysql://localhost:3306/?useUnicode=true&characterEncoding=UTF-8";
        String username = "root";
        String password = "12345678";

        hikariDataSource.setJdbcUrl(url);
        hikariDataSource.setUsername(username);
        hikariDataSource.setPassword(password);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            hikariDataSource.shutdown();
            threadPoolExecutor.shutdown();
        }));
    }

    public static void persist(String className, String methodName, String collectStackStr, String classloaderName) {
        if (initOK.compareAndSet(false, true)) {
            initDBTable();
        }

        Thread thread = Thread.currentThread();
        String threadName = thread.getName();

        threadPoolExecutor.execute(()->{

            try (Connection connection = hikariDataSource.getConnection(); Statement statement = connection.createStatement()) {

                statement.execute("INSERT INTO Menethil.stacktrace " +
                        "(classloader,threadName,createdTime, stacktrace,`depth`,stackCrc) " +
                        "VALUES ('"+classloaderName+"','"+threadName+"','"+ LocalDateTime.now()+"','"
                        +collectStackStr +"','"+collectStackStr.split("\n").length
                        +"', CRC32('"+collectStackStr+"'))");
            } catch (Throwable ignore) {
            }

        });

    }

    private static void initDBTable() {
        String createDB = "CREATE DATABASE IF NOT EXISTS  `Menethil`  DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci ;\n";

        String createTable = "CREATE TABLE IF NOT EXISTS  Menethil.stacktrace ( classloader varchar(100) NOT NULL,\n" +
                "threadName varchar(100) NOT NULL,\n" +
                "createdTime varchar(100) NOT NULL,\n" +
                "stacktrace TEXT NOT NULL,\n" +
                "`depth` INT NOT NULL,\n" +
                "stackCrc varchar(100)NOT NULL,\n" +
                "CONSTRAINT stacktrace_PK PRIMARY KEY (threadName,\n" +
                "stackCrc) ) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;\n";
        try (Connection connection = hikariDataSource.getConnection(); Statement statement = connection.createStatement()) {
            statement.execute(createDB);
            statement.execute(createTable);
        } catch (Throwable throwables) {
            logger.error("[initDBTable]", throwables);
            throwables.printStackTrace();
        }
    }

}
