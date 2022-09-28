package GuiControllersMain;

import SystClasses.Appointment;
import SystClasses.MonthConversion;
import SystClassesTwo.AppointmentCount;
import SystClassesTwo.Contact;
import SystClassesTwo.Type;
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
import java.sql.Timestamp;
import java.util.ArrayList;


/**This class is responsible for the report page. Specifically you will see a list of appointments per month, a contact schedule, and then a custom tableview that can populate customer history.*/
public class ReportController {

    private ArrayList<Appointment> listCustAct = new ArrayList<>();
    private ObservableList<Appointment> allCustAct= FXCollections.observableList(listCustAct);

    private ArrayList<Appointment> listContactSched = new ArrayList<>();
    private ObservableList<Appointment> allContactSched = FXCollections.observableList(listContactSched);

    private ArrayList<AppointmentCount> sortedListApptCount = new ArrayList<>();
    private ObservableList<AppointmentCount> sortedAllApptCount = FXCollections.observableList(sortedListApptCount);




    @FXML
    private TableView<Appointment> TableHist;
    @FXML
    private TableColumn<Appointment, String> ColHisTitle;
    @FXML
    private TableColumn<Appointment, String> ColHistDesc;
    @FXML
    private TableColumn<Appointment, Timestamp> ColHistEndDate;
    @FXML
    private TableColumn<Appointment, Integer> ColHistApptID;
    @FXML
    private TableColumn<Appointment, String> ColHistType;
    @FXML
    private TableColumn<Appointment, Timestamp> ColHistStartDate;




    @FXML
    private TableView<Appointment> TableContactSch;
    @FXML
    private TableColumn<Appointment, Integer> ColContApptId;
    @FXML
    private TableColumn<Appointment, String> ColContTitle;
    @FXML
    private TableColumn<Appointment, String> ColContType;
    @FXML
    private TableColumn<Appointment, String> ColContStartDate;
    @FXML
    private TableColumn<Appointment, Integer> ColContCustId;
    @FXML
    private TableColumn<Appointment, String> ColContDesc;
    @FXML
    private TableColumn<Appointment, String> ColContEndDate;



    @FXML
    private TableView<AppointmentCount> TableAppt;
    @FXML
    private TableColumn<AppointmentCount, Integer> ColApptCount;
    @FXML
    private TableColumn<AppointmentCount, String> ColApptMonth;
    @FXML
    private TableColumn<AppointmentCount, String> ColApptType;



    @FXML
    private TextField txtSearchActivity;
    @FXML
    private RadioButton RadioApptMonth;
    @FXML
    private Button buttonBack;
    @FXML
    private RadioButton RadioApptType;
    @FXML
    private ComboBox<String> ComboSelContact;




    /**Unused.*/
    @FXML
    void txtSearchActivityOnAction(ActionEvent event) {}
    /**Unused.*/
    @FXML
    void TableContactSchOnAction(ActionEvent event) {}



/**Method associated with grouping appointment counts by month.*/
    @FXML
    void RadioApptMonthOnAction(ActionEvent event)
    {
        if(RadioApptMonth.isSelected())
        {
            sortTheCountArrayByMonth();

            TableAppt.setItems(sortedAllApptCount);
        }
    }

    /**Method associated with grouping appointment counts by type.*/
    @FXML
    void RadioApptTypeOnAction(ActionEvent event)
    {
        if (RadioApptType.isSelected())
        {
            sortTheCountArrayByType();

            TableAppt.setItems(sortedAllApptCount);
        }
    }

