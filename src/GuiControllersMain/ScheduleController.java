package GuiControllersMain;

import SystClasses.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.*;
import java.util.ArrayList;

/**Responsible for displaying Appointment information, this is what is seen after login. */
public class ScheduleController {

    private ArrayList<String> monthNameList = new ArrayList<>();
    private ObservableList<String> allMonthNameList = FXCollections.observableList(monthNameList);

    private ArrayList<Appointment> listAugAppointment = new ArrayList<>();
    private ObservableList<Appointment> augAllAppointment = FXCollections.observableList(listAugAppointment);


    @FXML
    private TableView<Appointment> TableAppointment;
    @FXML
    private TableColumn<Appointment, String> ColumnLocation;
    @FXML
    private TableColumn<Appointment, String> ColumnType;
    @FXML
    private TableColumn<Appointment, Integer> ColumnApptID;
    @FXML
    private TableColumn<Appointment, String> ColumnDescription;
    @FXML
    private TableColumn<Appointment, String> ColumnStartDate;
    @FXML
    private TableColumn<Appointment, String> ColumnTitle;
    @FXML
    private TableColumn<Appointment, Integer> ColumnCustomerID;
    @FXML
    private TableColumn<Appointment, Integer> ColumnContact;
    @FXML
    private TableColumn<Appointment, String> ColumnEndDate;
    @FXML
    private TableColumn<Appointment, Integer> ColumnUserID;


    @FXML
    private Button ButtonManageCustomer;
    @FXML
    private RadioButton RadioMonthAppt;
    @FXML
    private Button ButtonGenerateReport;
    @FXML
    private RadioButton RadioAllAppt;


    @FXML
    private RadioButton RadioWeekAppt;
    @FXML
    private DatePicker DatePickByWeeK;
    @FXML
    private ComboBox<String> ComboByMonth;

    /**Unused.*/
    @FXML
    void TableAppointmentOnAction(ActionEvent event) {}
    /**Unused.*/
    @FXML
    void RadioAllApptOnAction(ActionEvent event) {}
    /**Unused.*/
    @FXML
    void RadioWeekApptOnAction(ActionEvent event) {}
    /**Unused.*/
    @FXML
    void RadioMonthApptOnAction(ActionEvent event) {}
    /**Unused.*/
    @FXML
    void DatePickByWeeKOnAction(ActionEvent event) {}
    /**Unused.*/
    @FXML
    void ComboByMonthOnAction(ActionEvent event) {}


/**Method responsible for filtering appointments displayed in the tableview. Specifically it checks radiobutton selections(all, month, week) and behaves accordingly.*/
    @FXML
    void ButtonApplyFilterOnAction(ActionEvent event)
    {
        if(RadioAllAppt.isSelected())
        {
            augAllAppointment.clear();
            SystClasses.Appointment.loadAppointmentsFromDb();
            TableAppointment.setItems(SystClasses.Appointment.allAppointment);
        }
        try
        {
            if(RadioMonthAppt.isSelected() && !ComboByMonth.getValue().isEmpty())
            {
                String theMonth = ComboByMonth.getValue();
                SystClasses.Appointment.loadAppointmentsFromDb();
                if (!augAllAppointment.isEmpty()){augAllAppointment.clear();}

                for (int i=0; i< SystClasses.Appointment.allAppointment.size(); i++)
                {
                    if (MonthConversion.monthFromTimestamp(SystClasses.Appointment.allAppointment.get(i).getStartTime()).equals(theMonth))
                    {
                        augAllAppointment.add(SystClasses.Appointment.allAppointment.get(i));
                    }

                }
                TableAppointment.setItems(augAllAppointment);

            }
        }

        catch(Exception e)
        {
            Alert alert2 = new Alert(Alert.AlertType.INFORMATION);

            alert2.setContentText("Please select a month from the drop down menu.");

            alert2.showAndWait();
        }


        if(RadioWeekAppt.isSelected() && DatePickByWeeK.getValue() != null)
        {
            LocalDate theDate = DatePickByWeeK.getValue();
            LocalDate firOfWeek = DateTimeMgmt.firstDayOfWeek(theDate);
            LocalDate lasOfWeek = DateTimeMgmt.lastDayOfTheWeek(theDate);
            SystClasses.Appointment.loadAppointmentsFromDb();
            if (!augAllAppointment.isEmpty()){augAllAppointment.clear();}

            for (int i=0; i< SystClasses.Appointment.allAppointment.size(); i++)
            {
                if (DateTimeMgmt.convertTimestampToLocalDate(SystClasses.Appointment.allAppointment.get(i).getStartTime()).isAfter(firOfWeek)
                        && DateTimeMgmt.convertTimestampToLocalDate(SystClasses.Appointment.allAppointment.get(i).getStartTime()).isBefore(lasOfWeek))
                {
                    augAllAppointment.add(SystClasses.Appointment.allAppointment.get(i));
                }
            }
            TableAppointment.setItems(augAllAppointment);
        }

        else if (RadioWeekAppt.isSelected() && DatePickByWeeK.getValue() == null)
        {
            Alert alert2 = new Alert(Alert.AlertType.INFORMATION);

            alert2.setContentText("Please select a date from the drop down menu.");

            alert2.showAndWait();

        }
    }


/**Closes the Appointment page and loads the Customer management page. Associated with the manage customer button.*/
    @FXML
    void ButtonManageCustomerOnAction(ActionEvent event) throws IOException
    {
        Stage stageOld = (Stage) ButtonManageCustomer.getScene().getWindow();
        stageOld.close();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/GuiControllersMain/Customer.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setScene( new Scene(root));
        stage.show();
    }

