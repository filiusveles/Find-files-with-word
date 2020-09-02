package ru.daniels.findfiles.utils;

import com.sun.istack.internal.NotNull;
import javafx.concurrent.Task;
import ru.daniels.findfiles.model.Leaf;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.LinkedList;
import java.util.List;

public class FileSearcher extends Task<List<Leaf>> {
    private final String extension;
    private final String path;
    private final String keyword;

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

    class FileVisit extends SimpleFileVisitor<Path> {
        private final List<Leaf> files;

        public FileVisit(List<Leaf> files){
            this.files = files;
        }


        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            if(file.toString().endsWith(extension) && isWordContains(file.toUri()))
                files.add(new Leaf(file));
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
}
