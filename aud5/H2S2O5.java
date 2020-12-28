package aud5;

import javax.sound.midi.ShortMessage;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.concurrent.Semaphore;

public class H2S2O5 {

    static Semaphore kislorod;
    static Semaphore vodorod;
    static Semaphore jaglerod;

    static Semaphore jaglerodHere;
    static Semaphore vodorodHere;

    static Semaphore ready;
    static Semaphore finished;

    static Semaphore lock;
    static int numKislorod;


    public static void init() {

        kislorod = new Semaphore(5);
        vodorod = new Semaphore(2);
        jaglerod = new Semaphore(2);
        jaglerodHere = new Semaphore(0);
        vodorodHere = new Semaphore(0);
        ready = new Semaphore(0);
        finished = new Semaphore(0);
        lock = new Semaphore(1);
        numKislorod = 0;

    }

    public static class Kislorod extends Thread {

        public void execute() throws InterruptedException {

            kislorod.acquire();

            lock.acquire();
            numKislorod++;
            if(numKislorod == 5) {
                vodorodHere.acquire(2);
                jaglerodHere.acquire(2);
                ready.release(9);
            }
            lock.release();
            ready.acquire();
            bond();

            finished.release();

            lock.acquire();
            numKislorod--;
            if(numKislorod == 0) {
                finished.acquire(9);
                validate();
                kislorod.release(5);
                jaglerod.release(2);
                vodorod.release(2);
            }
            lock.release();
        }

        public void validate() {
            System.out.println("Validate");
        }

        public void bond() {
            System.out.println("O is bonding...");
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

    public static class Jaglerod extends Thread {

        public void execute() throws InterruptedException {

            jaglerod.acquire();
            jaglerodHere.release();
            ready.acquire();
            bond();
            finished.release();

        }

        public void bond() {
            System.out.println("C is bonding...");
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
        for(int i = 0; i < 100; i++) {
            threads.add(new Kislorod());
            threads.add(new Vodorod());
            threads.add(new Jaglerod());
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
