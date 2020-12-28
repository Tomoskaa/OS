package ispitnizadaci.prvkolokvium.sync.exam2014.januari.grupa1;

import java.util.HashSet;
import java.util.concurrent.Semaphore;

public class SchoolBus {

    static Semaphore vozacVlezi;
    static Semaphore vozacIzlezi;
    static Semaphore studentVlezi;
    static Semaphore trgni;
    static Semaphore studentIzlezi;
    static Semaphore lock;
    static int numStudents;

    public static void init() {
        vozacVlezi = new Semaphore(1);
        vozacIzlezi = new Semaphore(0);
        studentVlezi = new Semaphore(0);
        studentIzlezi = new Semaphore(0);
        trgni = new Semaphore(0);
        lock = new Semaphore(1);
        numStudents = 0;
    }

    public static class Vozac extends Thread {

        public Vozac() {
        }

        public void execute() throws InterruptedException {

            vozacVlezi.acquire();
            lock.acquire();
            driverEnter();
            studentVlezi.release(50);
            lock.release();

            trgni.acquire();

            lock.acquire();
            busDeparture();
            busArrive();
            studentIzlezi.release(50);
            lock.release();

            vozacIzlezi.acquire();
            lock.acquire();
            driverLeave();
            vozacVlezi.release();
            lock.release();

        }

        public void driverEnter() {
            System.out.println("Driver entering in the bus.");
        }

        public void studentEnter() {
            System.out.println("Student entering in the bus.");
        }

        public void busDeparture() {
            System.out.println("The bus is departuring.");
        }

        public void busArrive() {
            System.out.println("Driver entering in the bus.");
        }

        public void studentLeave() {
            System.out.println("Student leaving in the bus.");
        }

        public void driverLeave() {
            System.out.println("Driver leaving in the bus.");
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

    public static class Student extends Thread {

        int numRuns;
        public Student(int numRuns) {
            //super(numRuns);
            this.numRuns = numRuns;
        }

        public void execute() throws InterruptedException {

            studentVlezi.acquire();
            //lock.acquire();
            studentEnter();
            lock.acquire();
            numStudents++;
            if(numStudents == 50) {
                trgni.release();
            }
            lock.release();

            trgni.acquire();

            studentIzlezi.acquire();
            //lock.acquire();
            studentLeave();
            lock.acquire();
            numStudents--;
            if(numStudents == 0) {
                //numStudents = 0;
                vozacIzlezi.release();
            }
            lock.release();
        }

        public void driverEnter() {
            System.out.println("Driver entering in the bus.");
        }

        public void studentEnter() {
            System.out.println("Student entering in the bus.");
        }

        public void busDeparture() {
            System.out.println("The bus is departuring.");
        }

        public void busArrive() {
            System.out.println("Driver entering in the bus.");
        }

        public void studentLeave() {
            System.out.println("Student leaving in the bus.");
        }

        public void driverLeave() {
            System.out.println("Driver leaving in the bus.");
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
        for (int i = 0; i < 10; i++) {
            run();
        }
    }

    public static void run() {

        HashSet<Thread> threads = new HashSet<Thread>();

        for (int i = 0; i < 10; i++) {
            Student s = new Student(i);
            threads.add(s);
        }
        Vozac v = new Vozac();
        threads.add(v);

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
            }
            System.out.println("Finished");
        }
    }
}
