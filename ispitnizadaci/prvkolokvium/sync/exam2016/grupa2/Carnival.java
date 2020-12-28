package ispitnizadaci.prvkolokvium.sync.exam2016.grupa2;

import java.util.HashSet;
import java.util.concurrent.Semaphore;

public class Carnival {

    static final int NUM_RUN = 50;

    static final int TOTAL = 30;
    static final int GROUP = 10;

    static Semaphore ucesnici;
    static Semaphore izleziNaBina;
    static Semaphore ciklus;
    static Semaphore lock;

    static int numGroup;
    static int numTotal;

    public static void init() {
        ucesnici = new Semaphore(10);
        izleziNaBina = new Semaphore(0);
        ciklus = new Semaphore(0);
        lock = new Semaphore(0);

        numGroup = 0;
        numTotal = 0;
    }

    public static class Participant extends Thread {

        int num;
        public Participant(int num) {
            this.num = num;
        }

        public void execute() throws InterruptedException {
            ucesnici.acquire();
            participantEnter();
            lock.acquire();
            numGroup++;
            if(numGroup == GROUP) {
                izleziNaBina.release(GROUP);
            }
            lock.release();
            izleziNaBina.acquire();
            present();

            lock.acquire();
            numGroup--;
            numTotal++;
            if(numTotal == 0) {
                endGroup();
                ucesnici.release();
            }
            if(numTotal == TOTAL) {
                endCycle();
                ciklus.release();
                numTotal = 0;
            }
            lock.release();
            ciklus.acquire();
        }

        public void participantEnter() {
            System.out.println("Ucesnik se kacuva na bina");
        }

        public void present() {
            System.out.println("Ucesnikot zapocnuva so prezentacija");
        }

        public void endGroup() {
            System.out.println("Tekovnata grupa zavrsi so prezentiranje.");
        }

        public void endCycle() {
            System.out.println("Site grupi zavrsija so prezentiranje vo tekovniot ciklus.");
        }


    }

    public static void main(String[] args) {
        HashSet<Thread> threads = new HashSet<>();

        int numRuns = 15;
        for(int i = 0; i < NUM_RUN; i++) {
            Participant p = new Participant(numRuns);
            threads.add(p);
        }

        init();

        for(Thread t : threads) {
            t.start();
        }

        for(Thread t : threads) {
            try {
                t.join(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
