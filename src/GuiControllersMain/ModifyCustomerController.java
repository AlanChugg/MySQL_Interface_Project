package GuiControllersMain;


import SystClasses.Customer;
import SystClassesTwo.Countries;
import SystClassesTwo.FirstLvlDiv;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.ArrayList;

/**This class is associated with the modify customer screen. Note that any modifications performed are updated on the customer manager screen in real time.*/
public class ModifyCustomerController {


    Customer passingCustomer =null;

    private ArrayList<String> listCountryString= new ArrayList<>();
    private ObservableList<String> allCountryString = FXCollections.observableList(listCountryString);

    private ArrayList<String> listDivisionString= new ArrayList<>();
    private ObservableList<String> allDivisionString = FXCollections.observableList(listDivisionString);



    @FXML
    private TextField TxtPhone01;
    @FXML
    private TextField TxtAddress;
    @FXML
    private TextField TxtPostal;
    @FXML
    private TextField TxtName;
    @FXML
    private Label LabelCustomerId;
    @FXML
    private Button ButtonCancel;
    @FXML
    private ComboBox<String> ComboCountry;
    @FXML
    private ComboBox<String> ComboDivision;

    /**Unused*/
    @FXML
    void TxtNameOnAction(ActionEvent event) {}
    /**Unused*/
    @FXML
    void TxtAddressOnAction(ActionEvent event) {}
    /**Unused*/
    @FXML
    void TxtPostalOnAction(ActionEvent event) {}
    /**Unused*/
    @FXML
    void TxtPhone01OnAction(ActionEvent event) {}
    /**Unused*/
    @FXML
    void ComboDivisionOnAction(ActionEvent event) {}

    /**Method called when the save button is pressed. This method passes several parameters to the method in the customer class responsible for updating the database. Exceptions are caught and a error message is displayed.*/
    @FXML
    void ButtonSaveOnAction(ActionEvent event)
    {
        try
        {
            String thePhone = String.valueOf(TxtPhone01.getText());
            String theAddress = String.valueOf(TxtAddress.getText());
            String thePostal = String.valueOf(TxtPostal.getText());
            String theName = String.valueOf(TxtName.getText());
            int theCustId = passingCustomer.getCustId();
            String theDivId = String.valueOf(ComboDivision.getValue());

            Customer.modCustomerToTheDb(thePhone, theAddress, thePostal, theName, theDivId, theCustId);
            Customer.loadCustomerFromDb();


        }catch(Exception e)
        {

            Alert alert2 = new Alert(Alert.AlertType.INFORMATION);


            alert2.setContentText("There was a problem parsing your entered information. Please check the format of the data and try again.");


            alert2.showAndWait();

        }
    }

    /**Closes the modify customer screen.*/
    @FXML
    void ButtonCancelOnAction(ActionEvent event)
    {
        Stage stageOld = (Stage) ButtonCancel.getScene().getWindow();
        stageOld.close();
    }

    /**This method populates the list of local divisions ie allDivisionString when a country is selected by the user.*/
    @FXML
    void ComboCountryOnAction(ActionEvent event)
    {
        String countryName = ComboCountry.getValue();
        allDivisionString.clear();
        ComboDivision.setValue("");

        for (int i=0; i<Countries.listOfCountries.size(); i++)
        {
            if(Countries.listOfCountries.get(i).getCountryName().equals(countryName))
            {
                for (int j=0; j<Countries.listOfCountries.get(i).getListOfDiv().size(); j++)
                {
                    allDivisionString.add(Countries.listOfCountries.get(i).getSpecificDiv(j));
                }
            }
        }
        ComboDivision.setItems(allDivisionString);

    }

    /**This method sets data for the customer selected from the customer management screen. Specifically this method is utilized when the management screen loads the fxml file associated with this class.
     * @param tempCust  The customer object that contains the information used to populate the text fields.*/
    public void setCustomerFromSchedule(Customer tempCust)
    {
        passingCustomer = tempCust;

        TxtPhone01.setText(passingCustomer.getPhone());
        TxtAddress.setText(passingCustomer.getAddress());
        TxtPostal.setText(passingCustomer.getPostal());
        TxtName.setText(passingCustomer.getName());
        LabelCustomerId.setText(String.valueOf(passingCustomer.getCustId()));
        ComboCountry.setValue(Countries.retCountryNameFromDivId(passingCustomer.getDivision2()));
        ComboDivision.setValue(FirstLvlDiv.retDivNameFromId(passingCustomer.getDivision2()));
    }

    /**Initialize populates the country combo box with the list of countries generated by the countries class.*/
    public void initialize()
    {
        for(int i = 0; i< Countries.listOfCountries.size(); i++)
        {
            allCountryString.add(Countries.listOfCountries.get(i).getCountryName());
        }

        ComboCountry.setItems(allCountryString);
    }

}

