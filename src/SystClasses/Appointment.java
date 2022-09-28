package SystClasses;

import SystClassesTwo.Contact;
import SystClassesTwo.Type;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;
import java.text.ParseException;
import java.util.ArrayList;

/**Class for the appointment objects. Responsible for the creation of appointment objects as well as managing db interaction involving appointment related fields.*/
public class Appointment
{
    public static ArrayList<Appointment> listAppointment = new ArrayList<>();
    public static ObservableList<Appointment> allAppointment = FXCollections.observableList(listAppointment);


    private int aptId;
    private String title;
    private String desc;
    private String location;
    private Type type;
    private Timestamp startTime;
    private Timestamp endTime;
    private Timestamp createDate;
    private String createdBy;
    private Timestamp lastUpdate;
    private String lastUpdatedBy;
    private Customer custPerson;
    private int userId;
    private Contact contactPerson;

/**A somewhat involved constructor that uses a myriad of fields to construct a Appointment object. This method throws a parse exception that is unhandled. Note that if a new type is entered by the user, it will automatically call the type constructor and add that new type object to the type array.*/
    public Appointment(int aptId, String title, String desc, String location, String type, Timestamp startTime, Timestamp endTime, Timestamp createDate,
                       String createdBy, Timestamp lastUpdate, String lastUpdatedBy, int custPerson, int userId, int contactPerson) throws ParseException {

        this.aptId = aptId;
        this.title= title;
        this.desc = desc;
        this.location = location;
        this.startTime = startTime;
        this.endTime = endTime;
        this.createDate=createDate;
        this.createdBy=createdBy;
        this.lastUpdate=lastUpdate;
        this.lastUpdatedBy=lastUpdatedBy;
        this.userId = userId;

        int newTypeEntered =1;

        for(int i =0; i<Type.listOfTypes.size(); i++)
        {
            if(Type.listOfTypes.get(i).getTypeOfApt().equals(type))
            {
                this.type = Type.listOfTypes.get(i);
                newTypeEntered=0;
                break;
            }

        }
        if(newTypeEntered == 1)
        {
            Type.listOfTypes.add(new Type (type));

            for(int i =0; i<Type.listOfTypes.size(); i++)
            {
                if(Type.listOfTypes.get(i).getTypeOfApt().equals(type))
                {
                    this.type = Type.listOfTypes.get(i);
                    break;
                }
            }
        }

        for (int i =0; i<Customer.allCustomer.size(); i++)
        {
            if (Customer.allCustomer.get(i).getCustId() == custPerson)
            {
                this.custPerson = Customer.allCustomer.get(i);
                break;
            }
        }

        for (int i =0; i<Contact.listOfContacts.size(); i++)
        {
            if(Contact.listOfContacts.get(i).getContactId()== contactPerson)
            {
                this.contactPerson = Contact.listOfContacts.get(i);
            }
        }
    }

    /**Getter for appointment id.
     * @return appointment ID.*/
    public int getAptId(){return aptId; }
    /**Getter for appointment title.
     * @return appointment title.*/
    public String getTitle(){return title;}
    /**Getter for appointment description.
     * @return appointment description.*/
    public String getDesc(){return desc;}
    /**Getter for appointment location.
     * @return appointment location.*/
    public String getLocation(){return location;}
    /**Getter for appointment type.
     * @return appointment type.*/
    public String getType()
    {
        return type.getTypeOfApt();
    }
    /**Getter for appointment start time and date.
     * @return appointment start time and date.*/
    public Timestamp getStartTime()
    {
        return startTime;
    }
    /**Getter for appointment end time and date.
     * @return appointment end time and date.*/
    public Timestamp getEndTime()
    {
        return endTime;
    }
    /**An alternative getter that can be used for zone id modifications in future updates.*/
    public Timestamp retStartDateNoMod()
    {
        return startTime;
    }
    public Timestamp getCreateDate(){return createDate;}
    public String getCreatedBy(){return createdBy;}
    public Timestamp getLastUpdate(){return lastUpdate;}
    public String getLastUpdatedBy(){return lastUpdatedBy;}
    /**Getter for customer ID.
     * @return the customer ID associated with the appointment.*/
    public int getCustPerson(){return custPerson.getCustId();}
    /**Getter for user ID.
     * @return the user ID associated with the appointment.*/
    public int getUserId()
    {
        return userId;
    }
    public int getContactPerson(){return contactPerson.getContactId();}
    /**Getter for customer name.
     * @return the customer name associated with the appointment.*/
    public String getName(){return custPerson.getName();}
    /**Getter for contact name.
     * @return the contact's name associated with the appointment.*/
    public String getContactPersonName(){return contactPerson.getContactName();}

