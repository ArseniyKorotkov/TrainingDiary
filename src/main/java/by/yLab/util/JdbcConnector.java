package by.yLab.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class JdbcConnector {

    public JdbcConnector() {
    }

    private static final String URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String USER_NAME = "postgres";
    private static final String PASSWORD = "ArsySQL";

    public static Connection getConnection() throws SQLException {
//        connection.prepareStatement("SET search_path TO diary_repository,public;");
        return DriverManager.getConnection(URL, USER_NAME, PASSWORD);
    }

    public static void updateBase() {
        try (Connection connection = getConnection()) {
            connection.prepareStatement("CREATE SCHEMA IF NOT EXISTS diary_repository;").executeUpdate();
            connection.prepareStatement("drop table if exists audit;").executeUpdate();
            connection.prepareStatement("drop table if exists diary_note;").executeUpdate();
            connection.prepareStatement("drop table if exists exercise;").executeUpdate();
            connection.prepareStatement("drop table if exists user_account;").executeUpdate();

            connection.prepareStatement("""
                    CREATE TABLE IF NOT EXISTS user_account(
                        id BIGSERIAL PRIMARY key,
                        firstname VARCHAR(128),
                        lastname VARCHAR(128) NOT NULL,
                        birthday DATE NOT NULL,
                        email VARCHAR(128) NOT NULL,
                        registration DATE  NOT NULL,
                        unique (lastname, email)
                    );
                    """).executeUpdate();

            connection.prepareStatement("""
                    CREATE TABLE IF NOT EXISTS exercise(
                        id BIGSERIAL PRIMARY key,
                        user_id BIGINT  NOT NULL REFERENCES user_account,
                        exercise_name VARCHAR(128) NOT NULL ,
                        calories INT NOT NULL,
                        unique (user_id, exercise_name)
                    );
                    """).executeUpdate();

            connection.prepareStatement("""
                    CREATE TABLE IF NOT EXISTS diary_note(
                        id BIGSERIAL PRIMARY key,
                        user_id BIGINT  NOT NULL REFERENCES user_account,
                        exercise_id BIGINT NOT NULL REFERENCES exercise,
                        count INT NOT NULL,
                        exercise_date_time TIMESTAMP,
                        exercise_date DATE NOT NULL,
                        unique (exercise_id, exercise_date)
                    );
                    """).executeUpdate();

            connection.prepareStatement("""
                    CREATE TABLE IF NOT EXISTS audit(
                        id BIGSERIAL PRIMARY key,
                        user_id BIGINT  NOT NULL REFERENCES user_account,
                        action_date_time TIMESTAMP NOT NULL,
                        action_date DATE NOT NULL,
                        action VARCHAR(128) NOT NULL
                    );
                    """).executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
