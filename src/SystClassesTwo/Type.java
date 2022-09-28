package SystClassesTwo;

import SystClasses.DBConnection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**The Type class governs the Types of appointments. Specifically the type class loads various types from the db into an array and assigns an arbitrary index.*/
public class Type
{
    public static ArrayList<Type> listOfTypes = new ArrayList<>();

    private static int typeIndexCount = 1;


    private String typeOfApt;
    private int typeIndex;

/**The type constructor. The constructor takes a type parameter and creates a type object with that parameter and an index.*/
    public Type (String typeOfApt)
    {
        this.typeOfApt=typeOfApt;
        this.typeIndex=typeIndexCount++;
    }

/**The method that loads from the database. Note that it uses a SELECT SQL statement and exceptions are caught then ignored.*/
    public static void loadTypeFromDb()
    {
        if(listOfTypes.isEmpty()==false)
        {
            listOfTypes.clear();
        }
        try
        {
            String sql = "Select Type from appointments";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next())
            {
                addTypeToList(rs.getString("Type"));
            }
        }
        catch(SQLException e)
        {
            //caught
        }
    }

/**Adds non-duplicate types to the array. It is critical that this is used or else the array will have multiple index entries for identical types.
 * @param typeTemp the novel type to be added to the array duplicates are ignored.
 */
    private static void addTypeToList(String typeTemp)
    {
        int tempInt = 0;
        for (int i =0; i< listOfTypes.size(); i++)
        {
            if (listOfTypes.get(i).typeOfApt.equals(typeTemp))
            {
                tempInt = 1;
                break;
            }
        }
        if (tempInt ==0)
        {
            listOfTypes.add(new Type(typeTemp));
        }
    }
    /**Getter to return the type of appointment as a string.*/
    public String getTypeOfApt()
    {
        return typeOfApt;
    }
    /**Getter to return the index number.*/
    public int getTypeIndex()
    {
        return typeIndex;
    }
}
