package ru.daniels.findfiles.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.daniels.findfiles.model.Leaf;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FileSearcherTest {
    private  String extension;
    private  String path;
    private  String keyword;
    private FileSearcher searcher;

    private static final String ROOT_PATH = "D:\\";
    private static final String PATH_TO_TEST_FILES = "D:\\Java\\Find files with word\\test\\ru\\daniels\\findfiles\\utils\\files for test";
    private static final String PATH_TO_DIR_WITHOUT_CORRECT_FILES = "D:\\Java\\Find files with word\\test\\ru\\daniels\\findfiles\\utils\\files for test\\empty_correct_files";
    private static final String WRONG_CHARSET = "D:\\games\\blackdesert\\bin64\\xc\\ru\\2\\xigncode.log";
    private static final String FILE_WITH_WORD = "D:\\Java\\Find files with word\\test\\ru\\daniels\\findfiles\\utils\\files for test\\with_word.log";
    private static final String FILE_WITHOUT_WORD = "D:\\Java\\Find files with word\\test\\ru\\daniels\\findfiles\\utils\\files for test\\without_word.log";



    @BeforeEach
    void before(){
        extension = ".log";
        path = ROOT_PATH;
        keyword = "test";

    }

    @Test
    void call() {
        path = PATH_TO_TEST_FILES;
        searcher = new FileSearcher(path, keyword, extension);
        List<Leaf> leafs = searcher.call();
        assertTrue(leafs.size() > 0);

        path = PATH_TO_DIR_WITHOUT_CORRECT_FILES;
        searcher = new FileSearcher(path, keyword, extension);
        leafs = searcher.call();
        assertFalse(leafs.size() > 0);

    }

    @Test
    void isWordContains() {
        searcher = new FileSearcher(path, keyword, extension);
        assertFalse(searcher.isWordContains(new File(WRONG_CHARSET).toPath()));
        assertTrue(searcher.isWordContains(new File(FILE_WITH_WORD).toPath()));
    }
}