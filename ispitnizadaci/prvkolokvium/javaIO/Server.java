/*package ispitnizadaci.prvkolokvium.javaIO;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Semaphore;

public class Server {

    private ServerSocket server;
    private File writeHere;

    public Server(File writeHere) throws IOException {

        this.server = new ServerSocket(3398);

        this.writeHere = writeHere;


    public void takeRequests() throws IOException {

        while (true){
            Socket client = this.server.accept();
            ServerWorkerThread swt = new ServerWorkerThread(this.writeHere, client);
            swt.start();
        }

    }

    public static void main(String[] args) throws IOException {

        Server js = new Server(new File("n"));

        js.takeRequests();

    }

}


class ServerWorkerThread extends Thread{

    private File writeHere;
    private Socket client;
    private static Semaphore semaphore = new Semaphore(1);

    public ServerWorkerThread(File writeHere, Socket client){
        this.writeHere = writeHere;
        this.client = client;
    }

    @Override
    public void run() {
        try {
            DataInputStream dis = new DataInputStream(client.getInputStream());
            long last = dis.readLong();
            long length = dis.readLong();

            String row = client.getInetAddress().getHostAddress() + " " + client.getPort() + " " + length + " " + last;
            PrintWriter pw = new PrintWriter(writeHere);

            semaphore.acquire();
            pw.println(row);
            pw.flush();
            semaphore.release();

        } catch (InterruptedException e){
            e.printStackTrace();
        }
    }
    }
}

*/