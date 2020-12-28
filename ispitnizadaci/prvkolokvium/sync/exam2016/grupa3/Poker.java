package ispitnizadaci.prvkolokvium.sync.exam2016.grupa3;

import java.util.HashSet;
import java.util.concurrent.Semaphore;

public class Poker {

    static final int TOTAL = 15;
    static final int GROUP = 5;

    static Semaphore igraci;
    static Semaphore zapocniIgra;
    static Semaphore igrajPovtorno;
    static Semaphore lock;

    static int numGroup;
    static int numTotal;

    public static void init() {
        igraci = new Semaphore(5);
        zapocniIgra = new Semaphore(0);
        igrajPovtorno = new Semaphore(0);
        lock = new Semaphore(0);

        numGroup = 0;
        numTotal = 0;
    }

    public static class Player extends Thread {

        int run;
        public Player(int run) {
            this.run = run;
        }

        public void execute() throws InterruptedException {

            igraci.acquire();
            playerSeat();
            lock.acquire();
            numGroup++;
            if(numGroup == GROUP) {
                zapocniIgra.release(GROUP);
            }
            lock.release();
            zapocniIgra.acquire();

            play();
            lock.acquire();
            numGroup--;
            numTotal++;
            if(numTotal == 0) {
                endRound();
                igraci.release(GROUP);
            }
            if(numTotal == TOTAL) {
                endCycle();
                igrajPovtorno.release(TOTAL);
                numTotal = 0;
            }
            lock.release();
            igrajPovtorno.acquire();
        }

        public void playerSeat() {
            System.out.println("Igrac sednuva");
        }

        public void play() {
            System.out.println("Igracot zapocnuva so igra");
        }

        public void endRound() {
            System.out.println("Tekovnata grupa zavrsi so igranje.");
        }

        public void endCycle() {
            System.out.println("Site grupi zavrsija so igranje vo tekovniot ciklus.");
        }
    }


    public static void main(String[] args) {
        HashSet<Thread> threads = new HashSet<>();
        for(int i = 0; i < 20; i++) {
            Player p = new Player(i);
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

        for(Thread t : threads)
        {
            if(t.isAlive())
            {
                t.interrupt();
                System.out.println("Possible deadlock!");
            }
        }
        System.out.println("Tournament finished.");
    }

}
