package laboratoriska1.zadaca4;

import java.io.File;
import java.io.FileFilter;

public class zadaca4 {

    void listFolder(File dir) {
        File[] f = dir.listFiles(new FileFilter() {

            @Override
            public boolean accept(File pathname) {
                return pathname.isDirectory();
            }
        });

        listFile(dir);

        for(File folder: f) {
            listFolder(folder);
        }
    }

    private void listFile(File dir) {
        File[] files = dir.listFiles();
        for(File file:files) {
            System.out.println(file.getName());
        }
    }
    public static void main(String[] args) {
        new zadaca4().listFolder(new File("D:\\zadaca4"));
    }
}