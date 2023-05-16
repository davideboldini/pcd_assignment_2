package org.example;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class Directory {

    private final String dirPath;

    public Directory(final String dirPath){
        this.dirPath = dirPath;
    }

    public String getDirPath() {
        return this.dirPath;
    }

    public List<File> getAllElementsList(){
        return Arrays.asList(Objects.requireNonNull(new File(dirPath).listFiles()));
    }

    public List<File> getJavaFileList(){
        return new ArrayList<>(Arrays.stream(Objects.requireNonNull(new File(dirPath).listFiles((dir, name) -> name.endsWith(".java")))).toList());
    }

    public List<File> getGenericFileList(){
        return new ArrayList<>(Arrays.stream(Objects.requireNonNull(new File(dirPath).listFiles())).toList());
    }

    public List<Directory> getDirectoryList(){
        List<File> dirList = Arrays.stream(Objects.requireNonNull(new File(dirPath).listFiles())).toList().stream().filter(File::isDirectory).toList();
        return dirList.stream().map(dir -> new Directory(dir.getAbsolutePath())).collect(Collectors.toCollection(LinkedList::new));
    }
}
