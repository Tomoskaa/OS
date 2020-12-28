/*package ispitnizadaci.prvkolokvium.javaIO.juni;

import aud6.networking.Client;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class Client extends Thread {

    private File folderTxtOutput;
    private File csvFile;
    private PrintWriter pw;
    private Socket connection;

    public Client(File folderTxtOutput) throws IOException {
        this.folderTxtOutput = folderTxtOutput;

        this.csvFile = new File(folderTxtOutput, "files.csv");
        this.csvFile.createNewFile();

        this.pw = new PrintWriter(this.csvFile);

        this.connection = new Socket(InetAddress.getLocalHost(), 3398);
    }

    public void findFiles(File folderToSearch){

        File[] files = folderToSearch.listFiles();

        for(File f : files){

            if(f.isFile()){
                if((f.getName().endsWith(".txt") || f.getName().endsWith(".dat")) && f.length() < (512 * 1024)){
                    String row = f.getAbsolutePath() + "," + f.length();
                    pw.println(row);
                    pw.flush();
                }
            }
            else if (f.isDirectory()){
                findFiles(f);
            }
        }
    }

    public void sendToServer() throws IOException {

//      BufferedReader br = new BufferedReader(new FileReader(this.csvFile));
//
//        PrintWriter pb = new PrintWriter(connection.getOutputStream());
//
//        while (true) {
//            String line = br.readLine();
//            if (line == null)
//                break;
//            pb.println(line);
//            pb.flush();
//        }

        long last = csvFile.lastModified();
        long length = csvFile.length();

        DataOutputStream dos = new DataOutputStream(connection.getOutputStream());

        dos.writeLong(last);
        dos.writeLong(length);

        dos.flush();
    }

    @Override
    public void run() {
        findFiles(new File("mestp"));
        try {
            sendToServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        Client client = new Client(new File("nesto"));
        client.start();
    }

}
*/