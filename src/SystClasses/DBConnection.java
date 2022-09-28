package SystClasses;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**Contains methods that connect and disconnect with the mySQL database.*/
public class DBConnection {


    private static final String protocol = "jdbc";
    private static final String vendorName =":mysql:";
    private static final String ipAddress = "redacted";
    private static final String dbName = "redacted";
    private static final String jdbcURL = protocol + vendorName + ipAddress + dbName;
    private static final String MYSQLJBCDriver = "com.mysql.jdbc.Driver";
    private static final String username = "redacted";
    private static final Long   prepassword = 1111111111111L;
    private static final String password = String.valueOf(prepassword);
    private static Connection conn = null;


    public static Connection startConnection()
    {
        try
        {
            System.out.println(password);
            Class.forName(MYSQLJBCDriver);
            conn = DriverManager.getConnection(jdbcURL, username, password);

        }
        catch (ClassNotFoundException | SQLException e)
        {
            e.printStackTrace();
        }

        return conn;
    }

    public static void closeConnection()
    {
        try
        {
            conn.close();
        }
        catch(Exception e)
        {
            //ignore
        }
    }

    public static Connection getConnection()
    {
        return conn;
    }

}