    /**Ends program and closes the database connection. Associated with the Exit button.*/
    @FXML
    void ButtonExitOnAction(ActionEvent event)
    {
        DBConnection.closeConnection();
        System.exit(0);
    }

    /**Closes the Appointment page and launches the report page. Associated with the generate report button.*/
    @FXML
    void ButtonGenerateReportOnAction(ActionEvent event) throws IOException {

        Stage stageOld = (Stage) ButtonGenerateReport.getScene().getWindow();
        stageOld.close();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/GuiControllersMain/Report.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setScene( new Scene(root));
        stage.show();
    }
/**Loads the add appointment page. Note that information is updated in real time.*/
    @FXML
    void ButtonAddAppointmentOnAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/GuiControllersMain/AddAppt.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setScene( new Scene(root));
        stage.show();
    }
    /**Loads the update appointment page. Note that information is updated in real time.*/
    @FXML
    void ButtonUpdateAppointmentOnAction(ActionEvent event) throws IOException {

        if (TableAppointment.getSelectionModel().getSelectedItem() != null)
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GuiControllersMain/ModifyAppt.fxml"));
            Parent root = loader.load();
            ModifyApptController ma = loader.getController();

            Appointment selectedProduct = TableAppointment.getSelectionModel().getSelectedItem();
            ma.setAppointmentFromSchedule(selectedProduct);

            Stage stage = new Stage();
            stage.setScene( new Scene(root));
            stage.show();
        }
        else
        {
            Alert alert2 = new Alert(Alert.AlertType.INFORMATION);


            alert2.setContentText("Please select an Appointment from the Table.");


            alert2.showAndWait();
        }
    }
/**Deletes an appointment and then displays a custom message.*/
    @FXML
    void ButtonDeleteAppointmentOnAction(ActionEvent event)
    {
        if (TableAppointment.getSelectionModel().getSelectedItem() != null)
        {
            Alert alert2 = new Alert(Alert.AlertType.INFORMATION);


            alert2.setContentText("Appointment ID: " + TableAppointment.getSelectionModel().getSelectedItem().getAptId() + " of type: "+TableAppointment.getSelectionModel().getSelectedItem().getType()+" was cancelled." );

            SystClasses.Appointment.deleteFromDb(TableAppointment.getSelectionModel().getSelectedItem());
            SystClasses.Appointment.loadAppointmentsFromDb();

            alert2.showAndWait();

        }
        else
        {
            Alert alert2 = new Alert(Alert.AlertType.INFORMATION);


            alert2.setContentText("Please select an Appointment from the Table.");


            alert2.showAndWait();
        }

    }

