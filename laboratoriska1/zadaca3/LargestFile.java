package laboratoriska1.zadaca3;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;


public class LargestFile {

    public static void main(String[] args) throws IOException {

        Scanner sc=new Scanner(System.in);
        String filePath=sc.next();

        najgolemDokument(filePath);

    }

    public static void najgolemDokument(String filePath)
    {
        Long document = 0L;
        File f = new File(filePath);
        File[] files =f.listFiles();
        for (File file : files) {
            if (f.isFile())
            {
                if (document/(1024*1024) < file.length())
                {
                    document = file.length();
                    System.out.println(document);
                }
            }
        }
    }

}
