package GuiControllersMain;

import SystClasses.*;
import SystClassesTwo.Contact;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.sql.Timestamp;
import java.time.*;

/**Responsible for the add appointment screen.*/
public class AddApptController {

    @FXML
    private ComboBox<String> ComboContact;
    @FXML
    private TextField TxtLocation;
    @FXML
    private Button ButtonCancel;
    @FXML
    private DatePicker DateStart;
    @FXML
    private RadioButton RadioPmStart;
    @FXML
    private TextField ButtonCustId;
    @FXML
    private ToggleGroup EndTimeRadio;
    @FXML
    private Label LabelApptID;
    @FXML
    private TextField TxtType;
    @FXML
    private TextField TxtStartTime;
    @FXML
    private RadioButton RadioAmStart;
    @FXML
    private TextArea TxtBxDesc;
    @FXML
    private TextField TxtTitle;
    @FXML
    private TextField TxtEndTime;
    @FXML
    private ToggleGroup StartTimeRadio;
    @FXML
    private RadioButton RadioAmEnd;
    @FXML
    private DatePicker ComboEndDate;
    @FXML
    private TextField TxtCustomerName;
    @FXML
    private RadioButton RadioPmEnd;
    @FXML
    private Button ButtonSave;
    @FXML
    private TextField txtUserID;

    /**Unused.*/
    @FXML
    void TxtTypeOnAction(ActionEvent event) {}
    /**Unused.*/
    @FXML
    void TxtTitleOnAction(ActionEvent event) {}
    /**Unused.*/
    @FXML
    void TxtLocationOnAction(ActionEvent event) {}
    /**Unused.*/
    @FXML
    void TxtStartTimeOnAction(ActionEvent event) {}
    /**Unused.*/
    @FXML
    void TxtEndTimeOnAction(ActionEvent event) {}
    /**Unused.*/
    @FXML
    void ComboContactOnAction(ActionEvent event) {}
    /**Unused.*/
    @FXML
    void DateStartOnAction(ActionEvent event) {}
    /**Unused.*/
    @FXML
    void ComboEndDateOnAction(ActionEvent event) {}
    /**Unused.*/
    @FXML
    void RadioAmStartOnAction(ActionEvent event) {}
    /**Unused.*/
    @FXML
    void RadioPmStartOnAction(ActionEvent event) {}
    /**Unused.*/
    @FXML
    void RadioPmEndOnAction(ActionEvent event) {}
    /**Unused.*/
    @FXML
    void RadioAmEndOnAction(ActionEvent event) {}
    /**Unused.*/
    @FXML
    void ButtonCustIdOnAction(ActionEvent event) {}
    /**Unused.*/
    @FXML
    void TxtUserIDOnAction(ActionEvent event) {}


