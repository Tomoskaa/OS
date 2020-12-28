package laboratoriska3.zadaca4;


import java.util.concurrent.Semaphore;

public class Singleton extends Thread{

    private static volatile Singleton singleton;
    public static Semaphore lock = new Semaphore(1);
    private static boolean dozvola = true;

    private Singleton() {
    }

    public static Singleton getInstance() throws InterruptedException {
        // TODO: 3/29/20 Synchronize this
        lock.acquire();

        if(dozvola == true) {
            singleton = new Singleton();
            dozvola = false;
        }
        lock.release();

        return singleton;
    }

    public static void main(String[] args) throws InterruptedException {
        // TODO: 3/29/20 Simulate the scenario when multiple threads call the method getInstance

        for (int i = 0; i < 10; i++) {
            Singleton t = new Singleton();
            t.getInstance();
        }
    }

}
