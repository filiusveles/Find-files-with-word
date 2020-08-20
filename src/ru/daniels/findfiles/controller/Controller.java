package ru.daniels.findfiles.controller;

import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;
import ru.daniels.findfiles.utils.FileVisit;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;

public class Controller {
    public TabPane filesTabPane;
    public TextField fieldPathToFile;
    public TextField fieldKeyword;
    public TextField fieldExtension;
    public TreeView<String> fileTree;


    private String keyword;
    private String extension = ".log";


    public void startSearch() {
        File file = new File(fieldPathToFile.getText());
        String name = file.getName();
        name = name.equals("") ? file.toString() : name;
        TreeItem<String> root = new TreeItem<>(name);
        keyword = fieldKeyword.getText();
        fileTree.setRoot(createTree(root, files()));
        fileTree.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->
                System.out.println("name: " + newValue.getValue() + ", leaf: " + newValue.isLeaf()));
    }

    public void showExplorer() {
        DirectoryChooser chooser = new DirectoryChooser();
        File dir = chooser.showDialog(null);
        if(dir != null) {
            fieldPathToFile.setText(dir.toString());
        }
    }

    private TreeItem<String> createTree(TreeItem<String> root, List<Path> files){
        TreeItem<String> start;
        String[] tmp;
        boolean find;
        for(Path path: files){
            start = root;
            tmp = path.toString().replace(fieldPathToFile.getText(), "").split("\\\\");
            for(String s: tmp) {
                find = false;
                for(TreeItem<String> node: start.getChildren()){
                    if(node.getValue().equals(s)){
                        start = node;
                        find = true;
                        break;
                    }
                }
                if(find) continue;
                TreeItem<String> newItem = new TreeItem<>(s);
                start.getChildren().add(newItem);
                start = newItem;
            }
        }
        return root;
    }

    private List<Path> files(){
        List<Path> files = new LinkedList<>();
        File root = new File(fieldPathToFile.getText());
        try {
            Files.walkFileTree(root.toPath(), new FileVisit(files, keyword, extension));
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return files;
    }






}
