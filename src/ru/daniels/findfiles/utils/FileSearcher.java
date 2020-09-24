package ru.daniels.findfiles.utils;

import com.sun.istack.internal.NotNull;
import javafx.concurrent.Task;
import ru.daniels.findfiles.model.Leaf;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.LinkedList;
import java.util.List;


public class FileSearcher extends Task<List<Leaf>> {
    private  String extension;
    private  String path;
    private  String keyword;

    public FileSearcher(@NotNull String path, String keyword, String extension){
        this.path = path;
        this.keyword = keyword;
        this.extension = extension;
    }

    @Override
    protected List<Leaf> call() {
        return files();
    }

    private List<Leaf> files(){
        List<Leaf> files = new LinkedList<>();
        File root = new File(path);
        try {
            Files.walkFileTree(root.toPath(), new FileVisit(files));
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return files;
    }

    protected boolean isWordContains(Path path){
            try {
                return Files.lines(path, StandardCharsets.ISO_8859_1)
                        .anyMatch((s) -> s.contains(keyword));
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
    }

    class FileVisit extends SimpleFileVisitor<Path> {
        private final List<Leaf> files;

        public FileVisit(List<Leaf> files){
            this.files = files;
        }

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            if(file.toString().endsWith(extension) && isWordContains(file))
                files.add(new Leaf(file));
            return super.visitFile(file, attrs);
        }

        @Override
        public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
            if(exc instanceof AccessDeniedException) return FileVisitResult.SKIP_SUBTREE;
            return super.visitFileFailed(file, exc);
        }


    }
}
