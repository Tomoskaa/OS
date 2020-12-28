package laboratoriska1.zadaca6;

import java.io.*;

public class Opseg {
    public static void main(String[] args) throws IOException {

        String in = "D:\\izvor.txt";
        String out = "D:\\destinacija.txt";

        BufferedReader reader = null;
        BufferedWriter writer = null;

        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(in)));
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(out)));

            String line = null;

            while((line = reader.readLine()) != null) {

                if(Character.isDigit(line.charAt(0)))
                {
                    writer.write(line);
                    writer.newLine();
                    //System.out.println("\n");
                }
            }

        } catch (IOException e) {
            throw e;
        } finally {
            if (reader != null) {
                reader.close();
            }
            if (writer != null) {
                writer.flush();
                writer.close();
            }
        }
    }
}
