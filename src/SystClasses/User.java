package SystClasses;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**This class is responsible for all things User oriented. Specifically this class controls log in and password recognition and stores the logged in user's name and Id.*/
public class User
{
    public static String UserOfTheSystem = "";
    public static int UserOfTheSystemId = 0;
    private String userName;
    private String password;
    private int userId;
    private static ArrayList<User> credentialList = new ArrayList<>();
    public static ArrayList<User> userList = new ArrayList<>();


/**An overloaded constructor that is only to be used in methods involving password recognition.*/
    private User(String userName, String password, int userId)
    {
        this.userName = userName;
        this.password = password;
        this.userId= userId;
    }
    /**Overloaded constructor used to populate the array of users. Specifically this constructor will create objects used to correlate appointment with usernames.*/
    public User(String userName, int userId)
    {
        this.userName = userName;
        this.userId= userId;
    }
    /**Overloaded constructor that is only to be used when an unidentified person is attempting to log in to the system.*/
    public User (String userName, String password)
    {
        this.userName = userName;
        this.password = password;
    }

/**Method involved in username and password recognition. This method populates a two lists, one with passwords, one without, contrasts log in credentials, and then removes objects containing a password for safety reasons.
 * @return true if credentials match what is in the database.
 * @return false otherwise.*/
    public static boolean logInCheck(User unknownPer)
    {
        if (credentialList.isEmpty() == false)
        {
            credentialList.clear();
        }
        try
        {
            String sql = "Select User_ID, User_Name, Password from users" ;
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next())
            {
               credentialList.add(new User(rs.getString("User_Name"), rs.getString("Password"), rs.getInt("User_ID")));
               userList.add(new User(rs.getString("User_Name"), rs.getInt("User_ID")));
            }
        }
        catch(SQLException e)
        {
            //caught
        }

        for (int i = 0; i < credentialList.size(); i++)
        {
            if (unknownPer.userName.equals(credentialList.get(i).userName) && unknownPer.password.equals(credentialList.get(i).password))
            {
                UserOfTheSystem = userList.get(i).getUserName();
                UserOfTheSystemId = userList.get(i).getUserId();
                credentialList.clear();
                return true;
            }
        }
        credentialList.clear();
        return false;
    }

/**Getter for the username.
 * @return username.*/
    public String getUserName()
    {
        return userName;
    }
    /**Getter for the user id.
     * @return user ID.*/
    public int getUserId()
    {
        return userId;
    }
}
