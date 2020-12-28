package aud5;

import javax.swing.plaf.TableHeaderUI;
import java.util.HashSet;
import java.util.concurrent.Semaphore;

public class AlOH3 {

    static Semaphore vodorod;
    static Semaphore kislorod;
    static Semaphore aluminium;

    static Semaphore kislorodHere;
    static Semaphore ohHere;

    static Semaphore ohReady;
    static Semaphore ready;
    static Semaphore finished;
    static Semaphore next;

    public static void init() {

        vodorod = new Semaphore(3);
        kislorod = new Semaphore(3);
        aluminium = new Semaphore(1);
        kislorodHere = new Semaphore(0);
        ohHere = new Semaphore(0);
        ohReady = new Semaphore(0);
        ready = new Semaphore(0);
        finished = new Semaphore(0);
        next = new Semaphore(0);

    }

    public static class Kislorod extends Thread {

        public void execute() throws InterruptedException {

            kislorod.acquire();
            kislorodHere.release();
            ohReady.acquire();
            bondOH();

            ohHere.release();
            ready.acquire();
            bondAlOH();
            finished.release();
            next.acquire();
            kislorod.release();

        }

        public void bondOH() {
            System.out.println("OH is bonding...");
        }

        public void bondAlOH() {
            System.out.println("AlOH is bonding...");
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
            kislorodHere.acquire();
            ohReady.release();
            bondOH();

            ohHere.release();
            ready.acquire();
            bondAlOH();
            finished.release();
            next.acquire();
            vodorod.release();

        }

        public void bondOH() {
            System.out.println("OH is bonding...");
        }

        public void bondAlOH() {
            System.out.println("AlOH is bonding...");
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

    public static class Aluminium extends Thread {

        public void execute() throws InterruptedException {

            aluminium.acquire();
            ohHere.release(6);
            ready.release(6);
            bondAlOH3();

            finished.acquire(6);
            next.release(6);
            validate();
            aluminium.release();

        }

        public void bondAlOH3() {
            System.out.println("AlOH is bonding...");
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

    public static void main(String[] args) {
        HashSet<Thread> threads = new HashSet<>();
        for (int i = 0; i < 100; i++) {
            threads.add(new Kislorod());
            threads.add(new Vodorod());
            threads.add(new Aluminium());
        }

        init();

        for (Thread t : threads) {
            t.start();
        }

        for (Thread t : threads) {
            try {
                t.join(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        for (Thread t : threads) {
            if (t.isAlive()) {
                t.interrupt();
                System.out.println("Possible deadlock");
            } else {
                System.out.println("Finished");
            }
        }
    }

}


