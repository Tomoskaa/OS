package aud5;

import java.util.HashSet;
import java.util.concurrent.Semaphore;

public class Ca3N2 {

    static Semaphore c;
    static Semaphore n;

    static Semaphore cHere;
    static Semaphore nHere;
    static Semaphore ready;
    static Semaphore finished;

    static int brojC;
    static Semaphore lock;

    public static void init() {
        c = new Semaphore(3);
        n = new Semaphore(2);
        cHere = new Semaphore(0);
        nHere = new Semaphore(0);
        ready = new Semaphore(0);
        finished = new Semaphore(0);
        brojC = 0;
        lock = new Semaphore(1);
    }

    public static class Calcium extends Thread {

        public void execute() throws InterruptedException {

            c.acquire();

            lock.acquire();
            brojC++;
            if(brojC == 3) {
                //cHere.release(3);
                nHere.acquire(2);
                ready.release(5);
            }
            lock.release();
            ready.acquire();
            bond();
            finished.release();

            lock.acquire();
            brojC--;
            if(brojC == 0) {
                finished.acquire(5);
                validate();
                c.release(3);
                n.release(2);
            }
            lock.release();
        }

        public void bond() {
            System.out.println("C is bonding now...");
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

    public static class Natrium extends Thread {

        public void execute() throws InterruptedException {

            n.acquire();
            nHere.release();
            ready.acquire();
            bond();
            finished.release();
        }

        public void bond() {
            System.out.println("N is bonding...");
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
            threads.add(new Calcium());
            threads.add(new Natrium());
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
    }
}
