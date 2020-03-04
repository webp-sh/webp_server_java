package moe.keshane.webpserverjava.Utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUtils {
    /**
     * @param dirPath directory path
     * @return is the directory be created
     */
    public static boolean createDir(String dirPath){
        if(!isExist(dirPath)){
            try {
                Path path = Files.createDirectories(Paths.get(dirPath));
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * @param dirPath directory path
     * @return is the directory exist
     */
    public static boolean isExist(String dirPath){
        if(Files.exists(Paths.get(dirPath),new LinkOption[]{ LinkOption.NOFOLLOW_LINKS})){
            return true;
        }
        return false;
    }
}
