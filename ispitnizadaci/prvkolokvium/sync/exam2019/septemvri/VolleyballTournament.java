package ispitnizadaci.prvkolokvium.sync.exam2019.septemvri;

import java.util.HashSet;
import java.util.concurrent.Semaphore;

public class VolleyballTournament {

    public static Semaphore sala;
    public static Semaphore kabina;
    public static Semaphore zapocniNatprevar;
    public static Semaphore lock;
    public static int numPlayer;

    public static void init() {
        sala = new Semaphore(12);
        kabina = new Semaphore(4);
        zapocniNatprevar = new Semaphore(0);
        lock = new Semaphore(1);

        numPlayer = 0;
    }

    public static void main(String[] args) {
        HashSet<Player> threads = new HashSet<>();
        for (int i = 0; i < 60; i++) {
            Player p = new Player();
            threads.add(p);
        }

        init();

        // run all threads in background
        for(Thread t : threads) {
            t.start();
        }

        // after all of them are started, wait each of them to finish for maximum 2_000 ms
        for(Thread t : threads) {
            try {
                t.join(2_000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // for each thread, terminate it if it is not finished
        for(Thread t : threads) {
            if(t.isAlive()) {
                t.interrupt();
                System.out.println("Possible deadlock!");
            }
        }
        System.out.println("Tournament finished.");

    }

    public static class Player extends Thread {

        public void execute() throws InterruptedException {

            sala.acquire();
            // at most 12 players should print this in parallel
            System.out.println("Player inside.");

            kabina.acquire();
            // at most 4 players may enter in the dressing room in parallel
            System.out.println("In dressing room.");
            Thread.sleep(10);// this represent the dressing time
            kabina.release();


            lock.acquire();
            numPlayer++;
            if(numPlayer == 12) {
                zapocniNatprevar.release(12);
            }
            lock.release();

            zapocniNatprevar.acquire();

            // after all players are ready, they should start with the game together
            System.out.println("Game started.");
            Thread.sleep(100);// this represent the game duration
            System.out.println("Player done.");


            lock.acquire();
            numPlayer--;
            if(numPlayer == 0) {
                // only one player should print the next line, representing that the game has finished
                System.out.println("Game finished.");
                sala.release(12);
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

}