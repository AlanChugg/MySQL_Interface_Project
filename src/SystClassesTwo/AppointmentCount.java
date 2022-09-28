package SystClassesTwo;

import SystClasses.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.Timestamp;
import java.util.ArrayList;

/**This class is specifically designed to deal with counting appointments for the Report page. Initially a part of the Appointment class, it became more appropriate to have it as a stand alone class. Note the combinatorial nature of this class.*/
public class AppointmentCount
{
    public static ArrayList<AppointmentCount> listApptCount = new ArrayList<>();
    public static ObservableList<AppointmentCount> allApptCount = FXCollections.observableList(listApptCount);


    private String month;
    private Type typeForApptCount;
    private int count;


/**Constructor that takes a month string and a type object and from that creates an appointment count object that has an additional count variable. Note that this constructor is part of a combinatorial approach to populating the Appointment count list.
 * @param month The name of the month.
 * @param type The type object*/
    private AppointmentCount(String month,Type type)
    {
        this.month = month;
        this.typeForApptCount = type;
        count =0;
    }

/**Initializes the class array. This method creates month-type objects, adds them to the array, and then calls the setCountToList method.*/
    public static void initializeAppointmentCount()
    {
        if(allApptCount.isEmpty()==false)
        {
            allApptCount.clear();
        }
        for(int i = 0; i< MonthConversion.monthsOfYear.size(); i++)
        {
            for (int j=0; j< Type.listOfTypes.size();j++)
            {
                allApptCount.add(new AppointmentCount(MonthConversion.monthsOfYear.get(i).getMonth(),Type.listOfTypes.get(j)));
            }
        }
        setCountToList();
    }

    /**The focal point of the class, this private method parses the Appointment class array, matches the appointment objects with the respective appointment count objects, then increments the respective count variable accordingly.*/
    private static void setCountToList()
    {
        for (int i = 0; i<Appointment.allAppointment.size();i++)
        {
            Timestamp tempTime = Appointment.allAppointment.get(i).retStartDateNoMod();
            String tempMonth = DateTimeMgmt.convertLocalTimestampToMonth(tempTime);
            String theTypeIndex = Appointment.allAppointment.get(i).getType();

            for(int j=0;j<AppointmentCount.allApptCount.size();j++)
            {
                if(AppointmentCount.allApptCount.get(j).getMonth().equals(tempMonth) &&
                   AppointmentCount.allApptCount.get(j).getTypeForApptCount().equals(theTypeIndex))
                {
                    AppointmentCount.allApptCount.get(j).incrementCount();
                }
            }
        }
        removeZeroCountFromArray();
    }

    /**A private recursive method cleans up the Appointment count array before displaying as a tableview. Specifically this method removes all objects with a count equal to zero. Must be recursive due to the changing size of the array list.*/
    private static void removeZeroCountFromArray()
    {
        int recursionActive =0;
        for (int i=0; i<AppointmentCount.allApptCount.size();i++)
        {
            if(AppointmentCount.allApptCount.get(i).getCount()==0)
            {
                AppointmentCount.allApptCount.remove(i);
                recursionActive=1;
            }
        }
        if (recursionActive==1)
        {
            removeZeroCountFromArray();
        }
    }


/**Getter for the month name.
 * @return  The month name.*/
    public String getMonth(){return month;}
    /**Getter for the type name from the associated type object.
     * @return The name that is associated with the type object. */
    public String getTypeForApptCount(){return typeForApptCount.getTypeOfApt();}
    /**Getter for the appointment count
     * @return The count.*/
    public int getCount(){return count;}
    /**A private method to be used only with the setCountToList method and increments the count variable.*/
    private void incrementCount(){this.count++;}
    /**Getter for the type obect associated with the class.
     * @return The type object.*/
    public Type getType(){return typeForApptCount;}

}
