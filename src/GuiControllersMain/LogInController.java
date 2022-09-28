package GuiControllersMain;

import SystClasses.SystemMgmt;
import SystClasses.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.IllegalFormatException;

/**Class controller responsible for managing the log in screen.*/
public class LogInController {

    @FXML
    private Label UserId;
    @FXML
    private Button ButtonLogIn;
    @FXML
    private TextField TxtFldUserId;
    @FXML
    private TextField TxtFldPassword;
    @FXML
    private Label SysLogIn;
    @FXML
    private Label Password;
    @FXML
    private Label Location;

    /**Unused*/
    @FXML
    void TxtFldUserIdOnAction(ActionEvent event) {}
    /**Unused*/
    @FXML
    void TxtFldPasswordOnAction(ActionEvent event) {}


    /**Method called when the log in button is pressed. Works with the User class to validate credentials and displays a message in the appropriate language.*/
    @FXML
    void ButtonLogInOnAction(ActionEvent event) throws IOException {
        String username = null;
        String password = null;
        PrintWriter out = null;

        try { username = String.valueOf(TxtFldUserId.getText());}
        catch(IllegalFormatException | NullPointerException e){}

        try { password = String.valueOf(TxtFldPassword.getText());}
        catch(IllegalFormatException | NullPointerException e){}

        User unknownPerson = new User(username, password);

        if (username.isEmpty() == false && password.isEmpty() == false && User.logInCheck(unknownPerson))
        {
            try
            {
                out = new PrintWriter(new BufferedWriter(new FileWriter("login_activity.txt", true)));
                out.print("USER: "+username + "\t");
                Instant theTime =  Instant.now();
                out.print("TIME(UTC): "+theTime+ "\t");
                out.println("SUCCESSFUL LOGIN");
            }
            catch(IOException e){}
            finally{ if (out != null){out.close();}}

            Stage stageOld = (Stage) ButtonLogIn.getScene().getWindow();
            stageOld.close();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GuiControllersMain/Schedule.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene( new Scene(root));
            stage.show();

        }
        else
            {
                try
                {
                    out = new PrintWriter(new BufferedWriter(new FileWriter("login_activity.txt", true)));
                    out.print("USER: "+username + "\t");
                    Instant theTime =  Instant.now();
                    out.print("TIME(UTC): "+theTime+ "\t");
                    out.println("UNSUCCESSFUL LOGIN");
                }
                catch(IOException e){}
                finally{ if (out != null){out.close();}}

                Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
                if (SystemMgmt.LanguageIsFrench == 1)
                {
                    alert2.setTitle("Tentative De Connexion Non Valide");
                }
                else{alert2.setTitle("Invalid Log In Attempt");}
                if (SystemMgmt.LanguageIsFrench == 1)
                {
                    alert2.setContentText("Nom d'utilisateur ou mot de passe incorrect.");
                }
                else{alert2.setContentText("Username or Password Incorrect.");}
                alert2.showAndWait();
            }
    }

/**Sets language relevant text and determines default zone ID.*/
    public void initialize()
    {
        if (SystemMgmt.LanguageIsFrench == 1)
        {
            ButtonLogIn.setText("Connexion");
            UserId.setText("Identifiant");
            Password.setText("Mot de passe");
            SysLogIn.setText("Connexion Au Systeme");
        }
        try
        {
            Location.setText(String.valueOf(ZoneOffset.systemDefault()));
        }
        catch(IllegalFormatException | NullPointerException e)
        {
            Location.setText("Unable to Determine Location");
        }
    }


}
