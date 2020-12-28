package ispitnizadaci.prvkolokvium.sync.exam2017.kolokvium1.grupa1;

import java.util.HashSet;
import java.util.concurrent.Semaphore;

public class MusicBand {

    static final int MUZICARI = 5;

    static Semaphore pejaci;
    static Semaphore gitaristi;
    static Semaphore muzicari;
    static Semaphore zavrsi;
    static Semaphore lock;
    static int numPejaci;
    static int numGitaristi;

    public static void init() {

        pejaci = new Semaphore(2);
        gitaristi = new Semaphore(3);
        muzicari = new Semaphore(0);
        zavrsi = new Semaphore(0);
        lock = new Semaphore(1);
        numPejaci = 0;
        numGitaristi = 0;
    }


    public static class Singer extends Thread {

        int numRuns;
        public Singer(int numRuns) {
            this.numRuns = numRuns;
        }

        public void execute() throws InterruptedException {

            //moze da vleguvaat pejaci
            pejaci.acquire();
            lock.acquire();
            numPejaci++;
            if(numPejaci == 2) {
                //numPejaci = 0;
                muzicari.release(MUZICARI);
            }
            lock.release();

            muzicari.acquire();
            play();
            //muzicari.release();


            lock.acquire();
            numPejaci--;
            if(numPejaci == 0) {
                evaluate();
                zavrsi.release(MUZICARI);
                pejaci.release(2);
                gitaristi.release(3);
                //numPejaci = 0;
            }
            lock.release();
            pejaci.release();
            zavrsi.acquire();
        }

        public void play() {
            System.out.println("Moze da se formira grupa i da nastapi.");
        }

        public void evaluate() {
            System.out.println("Proverka dali nastapot bil uspesen.");
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

    public static class GuitarPlayer extends Thread {

        int numRuns;
        public GuitarPlayer(int numRuns) {
            this.numRuns = numRuns;
        }

        public void execute() throws InterruptedException {

            gitaristi.acquire();
            lock.acquire();
            numGitaristi++;
            if(numGitaristi == 3) {
                //numGitaristi = 0;
                muzicari.release(MUZICARI);
            }
            lock.release();

            muzicari.acquire();
            play();
            //muzicari.release();

            lock.acquire();
            numGitaristi--;
            if(numGitaristi == 0) {
                evaluate();
                zavrsi.release(5);
                gitaristi.release(3);
                pejaci.release(2);
                //numGitaristi = 0;
            }
            lock.release();

            gitaristi.release();
            zavrsi.acquire();
        }

        public void play() {
            System.out.println("Moze da se formira grupa i da nastapi.");
        }

        public void evaluate() {
            System.out.println("Proverka dali nastapot bil uspesen.");
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
        for(int i = 0; i < 2; i++) {
            Singer s = new Singer(i);
            threads.add(s);
        }

        for(int i = 0; i < 3; i++) {
            GuitarPlayer gp = new GuitarPlayer(i);
            threads.add(gp);
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
                System.out.println("Possible deadlock.");
            }
        }
        System.out.println("Finished.");
    }

}
