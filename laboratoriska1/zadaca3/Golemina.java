package laboratoriska1.zadaca3;

import java.io.File;
import java.util.Scanner;

public class Golemina {

    public static void izlistaj(String filePath) {
        File f = new File(filePath);
        File[] files = f.listFiles();

        long kilobytes = 1024;
        for(File file : files){
            if(file.isFile()){
                if(file.length() > 50*kilobytes){
                    System.out.println(file.getName());
                }
            }
        }
    }

    public static void main(String[] args){
        Scanner sc=new Scanner(System.in);
        String filepath=sc.next();

        izlistaj(filepath);

    }
}