package laboratoriska1.zadaca6;

import java.io.*;

public class Reverse {

    public static void main(String[] args) throws IOException{

        String source = "D:\\izvor.txt";
        String destination = "D:\\destinacija.txt";

        BufferedReader reader = null;
        BufferedWriter writer = null;

        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(source)));
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(destination)));

            String line = null;

            while((line = reader.readLine()) != null) {

                StringBuffer sodrzina = new StringBuffer(line);
                sodrzina = sodrzina.reverse();
                String prevrti = sodrzina.toString();

                writer.write(prevrti);
                writer.newLine();

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