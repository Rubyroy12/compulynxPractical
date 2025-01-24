package ibrahim.compulynxtest.Utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnection {

    public static Connection getDBConnection() throws SQLException {
        String url = "jdbc:mariadb://localhost:3306/compulynx";
        String user = "ibrahim";
        String password = "kali";
        return DriverManager.getConnection(url, user, password);
    }

//        public static Connection getDBConnection() throws SQLException {
//            String url = "jdbc:sqlserver://localhost:1433;databaseName=compulynx;encrypt=true;trustServerCertificate=true";
//            String user = "ibrahim";
//            String password = "kali";
//            return DriverManager.getConnection(url, user, password);
//        }
}
