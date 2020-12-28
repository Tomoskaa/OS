package ispitnizadaci.prvkolokvium.sync.exam2017.kolokvium1.grupa2;

import java.util.HashSet;
import java.util.concurrent.Semaphore;

public class Concert {

    static Semaphore grupaTenor;
    static Semaphore tenor;
    static Semaphore grupaBariton;
    static Semaphore bariton;
    static Semaphore izveduvac;
    static Semaphore nastap;
    static Semaphore formiranaGrupa;
    static Semaphore glasaj;

    public static void init() {
        grupaTenor = new Semaphore(3);
        grupaBariton = new Semaphore(3);
        tenor = new Semaphore(0);
        bariton = new Semaphore(0);
        izveduvac = new Semaphore(0);
        nastap = new Semaphore(0);
        formiranaGrupa = new Semaphore(0);
        glasaj = new Semaphore(0);
    }

    public static class Tenor extends Thread {

        public Tenor() {

        }


        public void execute() throws InterruptedException {

           //dozvoluvam da izlezi tenor
           grupaTenor.acquire();
           //eden tenor e sloboden
           tenor.release();
           //davam dozvola da pristapi bariton
           bariton.acquire();

           formBackingVocals();

           //vo grupata treba da povikame uste izveduvac zatoa stavame release
           formiranaGrupa.release();
           //davame dozvola da pristapi izveduvacot
           izveduvac.acquire();

            //gi imam site 3 clena znaci grupata mi e formirana, go povikuvame metodot perform
            perform();
            //nastapot mozi da pocni znaci stavam release
            nastap.release();

            //otkako zavrsi nastapot treba da se glasa
            glasaj.acquire();
            grupaTenor.release();
        }

        public void formBackingVocals() {
            System.out.println("Moze da se formira grupa.");
        }

        public void perform() {
            System.out.println("Moze da zapocne nastapot.");
        }

        public void vote() {
            System.out.println("Moze da se glasa.");
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

    public static class Baritone  extends Thread {

        public Baritone() {

        }

        public void execute() throws InterruptedException {

            grupaBariton.acquire();
            bariton.release();
            tenor.acquire();

            formBackingVocals();

            formiranaGrupa.release();
            izveduvac.acquire();

            perform();
            nastap.release();

            glasaj.acquire();
            grupaBariton.release();

        }

        public void formBackingVocals() {
            System.out.println("Moze da se formira grupa.");
        }

        public void perform() {
            System.out.println("Moze da zapocne nastapot.");
        }

        public void vote() {
            System.out.println("Moze da se glasa.");
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

    public static class Performer extends Thread {

        public Performer() {

        }

        public void execute() throws InterruptedException {

            //formiranaGrupa(6);
            izveduvac.release(6);

            perform();
            nastap.acquire(6);

            vote();
            glasaj.release(6);

        }


        public void formBackingVocals() {
            System.out.println("Moze da se formira grupa.");
        }

        public void perform() {
            System.out.println("Moze da zapocne nastapot.");
        }

        public void vote() {
            System.out.println("Moze da se glasa.");
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

        Tenor te = new Tenor();
        threads.add(te);

        Baritone b = new Baritone();
        threads.add(b);

        Performer p = new Performer();
        threads.add(p);

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
                System.out.println("Possible deadlock.");
            }
        }
        System.out.println("Finished.");
    }


}
