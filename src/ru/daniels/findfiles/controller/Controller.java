package ru.daniels.findfiles.controller;

import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.DirectoryChooser;
import ru.daniels.findfiles.model.Leaf;
import ru.daniels.findfiles.utils.FileDiscoverer;
import ru.daniels.findfiles.utils.FileSearcher;

import java.io.File;
import java.util.Arrays;
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

    public void showSystemExplorer() {
        DirectoryChooser chooser = new DirectoryChooser();
        File dir = chooser.showDialog(null);
        if(dir != null) {
            path = dir.toString();
            fieldPathToFile.setText(path);
        }
    }

    public void startSearch() {
        if(checkValues()){
            showProgressWhenWait();
            filesTabPane.getTabs().clear();
            fileTree = new TreeView<>();
        }
    }

    private boolean checkValues(){
        keyword = fieldKeyword.getText();
        path = fieldPathToFile.getText();
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
        filesScrollPane.setContent(getProgressBar(searcher));
        searcher.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, event -> {
            fileTree.setRoot(createTree(searcher.getValue()));
            filesScrollPane.setContent(fileTree);
        });
        new Thread(searcher).start();
    }

    private ProgressBar getProgressBar(Task<?> task){
        ProgressBar progressBar = new ProgressBar();
        progressBar.progressProperty().bind(task.progressProperty());
        return progressBar;
    }

    private TreeItem<Object> createTree(List<Leaf> files){
        File f = new File(path);
        String rootName = f.getName().equals("") ? f.toString() : f.getName();
        int rootStringLength = f.getParent() == null ? rootName.length() : path.length()+1;
        TreeItem<Object> root = new TreeItem<>(rootName);
        TreeItem<Object> start;
        String[] tmp;
        boolean find;
        for(Leaf file : files){
            start = root;
            tmp = file.getPath().toString().substring(rootStringLength).split("\\\\");
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
            if(newValue.isLeaf()){
                showFileContent((Leaf) newValue.getValue());
            }
        });
        return root;
    }

    private void showFileContent(Leaf leaf){
        FileDiscoverer discoverer = new FileDiscoverer(leaf);
        Text text = new Text();
        TextFlow textArea = new TextFlow(text);
        ScrollPane fileContent = new ScrollPane(textArea);
        Tab tab = new Tab();
        tab.setText(leaf.getName());
        tab.setContent(getProgressBar(discoverer));
        filesTabPane.getTabs().add(tab);
        discoverer.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, event -> {
            text.setText(discoverer.getValue());
            tab.setContent(fileContent);
        });
        new Thread(discoverer).start();
    }
}
