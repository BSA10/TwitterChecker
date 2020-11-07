package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainJavaFX extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("Checker.fxml"));
        primaryStage.setTitle("Checker Twitter | Made by Bassam");
        primaryStage.setScene(new Scene(root, 277, 384));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
