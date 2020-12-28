package laboratoriska3.zadaca2;

import aud5.SiO2;

import java.util.HashSet;
import java.util.concurrent.Semaphore;

public class C2H2O4 {

    public static int NUM_RUN = 50;

    static Semaphore c;
    static Semaphore h;
    static Semaphore o;
    static Semaphore cHere;
    static Semaphore hHere;
    static Semaphore oHere;
    static Semaphore ready;

    public static void init() {
        c = new Semaphore(2);
        h = new Semaphore(2);
        o = new Semaphore(4);
        cHere = new Semaphore(0);
        hHere = new Semaphore(0);
        oHere = new Semaphore(0);
        ready = new Semaphore(0);
    }


    public static class C extends Thread {

        public void bond() {
            System.out.println("C is boing");
        }

        @Override
        public void run() {
            for(int i = 0; i < NUM_RUN; i++) {
                try {
                    execute();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        public void execute() throws InterruptedException {
            c.acquire();
            cHere.release(6);
            hHere.acquire(2);
            ready.release(2);
            oHere.acquire(4);
            ready.release(4);
            bond();
            c.release();
        }

    }

    public static class H extends Thread {

        public void bond() {
            System.out.println("H is boing");
        }

        @Override
        public void run() {
            for(int i = 0; i < NUM_RUN; i++) {
                try {
                    execute();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        public void execute() throws InterruptedException {
            h.acquire();
            cHere.acquire();
            hHere.release();
            ready.acquire();
            hHere.release(4);
            oHere.acquire(4);
            ready.release(4);
            bond();
            h.release();
        }

    }

    public static class O extends Thread {

        public void bond() {
            System.out.println("O is boing");
        }

        @Override
        public void run() {
            for(int i = 0; i < NUM_RUN; i++) {
                try {
                    execute();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        public void execute() throws InterruptedException {
            o.acquire();
            cHere.acquire();
            oHere.release();
            ready.release();
            hHere.acquire();
            oHere.release();
            ready.release();
            bond();
            o.release();
        }

    }

    public static void main(String[] args) {
        HashSet<Thread> threads = new HashSet<>();
        for (int i = 0; i < NUM_RUN; i++) {
            threads.add(new C());
            threads.add(new H());
            threads.add(new O());
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
