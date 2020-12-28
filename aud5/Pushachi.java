package aud5;

import java.util.HashSet;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class Pushachi {

    //ovde definirajte gi semaforite
    static Semaphore accessBuffer;
    static Semaphore []smokers;
    static Semaphore notify;

    static State state = new State();

    public static void init() {
        //ovde napravete init
        accessBuffer = new Semaphore(1);
        smokers = new Semaphore[3];
        for (int i = 0; i < 3; i++) {
            smokers[i] = new Semaphore(0);
        }
        notify = new Semaphore(0);
    }

    public static class Smoker extends Thread {

        int type;
        public Smoker(int type) {
            this.type = type;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    execute();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        private void execute() throws InterruptedException {
            //baram dozvola za da ja dodadam sostojkata koja ja imam
            smokers[type].acquire();
            //bidejki vo baferot imam 2 sostojki baram dozvola za da ja napravam cigarata
            accessBuffer.acquire();
            //pravam proverka dali sostojkata koja ja imam e soodvetna za da napravam cifara
            if (state.hasMyItems(type)) {
                //ako e taa sostojka koja se bara da se dodadi ja pusam cigarata
                state.consume(type);
            }
            //go osloboduvam baferot za agentot da mozi da dodade naredni 2 sostojki
            accessBuffer.release();
            //izvesti go agentot
            notify.release();
        }
    }

    public static class Agent extends Thread {
        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    execute();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        private void execute() throws InterruptedException {
            //baram dozvola za da mozam da dodadam sostojki
            accessBuffer.acquire();
            //go povikuvam metodot dodadi
            state.putItems();
            //sum zavrsil rabota treba da gi izvestam pusacite deka moze da dodavaat sostojki
            for (int i = 0; i < 3; i++) {
                smokers[i].release();
            }
            //go osloboduvam baferot
            accessBuffer.release();
            //davam zabrana na pusacite da dodavaat sostojka bidejki mozi samo eden od niv da ima dozvola
            notify.acquire(3);
        }
    }

    public static class State {
        boolean[] tableItems;

        public State() {
            tableItems = new boolean[3];
        }


        public void putItems() {
            int temp = new Random().nextInt(3);
            int prvaSostojka = (temp + 1) % 3;
            int vtoraSostojka = (temp + 3 - 1) % 3;
            System.out.println("Putting items for types: " + prvaSostojka + " and " + vtoraSostojka);
            tableItems[prvaSostojka] = true;
            tableItems[vtoraSostojka] = true;
        }

        public boolean hasMyItems(int type) {
            int prvaSostojka = (type + 1) % 3;
            int vtoraSostojka = (type + 3 - 1) % 3;
            return tableItems[prvaSostojka] && tableItems[vtoraSostojka];
        }

        public void consume(int type) {
            System.out.println("Consuming items. Smoker type: " + type);
            tableItems = new boolean[3];
        }

    }

    public static void main(String[] args) {
        HashSet<Thread> threads = new HashSet<>();
        for (int i = 0; i < 3; i++) {
            threads.add(new Smoker(i));
        }
        threads.add(new Agent());
        init();
        for (Thread t : threads) {
            t.start();
        }

        for (Thread t: threads) {
            try {
                t.join(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
