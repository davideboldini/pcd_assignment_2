package Model;

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

    public List<File> getJavaFileList(){
        List<File> fileList = new ArrayList<>(Arrays.stream(Objects.requireNonNull(new File(dirPath).listFiles((dir, name) -> name.endsWith(".java")))).toList());
        return fileList;
    }

    public List<File> getGenericFileList(){
        List<File> fileList = new ArrayList<>(Arrays.stream(Objects.requireNonNull(new File(dirPath).listFiles())).toList());
        return fileList;
    }

    public List<Directory> getDirectoryList(){
        List<File> dirList = Arrays.stream(Objects.requireNonNull(new File(dirPath).listFiles())).toList().stream().filter(File::isDirectory).toList();
        return dirList.stream().map(dir -> new Directory(dir.getAbsolutePath())).collect(Collectors.toCollection(LinkedList::new));
    }
}
