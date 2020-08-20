package ru.daniels.findfiles.utils;

import java.io.*;
import java.net.URI;
import java.nio.file.AccessDeniedException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;

public class FileVisit extends SimpleFileVisitor<Path> {
    private String keyword;
    private String extension;
    private List<Path> files;

    public FileVisit(List<Path> files, String keyword, String extension){
        this.keyword = keyword;
        this.extension = extension;
        this.files = files;
    }


    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        if(file.toString().endsWith(extension) && isWordContains(file.toUri()))
            files.add(file);
        return super.visitFile(file, attrs);
    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
        if(exc instanceof AccessDeniedException) return FileVisitResult.SKIP_SUBTREE;
        return super.visitFileFailed(file, exc);
    }

    private boolean isWordContains(URI path){
        byte[] bytes = keyword.getBytes();

        try(FileReader reader = new FileReader(new File(path))) {
            int c;
            int counter = 0;
            while ((c=reader.read())!=-1){
                if(counter == bytes.length-1) return true;
                if(c == bytes[counter]) counter++;
                else counter = 0;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

}
