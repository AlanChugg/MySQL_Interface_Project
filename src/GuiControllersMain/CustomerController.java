package GuiControllersMain;

import SystClasses.Appointment;
import SystClasses.Customer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;

/**Responsible for the customer manager interface.*/
public class CustomerController {


    @FXML
    private TableColumn<Customer, String> ColumnCustomerName;
    @FXML
    private TableColumn<Customer, String> ColumnAddress;
    @FXML
    private TableColumn<Customer, String> ColumnPostal;
    @FXML
    private TableColumn<Customer, String> ColumnFirLvDiv;
    @FXML
    private TableColumn<Customer, String> ColumncCreateBy;
    @FXML
    private TableColumn<Customer, Integer> ColumnCustomerID;
    @FXML
    private TableColumn<Customer, String> ColumnPhone;
    @FXML
    private TableColumn<Customer, String> ColumnUpdateBy;
    @FXML
    private TableColumn<Customer, java.sql.Timestamp> ColumnUpdateDate;
    @FXML
    private TableColumn<Customer, java.sql.Timestamp> ColumnCreateDate;



    @FXML
    private Button ButtonDeleteCustomer;
    @FXML
    private Button ButtonUpdateCustomer;
    @FXML
    private Button ButtonBack;
    @FXML
    private Button ButtonAddCustomer;
    @FXML
    private TableView<Customer> TableCustomer;

    /**Unused.*/
    @FXML
    void TableCustomerOnAction(ActionEvent event) {}

    /**Loads the Appointment scheduler interface*/
    @FXML
    void ButtonBackOnAction(ActionEvent event)
    {
        try {
            Stage stageOld = (Stage) ButtonBack.getScene().getWindow();
            stageOld.close();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GuiControllersMain/Schedule.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**Loads the Add customer interface. Any updates are reflected in the tableview in real time.*/
    @FXML
    void ButtonAddCustomerOnAction(ActionEvent event) throws IOException
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/GuiControllersMain/AddCustomer.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setScene( new Scene(root));
        stage.show();
    }

    /**Loads the Modify customer interface. Any updates are reflected in the tableview in real time.*/
    @FXML
    void ButtonUpdateCustomerOnAction(ActionEvent event) throws IOException {
        if (TableCustomer.getSelectionModel().getSelectedItem() != null)
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GuiControllersMain/ModifyCustomer.fxml"));
            Parent root = loader.load();
            ModifyCustomerController mc = loader.getController();

            Customer selectedCustomer= TableCustomer.getSelectionModel().getSelectedItem();
            mc.setCustomerFromSchedule(selectedCustomer);

            Stage stage = new Stage();
            stage.setScene( new Scene(root));
            stage.show();
        }
        else
        {
            Alert alert2 = new Alert(Alert.AlertType.INFORMATION);


            alert2.setContentText("Please select a Customer from the Table.");


            alert2.showAndWait();
        }
    }

    /**Deletes a selected customer and all of the corresponding appointments for that customer.*/
    @FXML
    void ButtonDeleteCustomerOnAction(ActionEvent event)
    {
        if (TableCustomer.getSelectionModel().getSelectedItem() != null)
        {

            Alert alert2 = new Alert(Alert.AlertType.INFORMATION);


            alert2.setContentText(TableCustomer.getSelectionModel().getSelectedItem().getName() +" was deleted successfully.");


            Customer.delCustomerFromDb(TableCustomer.getSelectionModel().getSelectedItem());

            Customer.loadCustomerFromDb();
            Appointment.loadAppointmentsFromDb();


            alert2.showAndWait();
        }
        else
            {
                Alert alert2 = new Alert(Alert.AlertType.INFORMATION);


                alert2.setContentText("Please select a Customer from the Table.");


                alert2.showAndWait();
            }
    }

/**Calls the method that loads all customer info from the database and sets the tableview.*/
    public void initialize()
    {
        Customer.loadCustomerFromDb();
        TableCustomer.setItems(Customer.allCustomer);
        ColumnCustomerName.setCellValueFactory(new PropertyValueFactory<>("name"));
        ColumnAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        ColumnPostal.setCellValueFactory(new PropertyValueFactory<>("postal"));
        ColumnFirLvDiv.setCellValueFactory(new PropertyValueFactory<>("division"));
        ColumnPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        ColumnCustomerID.setCellValueFactory(new PropertyValueFactory<>("custId"));
        ColumnUpdateBy.setCellValueFactory(new PropertyValueFactory<>("updateBy"));
        ColumnUpdateDate.setCellValueFactory(new PropertyValueFactory<>("updateDate"));
        ColumncCreateBy.setCellValueFactory(new PropertyValueFactory<>("createBy"));
        ColumnCreateDate.setCellValueFactory(new PropertyValueFactory<>("createDate"));

    }

}
