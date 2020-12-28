package ispitnizadaci.prvkolokvium.sync.exam2018.januari;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class EventProcessor extends Thread {

    static Semaphore lock = new Semaphore(5);
    static Semaphore dozvola = new Semaphore(1);

    public static Random random = new Random();
    static List<EventGenerator> scheduled = new ArrayList<>();

    public static void main(String[] args) throws InterruptedException {

        List<EventProcessor> processors = new ArrayList<>();
        // TODO: kreirajte 20 Processor i startuvajte gi vo pozadina
        for (int i = 0; i < 20; i++) {
            EventProcessor p = new EventProcessor();
            processors.add(p);
            //TODO: startuvajte go vo pozadina
            p.start();
        }

        for (int i = 0; i < 100; i++) {
            EventGenerator eventGenerator = new EventGenerator();
            //TODO: startuvajte go eventGenerator-ot
            eventGenerator.start();
        }


        for (int i = 0; i < 20; i++) {
            EventProcessor p = processors.get(i);
            // TODO: Cekajte 20000ms za Processor-ot p da zavrsi
            p.join(20000);


            // TODO: ispisete go statusot od izvrsuvanjeto
            if(p.isAlive()) {
                p.interrupt();
                System.out.println("Terminated processing");
            }
            else {
                System.out.println("Finishd processing");
            }
        }
    }


    public static void process() {
        // TODO: pocekajte 5 novi nastani
        try {
            dozvola.acquire();
            lock.acquire(5);
            System.out.println("processing event");
            lock.release(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    static class EventGenerator extends Thread {

        public Integer duration;

        public EventGenerator() throws InterruptedException {
            this.duration = EventProcessor.random.nextInt(1000);
        }


        /**
         * Ne smee da bide povikan paralelno kaj poveke od 5 generatori
         */
        public static void generate() {
            try {
                lock.acquire();
                System.out.println("Generating event: ");
                dozvola.release();
                lock.release();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        public void run() {
            try {
                generate();
                Thread.sleep(this.duration);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}