package aud1;

import aud1.files.FileManager;
import aud1.files.impl.FileManagerImpl;

import java.io.File;
import java.io.FileNotFoundException;

public class Main {

    public static final String path = "D:\\Data\\OS\\tmp";

    public static void main(String[] args) throws FileNotFoundException {

        FileManager manager = new FileManagerImpl();

        System.out.println(manager.deleteFolder(new File("D:\\Data\\OS\\tmp\\todelete")));

    }
}
