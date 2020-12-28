package laboratoriska1.zadaca6;

import java.io.*;


public class Samoglaski {
    public static void main(String[] args) throws IOException {

        String in = "D:\\izvor.txt";
        String out = "D:\\destinacija.txt";

        BufferedReader reader = null;
        BufferedWriter writer = null;

        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(in)));
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(out)));

            String line = null;

           // System.out.println("Enter the character");
            //Scanner sc = new Scanner(System.in);
            //String input = sc.nextLine();
            line = reader.readLine();
            int count=0;
            //System.out.print("Vowel are = ");
            for(int i=0; i < line.length(); i++)
            {
                char ch = line.charAt(i);
                if(ch=='e'||ch=='o'||ch=='i'||ch=='a'||ch=='u'||ch=='O'||ch=='I'||ch=='E'||ch=='O'||ch=='A')
                    count++;
            }
            writer.write(count);
           //System.out.println(count);

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
