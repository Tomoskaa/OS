/*package ispitnizadaci.prvkolokvium.sync.exam2014.septemvri;

import java.util.HashSet;
import java.util.concurrent.Semaphore;

public class TribeDinner {

    static Semaphore clen;
    static Semaphore shef;
    static Semaphore jadi;
    static Semaphore polnKazan;
    static Semaphore trpeza;


    public static void init() {
        clen = new Semaphore(4);
        shef = new Semaphore(0);
        jadi = new Semaphore(0);
        polnKazan = new Semaphore(0);
        trpeza = new Semaphore(0);
    }

    public static class TribeMember extends Thread {

        public TribeMember() {

        }

        public void execute() throws InterruptedException {

            //clenovite dobivaat dozvola da si zemat hrana
            clen.acquire();
            //se pravi proverka dali kazanot e prazen, ako e se povikuva shefot i go polni kazanot pa zatoa stavame zabrana
            //if(isPotEmpty()) {
                shef.release();
                polnKazan.acquire();
            //}
            //ako kazanot e poln mozat da zemaat hrana
            fillPlate();
            clen.release();

            //dobivam dozvola dali moze da sednam na masata  bidejki ima max 4 slobodni mesta
            trpeza.acquire();
            eat();
            //koga kje zavrsam so jadenje ja osloboduvam masata
            trpeza.release();

        }

        public void isPotEmpty() {
            int hrana;
            if(hrana == 0){
                System.out.println("Kazanot e prazen");
            }
            System.out.println("Kazanot e prazen");
        }

        public void fillPlate() {
            System.out.println("Zemanje hrana");
        }

        public void eat() {
            System.out.println("Jadenje na hranata");
        }

        public void cook() {
            System.out.println("Gotvenje na hranata");
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

    public static class Chef extends Thread {

        int numRuns;
        public Chef(int numRuns) {
            this.numRuns = numRuns;
        }

        public void execute() throws InterruptedException {

            shef.acquire();
            cook();
            polnKazan.release();

        }

        public void isPotEmpty() {
            System.out.println("Kazanot e prazen");
        }

        public void fillPlate() {
            System.out.println("Zemanje hrana");
        }

        public void eat() {
            System.out.println("Jadenje na hranata");
        }

        public void cook() {
            System.out.println("Gotvenje na hranata");
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
        for (int i = 0; i < 10; i++) {
            TribeMember tm = new TribeMember(i);
            threads.add(tm);
        }
        Chef shef = new Chef();
        threads.add(shef);

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
            }
            System.out.println("Finished");
        }
    }

}

 */
