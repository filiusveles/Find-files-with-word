package ru.daniels.findfiles;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    private Stage stage;

    @Override
    public void start(Stage primaryStage) throws Exception{
        stage = primaryStage;
        stage.setTitle("Find files with word");
        showBaseWindow();
    }

    private void showBaseWindow(){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/ru/daniels/findfiles/view/sample.fxml"));
            VBox rootLayout = loader.load();
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
