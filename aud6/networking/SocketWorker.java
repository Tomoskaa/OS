package aud6.networking;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class SocketWorker extends Thread {

    private Socket socket = null;
    private BufferedWriter writer = null;

    public SocketWorker(Socket socket, BufferedWriter writer) {
        this.socket = socket;
        this.writer = writer;
    }

    @Override
    public void run() {
        try {
            receiveData(this.socket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void receiveData(Socket socket) throws IOException {
        //posoodvetna e ovaa implementacija bidejki imame binarno prakjanje i primanje na podatoci
        DataInputStream dis = new DataInputStream(socket.getInputStream());
        //BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        //iscituvanje na fajlovi
        int numFiles = dis.readInt();
        synchronized (SocketWorker.class) {
            //zapisuvanje na ovoj podatok vo samiot fajl
            //so socket.getInetAddress().getHostAddress() ja zemame adresata na klientot, ja prevzemame od socket-ot
            //socket-ot nudi moznost da zememe ip adresa od mrezno nivo i porta od transportno nivo
            writer.write(String.format("%s %d %d",socket.getInetAddress().getHostAddress(),
                    socket.getPort(),numFiles));
            writer.newLine();
            writer.flush();
        }

    }
}