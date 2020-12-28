package aud5;

import java.util.HashSet;
import java.util.concurrent.Semaphore;

public class SiO2 {

    public static int NUM_RUN = 50;

    static Semaphore si;
    static Semaphore o;
    static Semaphore siHere;
    static Semaphore oHere;
    static Semaphore ready;

    public static void init() {
        si = new Semaphore(1);
        o = new Semaphore(2);
        siHere = new Semaphore(0);
        oHere = new Semaphore(0);
        ready = new Semaphore(0);
    }

    public static class Si extends Thread {

        public void bond() {
            System.out.println("Si is bonding now.");
        }

        @Override
        public void run() {
            for (int i=0;i<NUM_RUN;i++) {
                try {
                    execute();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        public void execute() throws InterruptedException {
            si.acquire();
            siHere.release(2);
            oHere.acquire(2);
            ready.release(2);
            bond();
            si.release();
        }

    }

    public static class O extends Thread {

        public void execute() throws InterruptedException {
            o.acquire(1);
            siHere.acquire();
            oHere.release();
            ready.acquire();
            bond();
            o.release();
        }

        public void bond() {
            System.out.println("O is bonding now.");
        }


        @Override
        public void run() {
            for (int i=0;i<NUM_RUN;i++) {
                try {
                    execute();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        HashSet<Thread> threads = new HashSet<>();
        for (int i = 0; i < NUM_RUN; i++) {
            threads.add(new Si());
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

