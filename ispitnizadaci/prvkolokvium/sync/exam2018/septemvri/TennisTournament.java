package ispitnizadaci.prvkolokvium.sync.exam2018.septemvri;

import java.util.HashSet;
import java.util.concurrent.Semaphore;

public class TennisTournament {

    public static Semaphore redGroup;
    public static Semaphore greenGroup;
    public static Semaphore lock;
    public static Semaphore igraj;
    public static int red;
    public static int green;

    public static void init() {
        redGroup = new Semaphore(2);
        greenGroup = new Semaphore(2);
        igraj = new Semaphore(0);
        lock = new Semaphore(1);
        red = 0;
        green = 0;
    }

    public static class GreenPlayer extends Thread {


        public void execute() throws InterruptedException {
            greenGroup.acquire();
            System.out.println("Green player ready");

            lock.acquire();
            green++;
            if(green + red == 4) {
                igraj.release(4);
            }
            lock.release();
            System.out.println("Green player enters field");

            igraj.acquire();
            System.out.println("Match started");
            Thread.sleep(200);

            lock.acquire();
            green--;
            if(green == 0 && red == 0) {
                System.out.println("Green player finished playing");
                // TODO: only one player calls the next line per match
                System.out.println("Match finished");
                greenGroup.release(2);
                redGroup.release(2);
            }
            lock.release();

        }

        public void run() {
            try {
                execute();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    public static class RedPlayer extends Thread {


        public void execute() throws InterruptedException {
            redGroup.acquire();
            System.out.println("Red player ready");

            lock.acquire();
            red++;
            if(red + green == 4) {
                igraj.release(4);
            }
            lock.release();
            System.out.println("Red player enters field");

            igraj.acquire();
            System.out.println("Match started");
            Thread.sleep(200);


            lock.acquire();
            red--;
            if(red == 0 && green == 0) {
                System.out.println("Red player finished playing");
                // TODO: only one player calls the next line per match
                System.out.println("Match finished");
                redGroup.release(2);
                greenGroup.release(2);
            }
            lock.release();
        }

        public void run() {
            try {
                execute();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

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
            t.join();
        }

        // after the waiting for each of the players is done, check the one that are not finished and terminate them
        for(Thread t : threads) {
            if(t.isAlive()) {
                t.interrupt();
                System.err.println("Possible deadlock");
            }
        }
        System.out.println("Finished");
    }

}