package aud5;

import java.util.HashSet;
import java.util.concurrent.Semaphore;

public class C2H2O4 {

    static Semaphore kislorod;
    static Semaphore vodorod;
    static Semaphore jaglerod;

    static Semaphore jaglerodHere;
    static Semaphore vodorodHere;

    static Semaphore ready;
    static Semaphore finished;

    static int brojKislorod;
    static Semaphore lock;


    public static void init() {

        kislorod = new Semaphore(4);
        vodorod = new Semaphore(2);
        jaglerod = new Semaphore(2);

        jaglerodHere = new Semaphore(0);
        vodorodHere = new Semaphore(0);

        ready = new Semaphore(0);
        finished = new Semaphore(0);

        brojKislorod = 0;
        lock = new Semaphore(1);

    }

    public static class Kislorod extends Thread {

        public void execute() throws InterruptedException {
            kislorod.acquire();

            lock.acquire();
            brojKislorod++;
            if (brojKislorod == 4) {
                vodorodHere.acquire(2);
                jaglerodHere.acquire(2);
                ready.release(8);
            }
            lock.release();
            ready.acquire();
            bond();

            finished.release();

            lock.acquire();
            brojKislorod--;
            if (brojKislorod == 0) {
                finished.acquire(8);
                validate();
                kislorod.release(4);
                jaglerod.release(2);
                vodorod.release(2);
            }
            lock.release();

        }

        public void bond() {
            System.out.println("Bond from Kislorod");
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

    public static class Jaglerod extends Thread {

        public void execute() throws InterruptedException {
            jaglerod.acquire();
            jaglerodHere.release();
            ready.acquire();
            bond();
            finished.release();
        }

        public void bond() {
            System.out.println("Bond from Jaglerod");
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
            System.out.println("Bond from Vodorod");
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
            threads.add(new Jaglerod());
        }

        init();

        for (Thread t : threads) {
            t.start();
        }

        for (Thread t : threads) {
            try {
                t.join(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

