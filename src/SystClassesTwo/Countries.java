package SystClassesTwo;

import SystClasses.DBConnection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


/**This class governs the countries described by the database. The respective objects are composed of a country ID, Name, and the list of local divisions of that country. Note the associative relationship with the FirstLvlDiv class.*/
public class Countries
{
    public static ArrayList<Countries> listOfCountries = new ArrayList<>();

    private int countryId;
    private String countryName;
    private ArrayList<FirstLvlDiv> listOfLocalDiv = new ArrayList<>();

/**Constructor for countries objects. This constructor takes the id and name, then creates an array of FirstLvlDiv objects that contain that country id.*/
    public Countries(int countryId, String countryName)
    {
        this.countryId = countryId;
        this.countryName = countryName;

        for (int i = 0; i < FirstLvlDiv.listOfDiv.size(); i++)
        {
            if (countryId == FirstLvlDiv.listOfDiv.get(i).getCountryId())
            {
                this.listOfLocalDiv.add(FirstLvlDiv.listOfDiv.get(i));
            }
        }
    }

    public int getCountryId()
    {
        return countryId;
    }
    /**Getter that returns country name.
     * @return the name.
     * */
    public String getCountryName(){return countryName;}
    /**Getter for the list of local division objects from the FirstLvlDiv class.
     * @return an array of the respective FirstlvlDiv objects.*/
    public ArrayList getListOfDiv(){return listOfLocalDiv;}

/**This method is used to look up a specific division associated with a specific country. Note the dependence on the country object.
 * @param index the index of the division
 * @return the name of the division with that index parameter.*/
    public String getSpecificDiv(int index)
    {
        if (index < listOfLocalDiv.size())
        {
            return listOfLocalDiv.get(index).getDivision();
        }
        return null;
    }

    /**Method used to add countries objects to the countries array.*/
    public static void addToList(Countries tempCountry)
    {
        listOfCountries.add(tempCountry);
    }

/**Initialization method that loads countries from the database. Note that this method utilizes a SELECT SQL statement.*/
    public static void loadCountries()
    {
        try
        {
            String sql = "Select Country, Country_ID from countries";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next())
            {
                addToList(new Countries(rs.getInt("Country_ID"), rs.getString("Country")));

            }
        }
        catch(SQLException e)
        {
            //caught
        }
    }


/**This method is used to look up a country name from a division located in that country.
 * @param theDivId The index of the division associated with a specific country.
 * @return The name of the country that division is a part of.*/
    public static String retCountryNameFromDivId(int theDivId)
    {
        for (int i =0; i< listOfCountries.size(); i++)
        {
            for (int j=0; j< listOfCountries.get(i).listOfLocalDiv.size();j++ )
            {
                if (listOfCountries.get(i).listOfLocalDiv.get(j).getDivisionId()== theDivId)
                {
                    return listOfCountries.get(i).getCountryName();
                }
            }
        }
        return null;
    }

}
