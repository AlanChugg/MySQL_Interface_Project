package SystClasses;

import SystClassesTwo.FirstLvlDiv;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;
import java.util.ArrayList;

/**Class governing customer information.*/
public class Customer
{
    public static ArrayList<Customer> listCustomer = new ArrayList<>();
    public static ObservableList<Customer> allCustomer = FXCollections.observableList(listCustomer);



    private String name;
    private String address;
    private String postal;
    private String phone;
    private Timestamp createDate;
    private String createBy;
    private Timestamp updateDate;
    private String updateBy;
    private int division;
    private int custId;


    /**A somewhat complicated constructor that takes several fields of information and creates a customer object from them. Note the association with other object classes.*/
    public Customer(int custId, String name, String address, String postal, String phone,
                    int state, Timestamp createDate, String createBy, Timestamp updateDate, String updateBy)
    {
        this.name = name;
        this.custId = custId;
        this.address= address;
        this.postal = postal;
        this.phone = phone;
        this.createDate = createDate;
        this.createBy = createBy;
        this.updateDate = updateDate;
        this.updateBy = updateBy;
        this.division = state;


    }

    /**A method that looks up the creation date for a customer database entry.
     * @param theCustId The customer ID
     * @return A creation date timestamp.*/
    public static Timestamp lookUpCreateDate(int theCustId)
    {
        for (int i=0; i<listCustomer.size();i++)
        {
            if (listCustomer.get(i).getCustId() == theCustId)
            {
                return listCustomer.get(i).createDate;
            }
        }
        return null;
    }

    /**Used to look up who initially created the db entry from the customer ID.
     * @param theCustId The id assigned to the customer entry.
     * @return the name of the user who created it.*/
    public static String lookUpTheCreator(int theCustId)
    {
        for (int i=0; i<listCustomer.size();i++)
        {
            if (listCustomer.get(i).getCustId() == theCustId)
            {
                return listCustomer.get(i).createBy;
            }
        }
        return null;
    }

    /**Takes several fields of information and adds those fields to the SQL database. Note the use of an INSERT INTO SQL statement.*/
    public static void addCustomerToTheDb(String addPhone, String addAddress, String addPostal, String addName, String tempDivId)
    {
        int addDivId = FirstLvlDiv.retDivIdFromName(tempDivId);
        Timestamp tempCreate = new Timestamp(System.currentTimeMillis());
        Timestamp addCreate = DateTimeMgmt.convertLocalTimestampToUTCTimestamp(tempCreate);


        try
        {
            String sqli = "Insert into customers() values(Null,?,?,?,?,?,?,?,?,?)";
            PreparedStatement cust = DBConnection.getConnection().prepareStatement(sqli, Statement.RETURN_GENERATED_KEYS);

            cust.setString(1, addName);
            cust.setString(2,addAddress);
            cust.setString(3, addPostal);
            cust.setString(4, addPhone);
            cust.setTimestamp(5,addCreate);
            cust.setString(6, User.UserOfTheSystem);
            cust.setTimestamp(7, addCreate);
            cust.setString(8, User.UserOfTheSystem);
            cust.setInt(9,addDivId);

            cust.execute();
        }
        catch(Exception e)
        {
            //caught
        }

    }

    /**A method that takes several fields and uses those fields to modify an entry in the database. Note the UPDATE WHERE SQL statement.*/
    public static void modCustomerToTheDb(String addPhone, String addAddress, String addPostal, String addName, String tempDivId, int theCustId)
    {
        int addDivId = FirstLvlDiv.retDivIdFromName(tempDivId);
        Timestamp tempUpdate = new Timestamp(System.currentTimeMillis());
        Timestamp addUpdate = DateTimeMgmt.convertLocalTimestampToUTCTimestamp(tempUpdate);
        Timestamp addCreateDate = lookUpCreateDate(theCustId);
        String addTheCreator = lookUpTheCreator(theCustId);


        try
        {
            String sqli = "Update customers set Customer_Name=?, Address =?, Postal_Code=?, Phone=?, Create_Date=?, Created_By=?, Last_Update=?, Last_Updated_By=?, Division_ID=? WHERE Customer_ID=?";
            PreparedStatement cust = DBConnection.getConnection().prepareStatement(sqli, Statement.RETURN_GENERATED_KEYS);

            cust.setString(1, addName);
            cust.setString(2, addAddress);
            cust.setString(3, addPostal);
            cust.setString(4, addPhone);
            cust.setTimestamp(5, addCreateDate);
            cust.setString(6, addTheCreator);
            cust.setTimestamp(7, addUpdate);
            cust.setString(8, User.UserOfTheSystem);
            cust.setInt(9, addDivId);
            cust.setInt(10, theCustId);

            cust.execute();
        }
        catch(Exception e){}

    }

    /**A method that takes a customer object and uses information associated with that object to delete the corresponding database entry. Note the DELETE FROM WHERE statement.*/
    public static void delCustomerFromDb(Customer tempCustDelete)
    {
        for (int i =0; i<Appointment.allAppointment.size();i++)
        {
            if (Appointment.allAppointment.get(i).getCustPerson()== tempCustDelete.getCustId())
            {
                Appointment.deleteFromDb(Appointment.allAppointment.get(i));
            }
        }

        try
        {
            String sqli = "DELETE FROM customers WHERE Customer_ID=?";
            PreparedStatement custDel = DBConnection.getConnection().prepareStatement(sqli, Statement.RETURN_GENERATED_KEYS);
            int custDelId = tempCustDelete.getCustId();

            custDel.setInt(1, custDelId);
            custDel.execute();
        }
        catch(Exception e)
        {
            //caught
        }

    }

/**Loads information from the datase, creates objects with those fields, and then populates the class array.*/
    public static void loadCustomerFromDb()
    {
        if (!allCustomer.isEmpty()){allCustomer.clear();}

        try
        {
            allCustomer.clear();
            String sql = "Select * from customers";
            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next())
            {
                allCustomer.add(new Customer(rs.getInt("Customer_ID"),rs.getString("Customer_Name"),
                        rs.getString("Address"),rs.getString("Postal_Code"),
                        rs.getString("Phone"),rs.getInt("Division_ID"),rs.getTimestamp("Create_Date"),
                        rs.getString("Created_By"),rs.getTimestamp("Last_Update"),
                        rs.getString("Last_Updated_By")));
            }
        }
        catch(SQLException e)
        {
            //caught
        }
    }

    /**Getter for name.
     * @return name.*/
    public String getName()
    {
        return name;
    }
    /**Getter for address.
     * @return the address.*/
    public String getAddress()
    {
        return address;
    }
    /**Getter for the postal info.
     * @return the postal info.*/
    public String getPostal()
    {
        return postal;
    }
    /**Getter for the phone number.
     * @return the phone number.*/
    public String getPhone()
    {
        return phone;
    }
    public String getCreateBy()
    {
        return createBy;
    }
    public Timestamp getCreateDate()
    {
        return createDate;
    }
    public String getUpdateBy()
    {
        return updateBy;
    }
    public Timestamp getUpdateDate()
    {
        return updateDate;
    }
    public String getDivision(){return FirstLvlDiv.retDivNameFromId(division);}
    /**Getter for the division name.
     * @return the division where the customer lives.*/
    public int getDivision2()
    {
        return division;
    }
    /**Getter for the customer ID.
     * @return the Customer ID.*/
    public int getCustId()
    {
        return custId;
    }

}
