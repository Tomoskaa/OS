package aud5;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class ProducerController {

    public static int NUM_RUN = 50;

    static Semaphore accessBuffer;
    static Semaphore lock;
    static Semaphore canCheck;



    public static void init() {
        accessBuffer = new Semaphore(1);
        lock = new Semaphore(1);
        canCheck = new Semaphore(10);
    }

    public static class Buffer {

        public int numChecks = 0;

        public void produce() {
            System.out.println("Producer is producing...");
        }

        public void chceck() {
            System.out.println("Controller is checking...");
        }
    }

    public static class Producer extends Thread {
        private final Buffer buffer;
        public Producer(Buffer b) {
            this.buffer = b;
        }

        public void execute() throws InterruptedException {
            //se bara dozvola za da pristapi producer-ot
            accessBuffer.acquire();
            this.buffer.produce();
            accessBuffer.release();
        }

        @Override
        public void run() {
            for(int i =0; i < NUM_RUN; i++) {
                try {
                    execute();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static class Controller extends Thread {
        private final Buffer buffer;
        public Controller(Buffer b) {
            this.buffer = b;
        }

        public void execute() throws InterruptedException {
            //ako brojot na stavki koi treba da gi stavaat kontrolerite e 0, toa znaci deka najverojatno producerot e na red da proizveduva
            //i kje cekame da sme izvesteni od baferot t.e da ni dade dozvola
            //bidejki numCheck e spodelena promenliva od povekje kontroleri mora da ja zastitime
            lock.acquire();
            if(this.buffer.numChecks == 0) {
                accessBuffer.acquire();
                //this.buffer.chceck();
            }
            this.buffer.numChecks++;
            lock.release();

            //go povikuvame canCheck so cel da vidime dali kje ni e dozvoleno da vlezime biedjki max moze da ima 10 stavki
            canCheck.acquire();
            this.buffer.chceck();
            //so toa sto se dozvoluva da vnesuvame stavki mora i da ja namaluvame vrednosta na numChecks t.e
            //ako vensime edna stavka ni ostanuvaat da vnesime uste max 9 ako na pocetok e 0
            lock.acquire();
            this.buffer.numChecks--;
            canCheck.release();
            //treba da napravime proverka sto ako controlerot e posleden t.e nema posle nego koj da ja vrsi rabotata i treba da mu dademe prednost na prod.
            //ako dojde producer da mu se dozvoli da polni stavki
            if(this.buffer.numChecks == 0) {
                accessBuffer.release();
            }
            lock.release();

            /*
            //ako brojot na stavki e pogolem od 0 toa znaci deka kontrolerite se na red da pristapat do baferot i moze da se vklucat uste
            if(this.buffer.numChecks > 0) {
                //nema potreba da pravam proverka na accessBuffer-ot bidejki producer-ot nema sansi da pristapi dodeka ne zavrsat kontrolerite
                this.buffer.chceck();
            }

             */

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
    }

    public static void main(String[] args) {
        int numControllers = 10;
        init();
        Buffer buffer = new Buffer();
        Producer p = new Producer(buffer);
        List<Controller> controllers = new ArrayList<>();
        for(int i = 0; i < numControllers; i++) {
            controllers.add(new Controller(buffer));
        }
        p.start();
        for(int i = 0; i < numControllers; i++) {
            controllers.get(i).start();
        }
    }

}
