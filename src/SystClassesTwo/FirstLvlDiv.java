package SystClassesTwo;

import SystClasses.DBConnection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


/**The FirstLvlDiv class governs the first level divisions described by the database. These objects are created using the respective country, division name, and division id then loaded into an array. Note the associative relationship with the Countries class.*/
public class FirstLvlDiv
{
    public static ArrayList<FirstLvlDiv> listOfDiv = new ArrayList<>();

    private  int divisionId;
    private  String division;
    private  int countryId;

    /**Constructor for the FirstLvlDiv class.
     * @param divisionId This is the id assigned to the division by the database.
     * @param countryId This is the id assigned to the respective country by the database.
     * @param division This is the name of the division.
     */
    public FirstLvlDiv(int divisionId, String division, int countryId)
    {
        this.countryId = countryId;
        this.division = division;
        this.divisionId = divisionId;

    }

    /**Method responsible for adding class objects to the array.*/
    public static void addToList(FirstLvlDiv tempDiv)
    {
        listOfDiv.add(tempDiv);
    }

    /**Getter for the country ID associated with the respective object.*/
    public int getCountryId()
    {
        return countryId;
    }
    /**Getter for the division ID.*/
    public int getDivisionId() {return divisionId;}
    /**Getter for the division name.*/
    public String getDivision()
    {
        return division;
    }
/**Essentially a modified getter method, this method takes a division id, identifies the respective division, and returns the respective name.*/
    public static String retDivNameFromId(int theDivId)
    {
        for (int i =0; i< listOfDiv.size();i++)
        {
            if(listOfDiv.get(i).divisionId == theDivId)
            {
                return listOfDiv.get(i).division;
            }
        }
        return null;
    }
/**The reverse of retDivNameFromId, this method takes a division name and returns the respective ID.*/
    public static int retDivIdFromName(String theDivName)
    {
        for (int i =0; i< listOfDiv.size();i++)
        {
            if(listOfDiv.get(i).getDivision().equals(theDivName))
            {
                return listOfDiv.get(i).getDivisionId();
            }
        }
        return -1;
    }

    /**The method responsible for pulling the divisions out of the database and loading the array. Note that it utilizes a SQL statement.*/
    public static void initListOfDiv()
    {
        try
        {
                String sql = "Select Division_ID, Division, COUNTRY_ID from first_level_divisions";
                PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
                ResultSet rs = ps.executeQuery();
                while (rs.next())
                {
                    addToList(new FirstLvlDiv(rs.getInt("Division_ID"), rs.getString("Division"), rs.getInt("COUNTRY_ID")));

                }
        }
        catch(SQLException e)
        {
            //caught
        }

    }
}
