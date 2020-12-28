import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class Scheduler extends Thread {

    public static Semaphore lock = new Semaphore(100);
    public static Semaphore start = new Semaphore(1);

    public static Random random = new Random();
    static List<Process> scheduled = new ArrayList<>();

    public static void main(String[] args) throws InterruptedException {
        // TODO: kreirajte 100 Process thread-ovi i registrirajte gi
        for(int i = 0; i < 100; i++) {
            Process process = new Process();
            scheduled.add(process);
            process.start();
        }

        // TODO: kreirajte Scheduler i startuvajte go negovoto pozadinsko izvrsuvanje
        Scheduler scheduler = new Scheduler();
        scheduler.start();

        // TODO: Cekajte 20000ms za Scheduler-ot da zavrsi
        scheduler.join(20000);

        // TODO: ispisete go statusot od izvrsuvanjeto
        if(scheduler.isAlive()) {
            scheduler.interrupt();
            System.out.println("Terminated scheduling");
        } else {
            System.out.println("Finished scheduling");
        }
    }

    public static void register(Process process) {
        scheduled.add(process);
    }

    public Process next() {
        if (!scheduled.isEmpty()) {
            return scheduled.remove(0);
        }
        return null;
    }

    public void run() {
        try {
            while (!scheduled.isEmpty()) {
                Thread.sleep(100);
                System.out.print(".");

                try {
                    // TODO: zemete go naredniot proces
                    start.acquire();

                    lock.acquire();
                    // TODO: povikajte go negoviot execute() metod
                    next().execute();
                    // TODO: cekajte dodeka ne zavrsi negovoto pozadinsko izvrsuvanje
                    lock.release();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Done scheduling!");
    }


    public static class Process extends Thread {

        public Integer duration;

        public Process() throws InterruptedException {
            this.duration = Scheduler.random.nextInt(1000);
        }


        public void execute() {
            try {
                lock.acquire();
                System.out.println("Executing[" + this + "]: " + duration);
                // TODO: startuvajte go pozadinskoto izvrsuvanje
                start.release();
                lock.release();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

        public void run() {
            try {
                execute();
                Thread.sleep(this.duration);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}