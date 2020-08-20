package ru.daniels.findfiles.model;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Leaf{
    private final Path path;
    private final String name;
    private List<Integer> pointsWithWord;
    private boolean isFindPints;

    public Leaf(Path path){
        this.path = path;
        this.name = path.getFileName().toString();
        isFindPints = false;
    }

    public Path getPath(){
        return path;
    }

    public String getName(){
        return name;
    }

    public void addPoint(Integer point){
        if(pointsWithWord == null){
            pointsWithWord = new ArrayList<>();
        }
        pointsWithWord.add(point);
    }

    /**
     * Получает список позиций совпадений в тексте
     * @return список позиций
     */
    public List<Integer> getPointsWithWord(){
        if(pointsWithWord == null){
            return new ArrayList<>();
        }
        isFindPints = true;
        return pointsWithWord;
    }

    public boolean isFindPints() {
        return isFindPints;
    }

    @Override
    public String toString() {
        return name;
    }
}
