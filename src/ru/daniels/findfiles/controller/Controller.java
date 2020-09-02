package ru.daniels.findfiles.controller;

import javafx.concurrent.WorkerStateEvent;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;
import ru.daniels.findfiles.model.Leaf;
import ru.daniels.findfiles.utils.FileSearcher;
import java.io.File;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Controller {
    public TabPane filesTabPane;
    public TextField fieldPathToFile;
    public TextField fieldKeyword;
    public TextField fieldExtension;
    public TreeView<Object> fileTree;
    public ScrollPane filesScrollPane;
    public Label pathLabel;
    public Label keywordLabel;

    private String path;
    private String keyword;
    private String extension = ".log";


    private HashMap<String, Leaf> files = new HashMap<>();


    public void startSearch() {
        if(checkValues()) showProgressWhenWait();
    }

    public void showExplorer() {
        DirectoryChooser chooser = new DirectoryChooser();
        File dir = chooser.showDialog(null);
        if(dir != null) {
            path = dir.toString();
            fieldPathToFile.setText(path);
        }
    }

    private TreeItem<Object> createTree(TreeItem<Object> root, List<Leaf> files){
        TreeItem<Object> start;
        String[] tmp;
        boolean find;
        for(Leaf file : files){
            start = root;
            tmp = file.getPath().toString().replace(fieldPathToFile.getText(), "").split("\\\\");
            for(String s: tmp) {
                find = false;
                for(TreeItem<Object> node: start.getChildren()){
                    if(node.getValue().equals(s)){
                        start = node;
                        find = true;
                        break;
                    }
                }
                if(find) continue;
                TreeItem<Object> newItem = new TreeItem<>(s);
                start.getChildren().add(newItem);
                start = newItem;
            }
            start.setValue(file);
        }
        fileTree.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->{
            if(newValue.isLeaf() && newValue.getValue() instanceof Leaf){
                Leaf leaf = (Leaf) newValue.getValue();
                System.out.println(leaf.getPath());
            }
        });
        return root;
    }

    private boolean checkValues(){
        keyword = fieldKeyword.getText();
        path = path == null ? fieldPathToFile.getText() : path;
        extension = fieldExtension.getText().equals("") ? this.extension : fieldExtension.getText();

        if(path.equals("") && keyword.equals("")){
            missingValuesAlert(pathLabel.getText(), keywordLabel.getText());
            return false;
        }
        else if(path.equals("")) {
            missingValuesAlert(pathLabel.getText());
            return false;
        }
        else if(keyword.equals("")) {
            missingValuesAlert(keywordLabel.getText());
            return false;
        }
        return true;
    }

    private void missingValuesAlert(String... values){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Missing Value");
        alert.setHeaderText(null);
        alert.setContentText("Missing values: " + Arrays.toString(values));
        alert.showAndWait();

    }

    private void showProgressWhenWait(){
        FileSearcher searcher = new FileSearcher(path, keyword, extension);
        ProgressBar progressBar = new ProgressBar();
        progressBar.progressProperty().bind(searcher.progressProperty());
        filesScrollPane.setContent(progressBar);
        File file = new File(path);
        String name = file.getName();
        name = name.equals("") ? file.toString() : name;
        TreeItem<Object> root = new TreeItem<>(name);

        searcher.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, event -> {
            fileTree.setRoot(createTree(root, searcher.getValue()));
            filesScrollPane.setContent(fileTree);
        });
        new Thread(searcher).start();
    }



}
