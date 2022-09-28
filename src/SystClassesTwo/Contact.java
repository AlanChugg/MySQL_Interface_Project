package SystClassesTwo;

import SystClasses.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**This class governs the Contact names as described by the database. Note that contact emails are fetched but not used by the program. These emails can be utilized in a future update.*/
public class Contact
{
    public static ArrayList<Contact> listOfContacts = new ArrayList<>();
    public static ArrayList<String> contactNames = new ArrayList<>();
    public static ObservableList<String> allContactNames = FXCollections.observableList(contactNames);


    private int contactId;
    private String contactName;
    private String contactEmail;

/**Class constructor that assigns the components of the object with their respective parameters.
 * @param contactId The id number of the contact assigned by the database.
 * @param contactName The name of the contact.
 * @param contactEmail The unused email address of the contact.*/
    private Contact (int contactId, String contactName, String contactEmail)
    {
        this.contactId = contactId;
        this.contactName = contactName;
        this.contactEmail = contactEmail;
    }

    /**Fetches contact information from the database and adds the respective object to the contact array. Note the use of a SELECT SQL statement. Exceptions are caught and ignored.*/
    public static void initContacts()
    {
        try
        {
            String sql = "Select * from contacts";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next())
            {
                listOfContacts.add(new Contact(rs.getInt("Contact_ID"), rs.getString("Contact_Name"), rs.getString("Email")));
            }
        }
        catch(SQLException e)
        {
            //caught
        }
        for(int i=0; i< Contact.listOfContacts.size();i++)
        {
            allContactNames.add(Contact.listOfContacts.get(i).getContactName());
        }
    }

    /**This method returns the respective id number from the contact name. Will cause problems and need adjustment if more that one contact has the same name.
     * @param thePersonsName The name of the contact.
     * @return Returns the id of the respective contact.*/
    public static Integer retIdFromContactName(String thePersonsName)
    {
        int thePersonsId=0;

        for (int i =0; i<listOfContacts.size(); i++)
        {
            if (listOfContacts.get(i).getContactName().equals(thePersonsName))
            {
                thePersonsId = listOfContacts.get(i).getContactId();
            }
        }
        return thePersonsId;
    }
/**Getter for the contact name.
 * @return The contact name.*/
    public String getContactName()
    {
        return contactName;
    }
    /**The contact ID.
     * @return The contact ID.*/
    public int getContactId()
    {
        return contactId;
    }

}
