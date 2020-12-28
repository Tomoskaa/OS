package laboratoriska1.zadaca4;

import java.io.File;
import java.io.IOException;

public class Imenik {

    public static void main(String[] args) throws IOException {
        File folder = new File("D:\\OS\\laboratoriska4");
        directory(folder);

        try {
            File file = new File("filename.txt");
            if (file.createNewFile()) {
                System.out.println("File created: " + file.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            //System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
    public static void directory(File folder) {
        File[] files = folder.listFiles();

        for (int i = 0; i < files.length; i++) {
            if (files[i].isDirectory()) {
                System.out.println(files[i].toString());
                directory(files[i]);
            } else {
                System.out.println(files[i].toString());
            }
        }
    }

}
