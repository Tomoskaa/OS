package ispitnizadaci.prvkolokvium.sync.exam2015;

import java.util.HashSet;
import java.util.concurrent.Semaphore;

public class Poker {

    public static final int GROUP = 6;

    static Semaphore sedniNaMasa;
    static Semaphore diler;
    static Semaphore lock;
    static int numIgraci;


    public static void init() {
        sedniNaMasa = new Semaphore(6);
        diler = new Semaphore(0);
        lock = new Semaphore(1);
        numIgraci = 0;
    }

    public static class Player extends Thread {

        int numRun;
        public Player(int numRun) {
            this.numRun= numRun;
        }

        public void execute() throws InterruptedException {

            //igracot dobiva dozvola za da sedne na masata
            sedniNaMasa.acquire();
            //se povikuva soodvetniot metod
            playerSeat();
            //bidejki numIgraci e static promenliva mora da ja zastitime
            lock.acquire();
            numIgraci++;
            if(numIgraci == GROUP) {
                //ako ima tocno 6 igraci dilerot im deli karti
                diler.release(GROUP);
                //se povikuva soodvetniot metod
                dealCards();
            }
            lock.release();

            //davam zabrana da deli karti dilerot
            diler.acquire();
            //go povikuvam soodvetniot metod
            play();
            //go oslobodvam dilerot od zabranata PAZII!!!!!! ne deli samo ednas vo tekot na igrata mora da ima dozvola
            diler.release();

            //kako sto zavrsuvaat igracite so igra ja namaluvam vrednosta na promenlivata
            lock.acquire();
            numIgraci--;
            if(numIgraci == 0) {
                //cekam da zavrsat site igraci pa posle toa go povikvam metodot endRound()
                endRound();
                //ja osloboduvam masata t.e moze da sednat naredni 6 igraci
                sedniNaMasa.release(GROUP);
            }
            lock.release();
        }

        public void playerSeat() {
            System.out.println("Igracot sednuva na masa.");
        }

        public void dealCards() {
            System.out.println("Dilerot deli karti.");
        }

        public void play() {
            System.out.println("Igrata e zapocnata.");
        }

        public void endRound() {
            System.out.println("Igracite zavrsile so igrata.");
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
        for (int i = 0; i < 10; i++) {
            Player p = new Player(i);
            threads.add(p);
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
        }
        System.out.println("Finished");
    }

}
