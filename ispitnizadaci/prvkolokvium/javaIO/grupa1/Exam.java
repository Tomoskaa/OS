package ispitnizadaci.prvkolokvium.javaIO.grupa1;

import aud1.exceptions.FileExistsException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Exam {

    public static void main(String[] args) throws IOException, InterruptedException {

        //najlesno mi e da kreiram objekti
        Exam exam = new Exam();

        List<byte[]> bytes = new ArrayList<>();
        bytes.add("12".getBytes());
        bytes.add("33".getBytes());
        bytes.add("56".getBytes());

        byte[] res = exam.deserializeDataAtPosition("serialized_data.txt", 1, 2);
        System.out.println(new String(res));
    }

    public static void copyLargeTxtFiles(String from, String to, long size) throws FileExistsException, IOException {

        File in = new File(from);
        File out = new File(to);

        InputStream fis = null;
        OutputStream fos = null;
        //kreiram lista na fajlovi koi gi listam od dadeniot folder
        File[] files = in.listFiles();

        //proveruvam dali postoi imenik(folder) in
        if (!in.exists()) {
            System.err.println("Ne postoi");
            return;
        }

        //ako ne postoi imenik(folder) to treba da go kreiram
        if (out.exists()) {
            throw new FileExistsException();
        }
        out.mkdirs();

        for (File f : files) {
            if (f.isFile() && f.getName().endsWith(".txt")) {
                if (f.length() > size) {
                    try {
                        File newFile = new File(out.getAbsolutePath(), f.getName());
                        newFile.createNewFile();
                        System.out.println(newFile.getAbsolutePath());

                        fis = new FileInputStream(from);
                        fos = new FileOutputStream(to);

                        int c = -1;

                        while ((c = fis.read()) != -1) {
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

    public static void serializeData(String destination, List<byte[]> data) throws IOException {
        OutputStream fos = null;
        try {
            fos = new FileOutputStream(destination);
            for (byte[] element : data) {
                fos.write(element);
            }
        } finally {
            if (fos != null) {
                fos.close();
            }
        }
    }

    public byte[] deserializeDataAtPosition(String source, long position, int elementLength) throws IOException, InterruptedException {

        byte[] niza = new byte[elementLength];
        RandomAccessFile raf = null;

        try {
            raf = new RandomAccessFile(source, "r");
            //seek go vrakja podatokot na pozicija position
            raf.seek(position * elementLength);

            for (int i = 0; i < elementLength; i++) {
                niza[i] = (byte) raf.read();
            }
        } finally {
            if (raf != null) {
                raf.close();
            }
        }
        return niza;
    }
}