    /**LAMBDA USE #2(Duplicate) Same lambda structure used in Modify Appointments these lambdas interact with the Error Handler interface to assist exception handling. See modify Appointments for a description.*/
    @FXML
    void ButtonSaveOnAction(ActionEvent event)
    {
        int thereIsAProblem =0;

        try
        {
            String theDateStart = DateStart.getValue().toString();
            String theDateEnd   = ComboEndDate.getValue().toString();

            thereIsAProblem =1;

            String timeStart = null;
            if(RadioAmStart.isSelected())
            {
                timeStart = String.valueOf(TxtStartTime.getText());
                String timecheck[]= timeStart.split(":");
                int hourInQuestion = Integer.parseInt(timecheck[0]);
                if(hourInQuestion == 12)
                {
                    hourInQuestion = 0;
                    String hourFormatted = String.valueOf(hourInQuestion);
                    timeStart = hourFormatted+":"+timecheck[1];
                }
            }
            else
            {
                timeStart = DateTimeMgmt.convertPMToMilitaryTime(String.valueOf(TxtStartTime.getText()));
            }

            String timeEnd = null;
            if(RadioAmEnd.isSelected())
            {
                timeEnd  = String.valueOf(TxtEndTime.getText());
                String timecheck[]= timeEnd.split(":");
                int hourInQuestion = Integer.parseInt(timecheck[0]);
                if(hourInQuestion == 12)
                {
                    hourInQuestion = 0;
                    String hourFormatted = String.valueOf(hourInQuestion);
                    timeEnd = hourFormatted+":"+timecheck[1];
                }
            }
            else
            {
                timeEnd  = DateTimeMgmt.convertPMToMilitaryTime(String.valueOf(TxtEndTime.getText()));
            }

            thereIsAProblem =2;

            int theCustomerID = Integer.parseInt(ButtonCustId.getText());

            int customerInDb = 0;
            for (int i=0; i< Customer.allCustomer.size();i++)
            {
                if (Customer.allCustomer.get(i).getCustId()==theCustomerID)
                {
                    customerInDb=1;
                    break;
                }
            }
            if (customerInDb==0)
            {
                thereIsAProblem=3;
                throw new Exception();
            }

            thereIsAProblem=4;
            String theType = String.valueOf(TxtType.getText());
            String theTitle = String.valueOf(TxtTitle.getText());
            String theContact = ComboContact.getValue();
            String theLocation = String.valueOf(TxtLocation.getText());
            String theDescription = String.valueOf(TxtBxDesc.getText());
            String theStartDate = theDateStart+" "+timeStart+":00";
            String theEndDate = theDateEnd + " "+timeEnd+":00";
            int theUserID = Integer.parseInt(txtUserID.getText());

            int userIsPresent =0;
            for (int i=0; i< User.userList.size();i++)
            {
                if (User.userList.get(i).getUserId()==theUserID)
                {
                    userIsPresent=1;
                }
            }
            if(userIsPresent==0)
            {
                thereIsAProblem=9;
                throw new Exception();
            }


            if (theDateStart.isEmpty() || theDateEnd.isEmpty() || timeStart.isEmpty() || timeEnd.isEmpty() || theType.isEmpty() || theTitle.isEmpty()
                 || theContact.isEmpty() || theLocation.isEmpty())
            {
                thereIsAProblem=5;
                throw new Exception();
            }

            if(isOverlapAppt(theStartDate, theEndDate,theCustomerID))
            {
                thereIsAProblem=6;
                throw new Exception();
            }


            if( apptWithinBusHours(theStartDate, theEndDate))
            {
                Appointment.addToDb(theCustomerID,theType,theTitle,theContact,theLocation,theDescription,theStartDate,theEndDate,theUserID);
                Appointment.loadAppointmentsFromDb();
            }
            else
            {
                Alert alert2 = new Alert(Alert.AlertType.INFORMATION);


                alert2.setContentText("Your appointment is outside business hours (8am-10pm EST).");


                alert2.showAndWait();
            }
        }
        catch(Exception e)
        {
            Alert alert2 = new Alert(Alert.AlertType.INFORMATION);

            ErrorHandlerInterface message1 = ()->{return ErrorHandlerInterface.error01;};
            ErrorHandlerInterface message2 = ()->{return ErrorHandlerInterface.error02;};
            ErrorHandlerInterface message3 = ()->{return ErrorHandlerInterface.error03;};
            ErrorHandlerInterface message4 = ()->{return ErrorHandlerInterface.error04;};
            ErrorHandlerInterface message5 = ()->{return ErrorHandlerInterface.error05;};
            ErrorHandlerInterface message6 = ()->{return ErrorHandlerInterface.error06;};
            ErrorHandlerInterface message7 = ()->{return ErrorHandlerInterface.error07;};
            ErrorHandlerInterface message8 = ()->{return ErrorHandlerInterface.error08;};
            ErrorHandlerInterface message9 = ()->{return ErrorHandlerInterface.error09;};

            if (thereIsAProblem==0)
            {
                alert2.setContentText(message1.errorHandler());
            }
            else if (thereIsAProblem==1)
            {
                alert2.setContentText(message2.errorHandler());
            }
            else if (thereIsAProblem==2)
            {
                alert2.setContentText(message3.errorHandler());
            }
            else if (thereIsAProblem==3)
            {
                alert2.setContentText(message4.errorHandler());
            }
            else if (thereIsAProblem==4)
            {
                alert2.setContentText(message5.errorHandler());
            }
            else if (thereIsAProblem==5)
            {
                alert2.setContentText(message6.errorHandler());
            }
            else if (thereIsAProblem==6)
            {
                alert2.setContentText(message7.errorHandler());
            }
            else if(thereIsAProblem==9)
            {
                alert2.setContentText(message9.errorHandler());
            }
            else
                {
                    alert2.setContentText(message8.errorHandler());
                }


            alert2.showAndWait();
        }

    }

