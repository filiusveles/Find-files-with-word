package ru.daniels.findfiles;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    private Stage stage;
    private VBox rootLayout;

    @Override
    public void start(Stage primaryStage) throws Exception{
        stage = primaryStage;
        stage.setTitle("Hello World");
        showBaseWindow();
    }

    private void showBaseWindow(){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/ru/daniels/findfiles/view/sample.fxml"));
            rootLayout = loader.load();
            Scene scene = new Scene(rootLayout);
            stage.setScene(scene);
            stage.show();
        }catch (IOException exception){
            exception.printStackTrace();
        }


    }


    public static void main(String[] args) {
        launch(args);
    }
}
