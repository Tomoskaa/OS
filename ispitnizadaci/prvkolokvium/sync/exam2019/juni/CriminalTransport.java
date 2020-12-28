package ispitnizadaci.prvkolokvium.sync.exam2019.juni;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.Semaphore;

public class CriminalTransport {

    public static Semaphore policaecVlezi;
    public static Semaphore zatvorenikVlezi;
    public static Semaphore izlezi;
    public static int policaec;
    public static int zatvorenik;
    public static Semaphore trgni;
    public static Semaphore lock;

    public static void init() {
        policaecVlezi = new Semaphore(2);
        zatvorenikVlezi = new Semaphore(0);
        trgni = new Semaphore(0);
        izlezi = new Semaphore(0);
        zatvorenik = 0;
        policaec = 0;
        lock = new Semaphore(1);

    }

    public static void main(String[] args) throws InterruptedException {
        HashSet<Thread> threads = new HashSet<Thread>();
        for (int i = 0; i < 60; i++) {
            Policeman red = new Policeman();
            threads.add(red);
            Criminal green = new Criminal();
            threads.add(green);
        }

        init();

        // run all threads in background
        for(Thread t : threads) {
            t.start();
        }

        // after all of them are started, wait each of them to finish for maximum 1_000 ms
        for(Thread t : threads) {
            t.join(1_000);
        }

        // for each thread, terminate it if it is not finished
        for(Thread t : threads) {
            if(t.isAlive()) {
                t.interrupt();
                System.out.println("Terminated transport");
            }
        }
        System.out.println("Finished transport");

    }


    public static class Policeman extends Thread {

        public void execute() throws InterruptedException {
            policaecVlezi.acquire();
            // waits until it is valid to enter the car
            System.out.println("Policeman enters in the car");


            lock.acquire();
            if((zatvorenik + policaec == 4) && policaec == 2 && zatvorenik == 0) {
                zatvorenikVlezi.release(2);
            }
            if((zatvorenik + policaec == 4) && policaec == 3 && zatvorenik == 0) {
                zatvorenikVlezi.release();
            }
            if((zatvorenik + policaec == 4) && policaec == 4) {
                trgni.release(4);
                // when the four passengers are inside, one policeman prints the starting command
                System.out.println("Start driving.");
            }
            lock.release();

            trgni.acquire();
            Thread.sleep(100);


            if(policaec + zatvorenik == 4) {
                // one policeman prints the this command to notice that everyone can exit
                System.out.println("Arrived.");
                izlezi.release(4);
            }
            // the exit from the car is allowed after the "Arrived." message is printed
            System.out.println("Policeman exits from the car");
            izlezi.acquire();
        }

        public void run() {
            try {
                execute();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    public static class Criminal extends Thread {

        public void execute() throws InterruptedException {
            zatvorenikVlezi.acquire();
            // waits until it is valid to enter the car
            System.out.println("Criminal enters in the car");

            lock.acquire();
            zatvorenik++;
            lock.release();

            trgni.acquire();
            Thread.sleep(100);
            izlezi.acquire();
            // the exit from the car is allowed after the "Arrived." message is printed
            System.out.println("Criminal exits from the car");

            lock.acquire();
            zatvorenik--;
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