package GuiControllersMain;

import SystClasses.*;
import SystClassesTwo.Contact;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**Very similar to the modify customer controller, this class controller loads information into the respective fields and then sends the updated fields to the method in the Appointment class responsible for udpating the database.*/
public class ModifyApptController {

    private Appointment passingAppointment = null;




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
    private RadioButton RadioAmEnd;
    @FXML
    private TextField LabelCustID;
    @FXML
    private DatePicker ComboEndDate;
    @FXML
    private RadioButton RadioPmEnd;
    @FXML
    private TextField txtUserID;

    /**Unused*/
    @FXML
    void TxtTypeOnAction(ActionEvent event) {}
    /**Unused*/
    @FXML
    void TxtTitleOnAction(ActionEvent event) {}
    /**Unused*/
    @FXML
    void TxtLocationOnAction(ActionEvent event) {}
    /**Unused*/
    @FXML
    void TxtStartTimeOnAction(ActionEvent event) {}
    /**Unused*/
    @FXML
    void TxtEndTimeOnAction(ActionEvent event) {}
    /**Unused*/
    @FXML
    void ComboContactOnAction(ActionEvent event) {}
    /**Unused*/
    @FXML
    void DateStartOnAction(ActionEvent event) {}
    /**Unused*/
    @FXML
    void ComboEndDateOnAction(ActionEvent event) {}
    /**Unused*/
    @FXML
    void RadioAmStartOnAction(ActionEvent event) {}
    /**Unused*/
    @FXML
    void RadioPmStartOnAction(ActionEvent event) {}
    /**Unused*/
    @FXML
    void RadioPmEndOnAction(ActionEvent event) {}
    /**Unused*/
    @FXML
    void RadioAmEndOnAction(ActionEvent event) {}
    @FXML
    void TxtUserIDOnAction(ActionEvent event) {}

    /**LAMBDA EXPRESSION USE #2 The lambdas in this expression are correlated with the ErrorHandlerInterface and will load appropriate messages accordingly. Here, these lambdas make generating organized error messages easier, it shortens the amount of code needed, and it dramatically increases the re usability of code. This will become more apparent as the program grows.*/
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

           int customerInDb=0;
           int theCustomerID = Integer.parseInt(LabelCustID.getText());
           for (int i = 0; i< Customer.allCustomer.size(); i++)
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
           int theApptId = passingAppointment.getAptId();
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

           if(isOverlapAppt(theStartDate,theEndDate,theCustomerID))
           {
               thereIsAProblem=6;
               throw new Exception();
           }

            if ( apptWithinBusHours(theStartDate, theEndDate))
            {
                Appointment.modifyToDb(theCustomerID,theType,theTitle,theContact,theLocation,theDescription,theStartDate,theEndDate, theApptId, theUserID);
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

    /**Method associated with the cancel button, this will discard any changes and close the current screen.*/
    @FXML
    void ButtonCancelOnAction(ActionEvent event)
    {
        Stage stageOld = (Stage) ButtonCancel.getScene().getWindow();
        stageOld.close();
    }

    /**This method is responsible for fetching and loading the passing appointment into the textfields.*/
    public void setAppointmentFromSchedule(Appointment tempAppointment)
    {
        passingAppointment = tempAppointment;

        ComboContact.setValue(passingAppointment.getContactPersonName());
        TxtLocation.setText(passingAppointment.getLocation());
        DateStart.setValue(DateTimeMgmt.convertTimestampToLocalDate(passingAppointment.getStartTime()));
        if (DateTimeMgmt.timeIsInMilitary(passingAppointment.getStartTime()))
        {
            RadioPmStart.setSelected(true);
        }
        else
            {
                RadioAmStart.setSelected(true);
            }
        LabelApptID.setText(String.valueOf(passingAppointment.getAptId()));
        TxtType.setText(passingAppointment.getType());
        TxtStartTime.setText(DateTimeMgmt.convertMilitaryToPM(passingAppointment.getStartTime()));
        TxtBxDesc.setText(passingAppointment.getDesc());
        TxtTitle.setText(passingAppointment.getTitle());
        if (DateTimeMgmt.timeIsInMilitary(passingAppointment.getEndTime()))
        {
            RadioPmEnd.setSelected(true);
        }
        else
        {
            RadioAmEnd.setSelected(true);
        }
        TxtEndTime.setText(DateTimeMgmt.convertMilitaryToPM(passingAppointment.getEndTime()));
        LabelCustID.setText(String.valueOf(passingAppointment.getCustPerson()));
        ComboEndDate.setValue(DateTimeMgmt.convertTimestampToLocalDate(passingAppointment.getEndTime()));
        txtUserID.setText(String.valueOf(passingAppointment.getUserId()));
    }

    /**This method uses a little bit of time format juggling to ensure that the user entered time is within business hours EST.
     * @param theStartTime The user entered start time for the appointment.
     * @param theEndTime The user entered end time for the appointment.
     * @return true or false accordingly.*/
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

    /**This method ensures that there is no overlapping appointments per each customer.
     * @param theStartTime User entered appointment start time
     * @param theEndTime Appointment end time.
     * @param custID The id of the customer in question.
     * @return true if overlap, false otherwise*/
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
                    if(Appointment.allAppointment.get(i).getAptId() != passingAppointment.getAptId())
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**Initialize sets th contact combobox with contact names.*/
    public void initialize()
    {
        ComboContact.setItems(Contact.allContactNames);

    }

}

