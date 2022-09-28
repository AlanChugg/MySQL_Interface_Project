package SystClasses;

import SystClassesTwo.*;

/**A one method class that initializes the program. Called by main.*/
public class SystemMgmt
{

    public static int LanguageIsFrench = 0;
    public static int UserHasBeenWarned = 0;



    public static void initSystem()
    {
        FirstLvlDiv.initListOfDiv();
        Countries.loadCountries();
        Customer.loadCustomerFromDb();
        Type.loadTypeFromDb();
        Contact.initContacts();
        Appointment.loadAppointmentsFromDb();
        AppointmentCount.initializeAppointmentCount();
    }

}
