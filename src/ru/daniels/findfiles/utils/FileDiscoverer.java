package ru.daniels.findfiles.utils;

import javafx.concurrent.Task;
import ru.daniels.findfiles.model.Leaf;

import java.io.IOException;
import java.nio.file.Files;

public class FileDiscoverer extends Task<String> {
    private Leaf file;

    public FileDiscoverer(Leaf file){
        this.file = file;
    }

    @Override
    protected String call(){
        return getFileContent();
    }

    private String getFileContent(){
        StringBuilder builder = new StringBuilder();
        try {
            Files.readAllLines(file.getPath()).forEach(s -> builder.append(s).append("\n"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return builder.toString();
    }




}