/**This method checks for an appointment within 15 min and then displays an appropriate custom message.*/
    public void checkForImpendingAppt()
    {
        if(SystemMgmt.UserHasBeenWarned==0)
        {
            Timestamp currTime = new Timestamp(System.currentTimeMillis());

            for (int i=0; i< SystClasses.Appointment.allAppointment.size();i++)
            {
                if(currTime.toLocalDateTime().isBefore(SystClasses.Appointment.allAppointment.get(i).getStartTime().toLocalDateTime())
                        && currTime.toLocalDateTime().plusMinutes(15).isAfter(SystClasses.Appointment.allAppointment.get(i).getStartTime().toLocalDateTime())
                        && User.UserOfTheSystemId == SystClasses.Appointment.allAppointment.get(i).getUserId())
                {
                    Alert alert2 = new Alert(Alert.AlertType.INFORMATION);

                    alert2.setTitle("You have an appointment in 15 min!");
                    alert2.setContentText(" ID: "+ SystClasses.Appointment.allAppointment.get(i).getAptId()+" Start Date: "
                            +SystClasses.Appointment.allAppointment.get(i).getStartTime()+" End Date: "
                            + SystClasses.Appointment.allAppointment.get(i).getEndTime());


                    alert2.showAndWait();
                    SystemMgmt.UserHasBeenWarned=1;
                    break;

                }
                SystemMgmt.UserHasBeenWarned=2;
            }

        }
        if(SystemMgmt.UserHasBeenWarned==2)
        {
            SystemMgmt.UserHasBeenWarned=1;

            Alert alert2 = new Alert(Alert.AlertType.INFORMATION);

            alert2.setContentText("You have no upcoming appointments.");

            alert2.showAndWait();
        }
    }

    /**LAMBDA EXPRESSION USE #1 This initialize method utilizes a for-each version of a lambda expression to populate a list containing month names. Ultimately, this lambda decreases the amount of typed code needed for a for each loop, minimizing the error potential and maximizes readability.*/
   public void initialize()
   {

       MonthConversion.monthsOfYear.forEach((n)->{allMonthNameList.add(n.getAbbrev());});

       ComboByMonth.setItems(allMonthNameList);
       TableAppointment.setItems(SystClasses.Appointment.allAppointment);
       ColumnLocation.setCellValueFactory(new PropertyValueFactory<>("location"));
       ColumnType.setCellValueFactory(new PropertyValueFactory<>("type"));
       ColumnApptID.setCellValueFactory(new PropertyValueFactory<>("aptId"));
       ColumnDescription.setCellValueFactory(new PropertyValueFactory<>("desc"));
       ColumnStartDate.setCellValueFactory(new PropertyValueFactory<>("startTime"));
       ColumnTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
//     ColumnCustomerName.setCellValueFactory(new PropertyValueFactory<>("name"));
       ColumnCustomerID.setCellValueFactory(new PropertyValueFactory<>("custPerson"));
       ColumnContact.setCellValueFactory(new PropertyValueFactory<>("contactPerson"));
       ColumnEndDate.setCellValueFactory(new PropertyValueFactory<>("endTime"));
 //    ColumnLastUpBy.setCellValueFactory(new PropertyValueFactory<>("lastUpdatedBy"));
//     ColumnLastUpDate.setCellValueFactory(new PropertyValueFactory<>("lastUpdate"));
       ColumnUserID.setCellValueFactory(new PropertyValueFactory<>("userId"));

       checkForImpendingAppt();

   }

}

