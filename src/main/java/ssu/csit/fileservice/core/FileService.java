package ssu.csit.fileservice.core;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FileService implements Closeable {

    private static final String DB_NAME = "file_hash.db";
    private Connection connection;

    public FileService() {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + DB_NAME);
            connection
                    .prepareStatement(
                            "CREATE TABLE IF NOT EXISTS HASHES(PATH VARCHAR(256) NOT NULL PRIMARY KEY, HASH VARCHAR(128) NOT NULL)")
                    .execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getHashForFile(File file) {
        String hash = null;
        try {
            PreparedStatement select = connection.prepareStatement("SELECT HASH FROM HASHES WHERE PATH = ?");
            select.setString(1, file.getPath());
            ResultSet result = select.executeQuery();
            if (!result.next()) {
                return null;
            }
            hash = result.getString("hash");
            result.close();
            select.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return hash;
    }

    public String getFileForHash(String hash) {
        String file = null;
        try {
            PreparedStatement select = connection.prepareStatement("SELECT PATH FROM HASHES WHERE HASH = ?");
            select.setString(1, hash);
            ResultSet result = select.executeQuery();
            if (!result.next()) {
                return null;
            }
            file = result.getString("path");
            result.close();
            select.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return file;
    }

    public void addFile(String path, String hash) {
        try {
            PreparedStatement sql = connection.prepareStatement("INSERT INTO HASHES VALUES(?, ?)");
            sql.setString(1, path);
            sql.setString(2, hash);
            sql.executeUpdate();
            sql.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateFile(String column, String file, String hash) {
        try {
            boolean updateHash = column.equalsIgnoreCase("hash");
            PreparedStatement sql = connection.prepareStatement(String.format("UPDATE HASHES SET %s = ? WHERE %s = ?",
                    column, updateHash ? "PATH" : "HASH"));
            sql.setString(1, updateHash ? hash : file);
            sql.setString(2, updateHash ? file : hash);
            sql.executeUpdate();
            sql.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void close() throws IOException {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
