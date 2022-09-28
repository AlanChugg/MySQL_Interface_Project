package GuiControllersMain;

import SystClasses.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.Locale;

/**Main class that detects default language and calls the System management method responsible for initialization of the program. Also loads the login screen.*/
public class Main extends Application
{

    @Override
    public void start(Stage primaryStage) throws Exception{

        DBConnection.startConnection();
        if (Locale.getDefault().getLanguage() == "fr"){SystemMgmt.LanguageIsFrench = 1;}
        SystemMgmt.initSystem();




        Parent root = FXMLLoader.load(getClass().getResource("LogIn.fxml"));
        primaryStage.setTitle("Schedule Manager System");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }



}
