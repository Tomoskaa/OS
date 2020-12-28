package ispitnizadaci.prvkolokvium.sync.exam2019.kolokvium1.grupa1;

import javax.crypto.SealedObject;
import java.util.HashSet;
import java.util.concurrent.Semaphore;

public class MacauCardTournament {

    public static Semaphore redGroup;
    public static Semaphore greenGroup;
    public static Semaphore igraj;
    public static Semaphore lock;
    public static int numR;
    public static int numG;

    public static void init() {
        redGroup = new Semaphore(2);
        greenGroup = new Semaphore(2);
        igraj = new Semaphore(0);
        lock = new Semaphore(1);
        numG = 0;
        numR = 0;
    }

    public static void main(String[] args) throws InterruptedException {
        HashSet<Thread> threads = new HashSet<Thread>();
        for (int i = 0; i < 30; i++) {
            RedPlayer red = new RedPlayer();
            threads.add(red);
            GreenPlayer green = new GreenPlayer();
            threads.add(green);
        }

        init();

        // start 30 red and 30 green players in background
        for(Thread t : threads) {
            t.start();
        }

        // after all of them are started, wait each of them to finish for 1_000 ms
        for(Thread t : threads) {
            t.join(1000);
        }

        // after the waiting for each of the players is done, check the one that are not finished and terminate them
        for(Thread t : threads) {
            if(t.isAlive()) {
                t.interrupt();
                System.err.println("Possible deadlock");
            } else {
                System.out.println("Finished");
            }
        }
    }


    public static class GreenPlayer extends Thread {

        public void run() {
            try {
                execute();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        public void execute() throws InterruptedException {

            greenGroup.acquire();
            System.out.println("Green player ready");
            Thread.sleep(50);

            lock.acquire();
            numG++;
            if(numG + numR == 4) {
                igraj.release(4);
            }
            lock.release();

            System.out.println("Green player here");

            igraj.acquire();

            for(int num = 0; num <= 3; num++) {
                //igraj.acquire();
                // TODO: the following code should be executed 3 times
                System.out.println("Game " + num + " started");
                Thread.sleep(200);
                System.out.println("Green player finished game " + num);

                lock.acquire();
                numG--;
                if(numG == 0 && numR == 0) {
                    redGroup.release(2);
                    greenGroup.release(2);
                    // TODO: only one player calls the next line per game
                    System.out.println("Game " + num + " finished");
                    // TODO: only one player calls the next line per match
                    System.out.println("Match finished");
                }
                lock.release();
            }
        }

    }

    public static class RedPlayer extends Thread {


        public void execute() throws InterruptedException {

            redGroup.acquire();
            System.out.println("Red player ready");
            Thread.sleep(50);

            lock.acquire();
            numR++;
            if(numG + numR == 4) {
                igraj.release(4);
            }
            lock.release();

            System.out.println("Red player here");

            igraj.acquire();

            for(int num = 0; num <= 3; num++) {
                //igraj.acquire();
                // TODO: the following code should be executed 3 times
                System.out.println("Game " + num + " started");
                Thread.sleep(200);
                System.out.println("Red player finished game " + num);

                lock.acquire();
                numR--;
                if(numG == 0 && numR == 0) {
                    redGroup.release(2);
                    greenGroup.release(2);
                    // TODO: only one player calls the next line per game
                    System.out.println("Game " + num + " finished");
                    // TODO: only one player calls the next line per match
                    System.out.println("Match finished");
                }
                lock.release();
            }
        }

        public void run() {
            try {
                execute();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}