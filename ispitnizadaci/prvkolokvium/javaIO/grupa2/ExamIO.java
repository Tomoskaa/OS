package ispitnizadaci.prvkolokvium.javaIO.grupa2;

import aud1.exceptions.FileExistsException;
import aud1.files.FileManager;
import aud1.files.impl.FileManagerImpl;

import javax.swing.*;
import java.io.*;
import java.nio.file.FileAlreadyExistsException;
import java.util.LinkedList;
import java.util.List;

public class ExamIO {

    //public static final String path = "D:\\OS\\laboratoriska4";
    
    public static void main(String[] args) throws FileExistsException, IOException {
        //ExamIO exam = new ExamIO();

        String in = "D:\\OS\\laboratoriska";
        String out = "D:\\OS\\destinacija";
        moveWritableTxtFiles(in, out);

        String src = "D:\\OS\\input.txt";
        LinkedList<byte[]> a = new LinkedList<>();
        deserializeData(src, a, 2);
        System.out.println(a);

        String source = "D:\\OS\\input.txt";
        String destination = "D:\\OS\\output.txt";
        invertLargeFile(source, destination);

    }
    public static void moveWritableTxtFiles(String from, String to) throws FileExistsException, IOException {
        File in = new File(from);
        File out = new File(to);
        InputStream fis = null;
        OutputStream fos = null;
        File[] files = in.listFiles();

        if(!in.exists()) {
            System.err.println("Ne postoi");
        }

        if(out.exists()) {
            throw new FileExistsException();
        }
        out.mkdirs();

        for(File f : files) {
            if(f.isFile()) {
                if(f.getName().endsWith(".txt") && f.canWrite()) {
                    try {
                        f.renameTo(new File(out.getAbsolutePath(), f.getName()));

                        fis = new FileInputStream(from);
                        fos = new FileOutputStream(to);

                        int c = -1;

                        while ((c=fis.read())!=-1) {
                            fos.write(c);
                            fos.flush();
                        }

                    } finally {
                        fis.close();
                        fos.close();
                    }
                }
            }
        }
    }

    public static void deserializeData(String source, List<byte[]> data, long elementLength) throws IOException {
        File sourceFile = new File(source);
        InputStream  is = new FileInputStream(sourceFile);
        long sourceSize = sourceFile.length();
        long offset = 0;
        while(offset < sourceSize) {
            byte[] bytes = new byte[(int) elementLength];
            is.read(bytes);
            data.add(bytes);
            offset += elementLength;
        }
    }

    public static void invertLargeFile(String source, String destination) throws IOException {
            RandomAccessFile raf = null;
            FileOutputStream rafOut = null;
            File from = new File (source);

            try {
                raf = new RandomAccessFile(from, "r");
                rafOut = new FileOutputStream(destination);

                long totalSize = raf.length();
                while (totalSize>0) {
                    totalSize-=1;
                    raf.seek(totalSize);
                    rafOut.write(raf.read());
                }
            } catch (IOException e) {
                throw e;
            } finally {
                if (raf!=null) {
                    raf.close();
                }
                if (rafOut!=null) {
                    rafOut.close();
                }
            }
    }
}
