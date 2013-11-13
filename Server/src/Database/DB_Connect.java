package Database;

import java.sql.*;

public class DB_Connect {

    private String host, database, user, password;
    private Connection theConnection;

    public DB_Connect(String host, String database, String user, String password) {
        this.host = host;
        this.database = database;
        this.user = user;
        this.password = password;
        this.theConnection = openDB(host, database, user, password);
    }

    public Connection openDB(String host, String db, String user, String password) {
        Connection erg = null;
        try {
            erg = DriverManager.getConnection("jdbc:mysql://" + host + ":3306/" + db, user, password);
            System.out.println("DB_Connect: Die Verbindung wurde hergestellt");
        } catch (SQLException ex) {
            System.err.println(ex.getMessage() + " Die Verbindung konnte nicht hergestellt werden!");
        }
        return erg;
    }

    public void closeDB() {
        if (!(theConnection == null)) {
            try {
                theConnection.close();
                theConnection = null;
            } catch (SQLException ex) {
                System.err.print(ex.getMessage());
            }
        }
    }

    public ResultSet executeSQLQuery(String command) {
        ResultSet erg = null;

        try {
            Statement stm = this.theConnection.createStatement();
            erg = stm.executeQuery(command);
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return erg;
    }

    public ResultSet executeSQLQuery(PreparedStatement stmt) {
        ResultSet erg = null;

        try {
            erg = stmt.executeQuery();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return erg;
    }

    public int executeSQLInsert(PreparedStatement pstmt) {
        int erg = -1;

        try {
            erg = pstmt.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex);
            erg = -1;
        }
        return erg;
    }

    public ResultSetMetaData getMetaData(ResultSet rs) {
        try {
            return rs.getMetaData();
        } catch (SQLException ex) {
            return null;
        }
    }

    public String getHost() {
        return host;
    }

    public String getDatabase() {
        return database;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public Connection getTheConnection() {
        return theConnection;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setTheConnection(Connection theConnection) {
        this.theConnection = theConnection;
    }
}
