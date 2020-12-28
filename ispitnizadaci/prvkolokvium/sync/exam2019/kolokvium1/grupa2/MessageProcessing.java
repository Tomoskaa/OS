package ispitnizadaci.prvkolokvium.sync.exam2019.kolokvium1.grupa2;


import java.util.HashSet;
import java.util.concurrent.Semaphore;


public class MessageProcessing {

    public static boolean active = false;
    public static Semaphore processActive = new Semaphore(1);
    public static Semaphore waitForMessages = new Semaphore(0);
    public static Semaphore requestMessage = new Semaphore(0);
    public static Semaphore getMessage = new Semaphore(0);
    public static Semaphore doneProcessing = new Semaphore(0);
    public static Semaphore waitToLeave = new Semaphore(0);
    public static int readyMessages = 0;
    public static Semaphore countSemaphore = new Semaphore(1);


    public static void main(String[] args) throws InterruptedException {
        HashSet<Thread> threads = new HashSet<Thread>();
        for (int i = 0; i < 50; i++) {
            MessageSource ms = new MessageSource();
            threads.add(ms);
        }
        threads.add(new Processor());
        //TODO: start all threads in background
        for(Thread t : threads) {
            t.start();
        }

        //TODO: after all of them are started, wait each of them to finish for 1_000 ms
        for(Thread t : threads) {
            t.join(1_000);
        }

        //TODO: after the waiting for each of the players is done, check the one that are not finished and terminate them
        for(Thread t : threads) {
            if(t.isAlive()) {
                t.interrupt();
                System.err.println("Possible deadlock");
            }
        }
    }


    public static class Processor extends Thread {


        public void execute() throws InterruptedException {
            int processedMessages=0;

            while(processedMessages < 50) {

                processActive.acquire();
                if(!active) {
                    processActive.release();
                    waitForMessages.acquire();
                    System.out.println("Activate processing");
                    active = true;
                }
                processActive.release();
                System.out.println("Request message");
                requestMessage.release();
                getMessage.acquire();
                System.out.println("Process message");
                Thread.sleep(200);

                doneProcessing.release();

                waitToLeave.acquire();

                processActive.acquire();
                if(!active)
                    System.out.println("Processing pause");
                processActive.release();

                processedMessages++;
            }
        }

        public void run() {
            try {
                execute();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static class MessageSource extends Thread {


        public void execute() throws InterruptedException {
            Thread.sleep(50);
            System.out.println("Message ready");
            // wait until the processor requests the message
            System.out.println("Provide message");
            // wait until the processor is done with the processing of the message
            System.out.println("Message delivered. Leaving.");
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