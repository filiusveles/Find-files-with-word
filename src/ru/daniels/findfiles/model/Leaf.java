package ru.daniels.findfiles.model;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Leaf{
    private final Path path;
    private final String name;
    private List<Integer> wordPositions;

    public Leaf(Path path){
        this.path = path;
        this.name = path.getFileName().toString();
    }

    public Path getPath(){
        return path;
    }

    public String getName(){
        return name;
    }

    public void addPoint(Integer point){
        if(wordPositions == null){
            wordPositions = new ArrayList<>();
        }
        wordPositions.add(point);
    }

    /**
     * Получает список позиций совпадений в тексте
     * @return список позиций
     */
    public List<Integer> getWordPositions(){
        if(wordPositions == null){
            return new ArrayList<>();
        }
        return wordPositions;
    }

    @Override
    public String toString() {
        return name;
    }
}
