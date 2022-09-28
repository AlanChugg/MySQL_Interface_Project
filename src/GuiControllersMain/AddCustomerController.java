package GuiControllersMain;

import SystClasses.Customer;
import SystClassesTwo.Countries;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.util.ArrayList;

/**Responsible for the add customer interface.*/
public class AddCustomerController {


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
    private Button ButtonCancel;
    @FXML
    private ComboBox<String> ComboCountry;
    @FXML
    private ComboBox<String> ComboDivision;
    @FXML
    private Button ButtonSave;

    /**Unused.*/
    @FXML
    void TxtNameOnAction(ActionEvent event) {}
    /**Unused.*/
    @FXML
    void TxtAddressOnAction(ActionEvent event) {}
    /**Unused.*/
    @FXML
    void TxtPostalOnAction(ActionEvent event) {}
    /**Unused.*/
    @FXML
    void TxtPhone01OnAction(ActionEvent event) {}
    /**Unused.*/
    @FXML
    void ComboDivisionOnAction(ActionEvent event) {}


    /**Method called when the save button is pressed. Responsible for parsing the textfields and then passing that info to a method in the Customer class that manages database entries.*/
    @FXML
    void ButtonSaveOnAction(ActionEvent event)
    {

        try {
            String theCustomerName = String.valueOf(TxtName.getText());
            String theAddress = String.valueOf(TxtAddress.getText());
            String thePostal = String.valueOf(TxtPostal.getText());
            String thePhone = String.valueOf(TxtPhone01.getText());
            String tempDiv = String.valueOf(ComboDivision.getValue());


            Customer.addCustomerToTheDb(thePhone, theAddress,thePostal,theCustomerName,tempDiv);
            Customer.loadCustomerFromDb();

        }catch(Exception e)
        {
            Alert alert2 = new Alert(Alert.AlertType.INFORMATION);


                alert2.setContentText("There was a problem parsing your entered information. Please check the format of the data and try again.");


            alert2.showAndWait();
        }


    }
    /**Closes the screen*/
    @FXML
    void ButtonCancelOnAction(ActionEvent event)
    {
        Stage stageOld = (Stage) ButtonCancel.getScene().getWindow();
        stageOld.close();
    }

    /**Populates the division combo box with values when a country is selected.*/
    @FXML
    void ComboCountryOnAction(ActionEvent event)
    {
        String countryName = ComboCountry.getValue();
        allDivisionString.clear();

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

    /**Populates the country combobox with values.*/
    public void initialize()
    {
        for(int i=0; i< Countries.listOfCountries.size(); i++)
        {
            allCountryString.add(Countries.listOfCountries.get(i).getCountryName());
        }

        ComboCountry.setItems(allCountryString);
    }

}