    /**Method that populates the contact schedule tableview using the appointment list.*/
    @FXML
    void ComboSelContactOnAction(ActionEvent event)
    {
       String selectedName= ComboSelContact.getValue();

       allContactSched.clear();

       for (int i =0; i<Appointment.allAppointment.size(); i++)
       {
           if (Appointment.allAppointment.get(i).getContactPersonName().equals(selectedName))
           {
               allContactSched.add(Appointment.allAppointment.get(i));
           }
       }


       TableContactSch.setItems(allContactSched);
       ColContApptId.setCellValueFactory(new PropertyValueFactory<>("aptId"));;
       ColContTitle.setCellValueFactory(new PropertyValueFactory<>("title"));;
       ColContType.setCellValueFactory(new PropertyValueFactory<>("type"));;
       ColContStartDate.setCellValueFactory(new PropertyValueFactory<>("startTime"));
       ColContCustId.setCellValueFactory(new PropertyValueFactory<>("custPerson"));;
       ColContDesc.setCellValueFactory(new PropertyValueFactory<>("desc"));;
       ColContEndDate.setCellValueFactory(new PropertyValueFactory<>("endTime"));;

    }


/**Method that populates the user defined report from the list of appointments. Note a try catch block is used to display a message if no customer ID is found.*/
    @FXML
    void buttonDisplayActOnAction(ActionEvent event)
    {
        int customerSearch = -1;
        try
        {
            customerSearch=Integer.parseInt(String.valueOf(txtSearchActivity.getText()));
        }
        catch(Exception e)
        {
            Alert alert2 = new Alert(Alert.AlertType.INFORMATION);

            alert2.setContentText("Cannot find a matching customer ID.");

            alert2.showAndWait();
        }

        if (customerSearch != -1)
        {
            allCustAct.clear();
            for (int i=0; i<Appointment.allAppointment.size();i++)
            {
                if (Appointment.allAppointment.get(i).getCustPerson() == customerSearch)
                {
                    allCustAct.add(Appointment.allAppointment.get(i));
                }
            }

            TableHist.setItems(allCustAct);
            ColHisTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
            ColHistDesc.setCellValueFactory(new PropertyValueFactory<>("desc"));
            ColHistEndDate.setCellValueFactory(new PropertyValueFactory<>("endTime"));
            ColHistApptID.setCellValueFactory(new PropertyValueFactory<>("aptId"));
            ColHistType.setCellValueFactory(new PropertyValueFactory<>("type"));
            ColHistStartDate.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        }

    }

    /**Method that allows the back button to load the Appointment manager screen.*/
    @FXML
    void buttonBackOnAction(ActionEvent event)
    {
        try
        {
            Stage stageOld = (Stage) buttonBack.getScene().getWindow();
            stageOld.close();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GuiControllersMain/Schedule.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        }catch(Exception e){}
    }

/**This method sorts the Appointment count objects according to their associated month.*/
    public void sortTheCountArrayByMonth()
    {
        if(!sortedAllApptCount.isEmpty()) {sortedAllApptCount.clear();}

        for (int i =0; i< MonthConversion.monthsOfYear.size(); i++)
        {
            for(int j=0; j< AppointmentCount.allApptCount.size();j++)
            {
               if( MonthConversion.convertMonth(AppointmentCount.allApptCount.get(j).getMonth())
                       == MonthConversion.monthsOfYear.get(i).getIndex())
               {
                   sortedAllApptCount.add(AppointmentCount.allApptCount.get(j));
               }
            }
        }
    }

/**Very similar in structure to the sortTheCountArrayByMonth method, this method sorts the appointment count by type.*/
    public void sortTheCountArrayByType()
    {
        if(!sortedAllApptCount.isEmpty()) {sortedAllApptCount.clear();}

        for (int i =0; i< Type.listOfTypes.size(); i++)
        {
            for (int j=0; j<AppointmentCount.allApptCount.size(); j++)
            {
               if( AppointmentCount.allApptCount.get(j).getType().getTypeIndex() == Type.listOfTypes.get(i).getTypeIndex())
               {
                   sortedAllApptCount.add(AppointmentCount.allApptCount.get(j));
               }
            }
        }

    }


/**Initializes the appointment count array and then sets the respective tableview. All the other tableviews are set upon appropriate interaction with user input.*/
    public void initialize()
    {
        AppointmentCount.initializeAppointmentCount();
        TableAppt.setItems(AppointmentCount.allApptCount);
        ColApptCount.setCellValueFactory(new PropertyValueFactory<>("count"));
        ColApptMonth.setCellValueFactory(new PropertyValueFactory<>("month"));
        ColApptType.setCellValueFactory(new PropertyValueFactory<>("typeForApptCount"));

        ComboSelContact.setItems(Contact.allContactNames);


    }

}