    /**Method responsible for adding a new appointment to the database. Will generate timestamps from entered strings and a current timestamp for database entry. Note the INSERT INTO VALUES SQL statement.*/
    public static void addToDb(int theCustomerID,String theType,String theTitle,String theContact,String theLocation,String theDescription,String theStartDate,String theEndDate, int theUserID)
    {
        try
        {

            int theIdOfContact = Contact.retIdFromContactName(theContact);
            Timestamp startDateDb = Timestamp.valueOf(theStartDate);
            Timestamp endDateDb = Timestamp.valueOf(theEndDate);

            Timestamp theTimeTemp = new Timestamp(System.currentTimeMillis());
            Timestamp theFinalTime = theTimeTemp;

            String sqli = "Insert into appointments() values(Null,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement aptInsert = DBConnection.getConnection().prepareStatement(sqli, Statement.RETURN_GENERATED_KEYS);
            aptInsert.setString(1,theTitle);
            aptInsert.setString(2,theDescription);
            aptInsert.setString(3,theLocation);
            aptInsert.setString(4,theType);
            aptInsert.setTimestamp(5,startDateDb);
            aptInsert.setTimestamp(6,endDateDb);
            aptInsert.setTimestamp(7,theFinalTime);
            aptInsert.setString(8,User.UserOfTheSystem);
            aptInsert.setTimestamp(9,theFinalTime);
            aptInsert.setString(10,User.UserOfTheSystem);
            aptInsert.setInt(11,theCustomerID);
            aptInsert.setInt(12,theUserID);
            aptInsert.setInt(13,theIdOfContact);
            aptInsert.execute();
        }
        catch(Exception e){}
    }

    public static void modifyToDb(int theCustomerID,String theType,String theTitle,String theContact,String theLocation,String theDescription,String theStartDate,String theEndDate, int theApptID, int theUserID)
    {
        try
        {

            int theIdOfContact = Contact.retIdFromContactName(theContact);
            Timestamp startDateDb = DateTimeMgmt.convertLocalTimestampToUTCTimestamp(Timestamp.valueOf(theStartDate));
            Timestamp endDateDb = DateTimeMgmt.convertLocalTimestampToUTCTimestamp(Timestamp.valueOf(theEndDate));

            Timestamp theTimeTemp = new Timestamp(System.currentTimeMillis());
            Timestamp theFinalTime = DateTimeMgmt.convertLocalTimestampToUTCTimestamp(theTimeTemp);


            String sqli = "Update appointments set Title=?, Description=?, Location=?, Type=?, Start =?, End=?, Last_Update=?, Last_Updated_By=?, Customer_ID=?, User_ID=?, Contact_ID=? WHERE Appointment_ID =?";
            PreparedStatement aptInsert = DBConnection.getConnection().prepareStatement(sqli, Statement.RETURN_GENERATED_KEYS);
            aptInsert.setString(1,theTitle);
            aptInsert.setString(2,theDescription);
            aptInsert.setString(3,theLocation);
            aptInsert.setString(4,theType);
            aptInsert.setTimestamp(5,startDateDb);
            aptInsert.setTimestamp(6,endDateDb);
            aptInsert.setTimestamp(7,theFinalTime);
            aptInsert.setString(8,User.UserOfTheSystem);
            aptInsert.setInt(9,theCustomerID);
            aptInsert.setInt(10,theUserID);
            aptInsert.setInt(11,theIdOfContact);
            aptInsert.setInt(12, theApptID);
            aptInsert.execute();
        }
        catch(Exception e){}
    }

    public static void deleteFromDb(Appointment delAppt)
    {
        try
        {
            String sqli = "DELETE FROM appointments WHERE Appointment_ID=?";
            PreparedStatement aptDel = DBConnection.getConnection().prepareStatement(sqli, Statement.RETURN_GENERATED_KEYS);
            int theApptId= delAppt.getAptId();

            aptDel.setInt(1, theApptId);
            aptDel.execute();
        }
        catch(Exception e)
        {
            //caught
        }
    }

    /**Uses a select SQL statment to load appointments from the database.*/
    public static void loadAppointmentsFromDb()
    {
        try
        {
            allAppointment.clear();
            String sql = "Select * from appointments";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next())
            {
                allAppointment.add(new Appointment (rs.getInt("Appointment_ID"),
                                                    rs.getString("Title"),
                                                    rs.getString("Description"),
                                                    rs.getString("Location"),
                                                    rs.getString("Type"),
                                                    rs.getTimestamp("Start"),
                                                    rs.getTimestamp("End"),
                                                    rs.getTimestamp("Create_Date"),
                                                    rs.getString("Created_By"),
                                                    rs.getTimestamp("Last_Update"),
                                                    rs.getString("Last_Updated_By"),
                                                    rs.getInt("Customer_ID"),
                                                    rs.getInt("User_ID"),
                                                    rs.getInt("Contact_ID")
                                                   ));
            }

        }
        catch(SQLException | ParseException e)
        {
            //caught
        }
    }


}




