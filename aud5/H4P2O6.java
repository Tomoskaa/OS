package aud5;

import javax.sound.midi.ShortMessage;
import java.util.HashSet;
import java.util.concurrent.Semaphore;

public class H4P2O6 {

    static Semaphore vodorod;
    static Semaphore kislorod;
    static Semaphore fosfor;

    static Semaphore vodorodHere;
    static Semaphore fosforHere;

    static Semaphore ready;
    static Semaphore finished;

    static Semaphore lock;
    static int numKislorod;

    public static void init() {

        vodorod = new Semaphore(4);
        kislorod = new Semaphore(6);
        fosfor = new Semaphore(2);
        vodorodHere = new Semaphore(0);
        fosforHere = new Semaphore(0);
        ready = new Semaphore(0);
        finished = new Semaphore(0);
        lock = new Semaphore(1);
        numKislorod = 0;

    }

    public static class Vodorod extends Thread {

        public void execute() throws InterruptedException {

            vodorod.acquire();
            vodorodHere.release();
            ready.acquire();
            bond();
            finished.release();

        }

        public void bond() {
            System.out.println("H is bonding...");
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

    public static class Kislorod extends Thread {

        public void execute() throws InterruptedException {

            kislorod.acquire();

            lock.acquire();
            numKislorod++;
            if(numKislorod == 6) {
                vodorodHere.acquire(4);
                fosforHere.acquire(2);
                ready.release(12);
            }
            lock.release();
            ready.acquire();
            bond();

            finished.release();

            lock.acquire();
            numKislorod--;
            if(numKislorod == 0) {
                finished.acquire(12);
                validate();
                vodorod.release(4);
                fosfor.release(2);
                kislorod.release(6);
            }
            lock.release();
        }

        public void bond() {
            System.out.println("O is bonding...");
        }

        public void validate() {
            System.out.println("Validate");
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

    public static class Fosfor extends Thread {

        public void execute() throws InterruptedException {
            fosfor.acquire();
            fosforHere.release();
            ready.acquire();
            bond();
            finished.release();
        }

        public void bond() {
            System.out.println("F is bonding...");
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
        for(int i =0; i < 100; i++) {
            threads.add(new Vodorod());
            threads.add(new Kislorod());
            threads.add(new Fosfor());
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
            } else {
                System.out.println("Finished");
            }
        }
    }
}
