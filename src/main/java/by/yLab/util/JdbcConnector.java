package by.yLab.util;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Properties;

/**
 * Управление соединениями с базой данных
 */
public class JdbcConnector {

    private static final String CREATE_DIARY_SCHEMA_SQL = """
            CREATE SCHEMA IF NOT EXISTS diary_repository;
            """;

    private static final String CHANGE_DEFAULT_SCHEMA_SQL = """
            SET search_path TO diary_repository,public;
            """;
    private static final String CHANGELOG_PATH = getPropertyValue("cl.changelog_path");

    private static Connection connection;

    private static Properties PROPERTIES;
    private static final String URL = getPropertyValue("db.url");
    private static final String USER_NAME = getPropertyValue("db.user");
    private static final String PASSWORD = getPropertyValue("db.password");

    public JdbcConnector() {
    }

    /**
     * Создание соединения с базой данных
     *
     * @return соединение с базой данных
     */
    public Optional<Connection> getConnection() {
        Optional<Connection> connectionOptional = Optional.ofNullable(connection);
        if (connectionOptional.isEmpty()) {
            while (true) {
                try {
                    connection = DriverManager.getConnection(URL, USER_NAME, PASSWORD);
                    connection.prepareStatement(CHANGE_DEFAULT_SCHEMA_SQL).executeUpdate();
                    connectionOptional = Optional.of(connection);
                    break;
                } catch (SQLException e) {
                    try {
                        Thread.sleep(20L);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                    e.printStackTrace();
                }
            }
        }
        return connectionOptional;
    }

    /**
     * Инициализация базы данных
     *
     * @param connection соединение с базой данных
     * @throws SQLException неудача создания схемы базы данных
     */
    public static void initDatabaseLiquibase(Connection connection) throws SQLException {
        connection.prepareStatement(CREATE_DIARY_SCHEMA_SQL).executeUpdate();
        Database correctDatabaseImplementation;
        try {
            correctDatabaseImplementation = DatabaseFactory
                    .getInstance()
                    .findCorrectDatabaseImplementation(new JdbcConnection(connection));
            Liquibase liquibase = new Liquibase(CHANGELOG_PATH,
                    new ClassLoaderResourceAccessor(),
                    correctDatabaseImplementation);
            liquibase.update();
        } catch (LiquibaseException e) {
            e.printStackTrace();
        }
    }

    public static String getPropertyValue(String property) {
        if(PROPERTIES == null) {
            PROPERTIES = new Properties();
        }
        try( InputStream resourceAsStream = JdbcConnector.class
                .getClassLoader()
                .getResourceAsStream("application.properties")) {
            PROPERTIES.load(resourceAsStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return PROPERTIES.getProperty(property);
    }
}
