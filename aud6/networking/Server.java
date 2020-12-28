package aud6.networking;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread{

    String path;
    int port;
    ServerSocket serverSocket = null;
    BufferedWriter writer = null;

    public Server(String path, int port){
        this.path = path;
        this.port = port;

    }

    @Override
    public void run() {
        try {
            this.writer = new BufferedWriter(new FileWriter(path,true));
            this.serverSocket = new ServerSocket(port);
            while(true) {
                Socket socket = this.serverSocket.accept();
                SocketWorker worker = new SocketWorker(socket,writer);
                worker.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



    /*public void recieveData(Socket socket) throws IOException {
        //posoodvetna e ovaa implementacija bidejki imame binarno prakjanje i primanje na podatoci
        DataInputStream dis = new DataInputStream(socket.getInputStream());
        //BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        //iscituvanje na fajlovi
        int numFiles = dis.readInt();
        //zapisuvanje na ovoj podatok vo samiot fajl
        //so socket.getInetAddress().getHostAddress() ja zemame adresata na klientot, ja prevzemame od socket-ot
        //socket-ot nudi moznost da zememe ip adresa od mrezno nivo i porta od transportno nivo
        writer.write(String.format("%s %d %d", socket.getInetAddress().getHostAddress()), socket.getPort(), numFiles);
        writer.newLine();
        writer.flush();
    }*/
}
