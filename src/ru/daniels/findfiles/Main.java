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
        //launch(args);

        System.exit(0);
    }

    public static int expandedForm(int num)
    {
        int left = 0;
        int right = num;
        while(left < right){
            int mid = (left + right)/2;
            int sqrtMid = mid * mid;
            if(sqrtMid == num) return mid;
            if(sqrtMid > num) right = mid;
            if(sqrtMid < num) left = mid+1;
        }
        return left - 1;
    }

    public static String expandedForm1(int num)
    {
        String outs = "";
        for (long i = 10; i < num; i *= 10) {
            long rem = num % i;
            outs = (rem > 0) ? " + " + rem + outs : outs;
            num -= rem;
        }
        outs = num + outs;

        return outs;
    }









}
