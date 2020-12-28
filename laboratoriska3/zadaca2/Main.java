package laboratoriska3.zadaca2;

import java.util.concurrent.Semaphore;

class H2OMachine {

    static Semaphore h;
    static Semaphore o;
    static Semaphore hHere;
    static Semaphore oHere;
    static Semaphore ready;

    public static void init() {
        h = new Semaphore(2);
        o = new Semaphore(1);
        hHere = new Semaphore(0);
        oHere = new Semaphore(0);
        ready = new Semaphore(0);
    }

    String[] molecule;
    int count;

    public H2OMachine() {
        molecule = new String[3];
        count = 0;
    }

    public void hydrogen() throws InterruptedException {
        // TODO: 3/29/20 synchronized logic here
        System.out.println("The molecule is formed");

        h.acquire();;
        hHere.release();
        oHere.acquire();
        ready.acquire();
        h.release();
    }

    public void oxygen() throws InterruptedException {
        // TODO: 3/29/20 synchronized logic here
        System.out.println("The molecule is formed");

        o.acquire();
        hHere.acquire(2);
        oHere.release(2);
        ready.release(2);
        o.release();
    }
}
class H2OThread extends Thread {

    H2OMachine molecule;
    String atom;

    public H2OThread(H2OMachine molecule, String atom){
        this.molecule = molecule;
        this.atom = atom;
    }

    public void run() {
        if ("H".equals(atom)) {
            try {
                molecule.hydrogen();
            }
            catch (Exception e) {
            }
        }
        else if ("O".equals(atom)) {
            try {
                molecule.oxygen();
            }
            catch (Exception e) {
            }
        }
    }
}

public class Main
{
    public static void main(String[] args) {

        // TODO: 3/29/20 Simulate with multiple scenarios
        H2OMachine molecule = new H2OMachine();

        Thread t1 = new H2OThread(molecule,"H");
        Thread t2 = new H2OThread(molecule,"O");
        Thread t3 = new H2OThread(molecule,"H");
        Thread t4 = new H2OThread(molecule,"O");

        t2.start();
        t1.start();
        t4.start();
        t3.start();
    }
}
