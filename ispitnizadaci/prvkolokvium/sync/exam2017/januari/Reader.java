package ispitnizadaci.prvkolokvium.sync.exam2017.januari;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.Semaphore;

public class Reader extends Thread {

    public static Random random = new Random();
    public static Semaphore lock = new Semaphore(1);
    public static Semaphore signal = new Semaphore(0);

    public static void main(String[] args) throws InterruptedException {
        // TODO: kreirajte Reader i startuvajte go negovoto pozadinsko izvrsuvanje
        Reader reader = new Reader();
        reader.start();

        for (int i = 0; i < 100; i++) {
            Writer writer = new Writer();
            //TODO: startuvajte go writer-ot
            writer.start();
        }


        // TODO: Cekajte 10000ms za Reader-ot da zavrsi
        for(Thread t : reader) {
            try {
                t.join(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //reader.join(10000);


        // TODO: ispisete go statusot od izvrsuvanjeto
        for(Thread t : reader) {
            if (t.isAlive()) {
                t.interrupt();
                System.out.println("Terminated reading");
            } else {
                System.out.println("Finished reading");
            }
        }
    }

    /**
     * Ne smee da bide izvrsuva paralelno so write() metodot
     */
    public static void read() {
        System.out.println("reading");
    }


    public void run() {
        int pendingReading = 100;
        while (pendingReading > 0) {
            pendingReading--;
            try {
                // TODO: cekanje na nov zapisan podatok
                signal.acquire();

                //so lock ja zastituvam promenlivata
                lock.acquire();
                // TODO: read() metodot ne smee da se izvrsuva paralelno so write() od Writer klasata
                read();
                lock.release();
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }

        System.out.println("Done reading!");
    }


    static class Writer extends Thread {

        public Integer duration;

        public Writer() throws InterruptedException {
            this.duration = Reader.random.nextInt(1000);
        }

        /**
         * Ne smee da bide povikan paralelno
         */
        public static void write() {
            System.out.println("writting");
        }

        @Override
        public void run() {
            try {
                //Writer thread-овите веднаш по стартувањето во позадина треба да заспие рандом време со Thread.sleep(this.duration)
                Thread.sleep(this.duration);
                //по што треба да запише со методот write() кој не смее да се извршува паралелно
                lock.acquire();
                write();
                lock.release();
                //Потоа треба да се извести Reader thread-от дека има нови податоци за читање
                signal.release();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}