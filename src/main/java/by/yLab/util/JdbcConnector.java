package by.yLab.util;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Optional;

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
    private static final String CHANGELOG_PATH = "db.changelog/changelog.xml";

    private static Connection connection;

    private static final String URL = "jdbc:postgresql://localhost:5433/postgres";
    private static final String USER_NAME = "postgres";
    private static final String PASSWORD = "ArsySQL";

    public JdbcConnector() {
    }

    /**
     * Создание соединения с базой данных
     *
     * @return соединение с базой данных
     */
    public Optional<Connection> getConnection() {
        Optional<Connection> connectionOptional = Optional.ofNullable(connection);
        if(connectionOptional.isEmpty()) {
            try {
                connection = DriverManager.getConnection(URL, USER_NAME, PASSWORD);
                connection.prepareStatement(CHANGE_DEFAULT_SCHEMA_SQL).executeUpdate();
                connectionOptional = Optional.of(connection);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return connectionOptional;
    }

    /**
     * Инициализация базы данных
     * @param connection соединение с базой данных
     * @throws SQLException неудача создания схемы базы данных
     */
    public static void initDatabaseLiquibase (Connection connection) throws SQLException {
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
}
