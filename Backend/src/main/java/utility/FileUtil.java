package utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Thomas on 15.05.2015.
 */
public class FileUtil {

    public static ArrayList<File> readDirectory(String path, ArrayList<File> list) {
        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                list.add(listOfFiles[i]);
            } else if (listOfFiles[i].isDirectory()) {
                //It's a Directory.... Then do Nothing!
            }
        }
        return list;
    }

    /**
     * Add extension to a file that doesn't have yet an extension
     * this method is useful to automatically add an extension in the savefileDialog control
     *
     * @param file file to check
     * @param ext  extension to add
     * @return file with extension (e.g. 'test.doc')
     */
    public static String addFileExtIfNecessary(String file, String ext) {
        if (file.lastIndexOf('.') == -1)
            file += "." + ext;

        return file;
    }

    public static InputStream getInputStream(String filePath) {
        InputStream inputStream = null;
        File f = new File(filePath);

        if (f.exists()) {
            try {
                inputStream = new FileInputStream(f);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        } else {
            inputStream = new FileUtil().getClass().getResourceAsStream(filePath);
        }

        return inputStream;
    }
}
