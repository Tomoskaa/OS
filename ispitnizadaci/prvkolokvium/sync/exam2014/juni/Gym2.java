package ispitnizadaci.prvkolokvium.sync.exam2014.juni;

import java.util.HashSet;
import java.util.concurrent.Semaphore;

public class Gym2 {

    static Semaphore sala;
    static Semaphore soblekuvaj;
    static Semaphore sportuva;
    static Semaphore lock;
    static int numIgraci;

    public static void init() {
        sala = new Semaphore(12);
        soblekuvaj = new Semaphore(4);
        sportuva = new Semaphore(0);
        lock = new Semaphore(1);
        numIgraci = 0;
    }

    public static class Igrac extends Thread {


        public void execute() throws InterruptedException {

            sala.acquire();
            vlezi();

            lock.acquire();
            numIgraci++;
            if(numIgraci == 12) {
                sportuva.release(12);
            }
            lock.release();
            sportuva.acquire();

            sportuvaj();

            soblekuvaj.acquire();
            presobleci();
            soblekuvaj.release();

            lock.acquire();
            numIgraci--;
            if(numIgraci == 0) {
                slobodnaSala();
                sala.release(12);
            }
            lock.release();
        }

        public void vlezi() {
            System.out.println("Igracot vleguva vo salata.");
        }

        public void presobleci() {
            System.out.println("Igracot se presoblekuva");
        }

        public void sportuvaj() {
            System.out.println("Igracot sportuva");
        }

        public void slobodnaSala() {
            System.out.println("Igracot izvestuva deka salata e slobodna");
        }

        @Override
        public void run() {
            try {
                execute();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        HashSet<Thread> threads = new HashSet<>();
        for(int i = 0; i < 10; i++) {
            Igrac igraci = new Igrac();
            threads.add(igraci);
        }

        init();

        for(Thread t : threads) {
            t.start();
        }

        for(Thread t : threads) {
            try {
                t.join(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        for(Thread t : threads) {
            if(t.isAlive()) {
                t.interrupt();
                System.out.println("Possible deadlock");
            }
            System.out.println("Finished");
        }
    }


}