    /**Closes the add appointment screen.*/
    @FXML
    void ButtonCancelOnAction(ActionEvent event)
    {
        Stage stageOld = (Stage) ButtonCancel.getScene().getWindow();
        stageOld.close();
    }

    /**Method that ensures that the appointment is within business hours 8am to 10pm EST.
     * @param theStartTime The start time in string form.
     * @param theEndTime The end time in string form.
     * @return true if within business hours, false otherwise.*/
    public boolean apptWithinBusHours(String theStartTime, String theEndTime)
    {


        Timestamp startStamp = Timestamp.valueOf(theStartTime);
        Timestamp endStamp = Timestamp.valueOf(theEndTime);

        LocalDateTime formStartTime = startStamp.toLocalDateTime();
        LocalDateTime formEndTime = endStamp.toLocalDateTime();

        ZonedDateTime ESTStartTime = formStartTime.atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("America/New_York"));
        ZonedDateTime ESTEndTime = formEndTime.atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("America/New_York"));

        String strStartTime = ESTStartTime.toString();
        String strEndTime = ESTEndTime.toString();

        String[] arrStartTime = strStartTime.split("T|:");
        String[] arrEndTime = strEndTime.split("T|:");

        int startHrs = Integer.parseInt(arrStartTime[1]);
        int endHrs = Integer.parseInt(arrEndTime[1]);

        if(startHrs>=8 && startHrs<22 && endHrs < 22 && endHrs>=8)
        {
            return true;
        }
        return false;

    }

    /**This method ensures that the new appointment does not overlap from the customer perspective.
     * @param theStartTime The start of the appointment in string form.
     * @param theEndTime Then end of the appointmetn in string form.
     * @param custID Id of the customer in question.
     * @return True if overlapped false otherwise.*/
    public boolean isOverlapAppt(String theStartTime, String theEndTime, int custID)
    {
        Timestamp startStamp = Timestamp.valueOf(theStartTime);
        Timestamp endStamp = Timestamp.valueOf(theEndTime);

        LocalDateTime formStartTime = startStamp.toLocalDateTime();
        LocalDateTime formEndTime = endStamp.toLocalDateTime();

        for (int i=0; i<Appointment.allAppointment.size();i++)
        {
            if (Appointment.allAppointment.get(i).getCustPerson() == custID)
            {
                LocalDateTime startStandard = Appointment.allAppointment.get(i).getStartTime().toLocalDateTime();
                LocalDateTime endStandard = Appointment.allAppointment.get(i).getEndTime().toLocalDateTime();
                if
                (
                    formStartTime.isAfter(startStandard) && formStartTime.isBefore(endStandard) ||
                    formEndTime.isAfter(startStandard) && formEndTime.isBefore(endStandard) ||
                    formStartTime.isBefore(startStandard) && formEndTime.isAfter(endStandard) || formStartTime.isEqual(startStandard) ||
                    formStartTime.isBefore(startStandard) && formEndTime.isEqual(endStandard) || formStartTime.isAfter(startStandard) && formEndTime.isEqual(endStandard)
                )
                {
                    return true;
                }
            }
        }
        return false;
    }

    /**Sets contact names to the respective combobox.*/
    public void initialize()
    {
        ComboContact.setItems(Contact.allContactNames);
    }

}

