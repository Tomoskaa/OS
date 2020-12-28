package ispitnizadaci.prvkolokvium.sync.exam2015;

import java.util.HashSet;
import java.util.concurrent.Semaphore;

public class SushiBar {

    static final int GROUP = 6;

    static Semaphore vleziVoBarot;
    static Semaphore kelner;
    static Semaphore lock;
    static int numClients;

    public static void init() {
        vleziVoBarot = new Semaphore(6);
        kelner = new Semaphore(0);
        lock = new Semaphore(1);
        numClients = 0;
    }

    public static class Customer extends Thread {

        int numRuns;
        public Customer(int numRuns) {
            this.numRuns = numRuns;
        }

        public void execute() throws InterruptedException {

            //klentite moze da vleguvaat vo barot samo dokolku ima mesto, so acquire im davam dozvola da vleguvaat
            vleziVoBarot.acquire();
            //go povikuvam soodvetniot metod
            customerSeat();
            lock.acquire();
            numClients++;
            if(numClients == GROUP) {
                kelner.release(GROUP);
                callWaiter();
            }
            lock.release();

            kelner.acquire();
            customerEat();
            kelner.release();

            lock.acquire();
            numClients--;
            if(numClients == 0) {
                eatingDone();
                vleziVoBarot.release(GROUP);
            }
            lock.release();
        }

        public void customerSeat() {
            System.out.println("Klientot vleguva vo barot.");
        }

        public void callWaiter() {
            System.out.println("Klientot go povikuva kelnerot.");
        }

        public void customerEat() {
            System.out.println("Klientot jade.");
        }

        public void eatingDone() {
            System.out.println("Klientot zavrsil so obrokot.");
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
        for(int i = 0; i < 10; i++) {
            Customer c = new Customer(i);
            threads.add(c);
        }

        init();

        for(Thread t : threads) {
            t.start();
        }

        for(Thread t : threads) {
            try {
                t.join(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        for(Thread t : threads) {
            if(t.isAlive()) {
                t.interrupt();
                System.out.println("Possible deadlock");
            }
        }
        System.out.println("Finished");
    }
}
