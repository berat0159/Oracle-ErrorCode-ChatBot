package dbutil;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBUtil {
    public static Connection getConnection() throws SQLException {
        Properties properties = new Properties();
        try (InputStream input = DBUtil.class.getClassLoader().getResourceAsStream("resources/config.properties.txt")) {
            if (input == null) {
                throw new SQLException("Unable to find config.properties");
            }
            properties.load(input);
        } catch (IOException e) {
            throw new SQLException("Failed to load properties file", e);
        }

        String dbURL = properties.getProperty("db.url");
        String dbUserName = properties.getProperty("db.username");
        String dbPassword = properties.getProperty("db.password");

        return DriverManager.getConnection(dbURL, dbUserName, dbPassword);
    }
}
