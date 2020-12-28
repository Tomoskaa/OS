package aud4.davcev;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SyncTester {
    public static void main(String[] args) throws InterruptedException {
        List<ExampleThread> threads = new ArrayList<>();

        //gi gledame koi se vrednostite vo konstruktorot i niv gi zapisuvame, ednata e za threadPrivateField, a drugata za IntegerWrapper
        //pravime 2 promenlivi
        int defaultValue = 0; //kje ja primaat ovaa vrednost site nitki
        IntegerWrapper wrapper = new IntegerWrapper();

        for(int i = 0; i < 100; i++) {
            ExampleThread exampleThread = new ExampleThread(defaultValue, wrapper);
            threads.add(exampleThread); //gi dodavame site 100 threadovi na ovoj nacin
        }

        //namesto ExampleThread moze da stoi i samo Thread bidejki od nea e nasledena ovaa klasa(moze da stoi bilo koja bazna klasa)
        for(ExampleThread t : threads) {
            t.start();
        }

        //problem pri public promenlivata bidejki e pristapena od drug metod =>main
        for(ExampleThread t : threads) {
            //t.safeIncrementThreadPublicField();
            t.safeIncrementIntegerWrapper();
        }

        for(ExampleThread t : threads) {
            t.join();   //cekame da zavrsat site thread-ovi
        }
        System.out.println(wrapper.getValue());
    }
}

class IntegerWrapper {
    private int value;

    public void increment() {
        this.value++;
    }

    public int getValue() {
        return value;
    }
}

class ExampleThread extends Thread {

    //primitivna promenliva i bidejki e private nikoj ne moze da ja pristapi
    private int threadPrivateField;

    //bidejki imame public promenliva toa znaci deka nekoja druga promenliva moze da pristapi do nejze i da i ja promeni vrednosta
    //imame 3 mozni nacini da ja zasitime public promenlivata
    Lock lockThreadPublicField;
    Semaphore semaphoreThreadPublicField;
    Object mutex;
    public int threadPublicField;

    public static int threadStaticField;

    Semaphore semaphoreIntegerWrapperLocal;
    //bidejki e staticki semafor moze odma da se inicijalizira
    static Semaphore semaphoreIntegerWrapperGlobal = new Semaphore(1);
    static Object staticMutex;  //vo synchronized moze da se stavi ova i togas kje raboti
    private IntegerWrapper wrapper;

    //init primitivna promenliva gi postavuva na 0 site promenlivi
    public ExampleThread(int init, IntegerWrapper wrapper) {
        this.threadPrivateField = init;
        this.wrapper = wrapper;

        //gi inicijalizirame 3te nacini na sinhronizacija
        this.lockThreadPublicField = new ReentrantLock();
        this.semaphoreThreadPublicField = new Semaphore(1);
        this.mutex = new Object();

        this.semaphoreIntegerWrapperLocal = new Semaphore(1);
    }

    //funkcija so koja simulirame thread-ot da zaspie odnosno vremeto na izvrsuvanje da go zeme drugiot thread
    //na toj nacin go zastituvame ovoj metod
    public static void forceSwitch(int miliseconds) {
        try {
            Thread.sleep(miliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void incrementThreadPrivateField() {
        this.threadPrivateField++;
        int localVar = this.threadPrivateField;
        this.forceSwitch(300);
        //proverka dali lokolanata promenliva e razlicna od promenlivata koja ja imame vo klasata t.e localVar
        //dali vo pozadina nekoja druga promenliva ne i ja promenila vrednosta testirame
        if(localVar != this.threadPrivateField) {
        //    System.err.println(String.format("private-mismatch-%d", getId()));
        } else {
        //    System.out.println(String.format("private-%d, value: %d", getId(), this.threadPrivateField));
        }
    }


    //pravime sinhronizacija na public promenlivata, ovozmozuva ovaa funkcija da e povikana samo ednas od dadena nitka
    public void safeIncrementThreadPublicField() {
        //reshenie so monitor
//        synchronized (mutex) { //vaka se upotrebuvaaat monitori moze da stavime vo zagrada i (this)
//            this.incrementThreadPublicField();
//        }

        //reshenie so lock

//        lockThreadPublicField.lock();
//        this.incrementThreadPublicField();
//        lockThreadPublicField.unlock();

        //reshenie so semafori

        try {
            semaphoreThreadPublicField.acquire();
            this.incrementThreadPublicField();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            semaphoreThreadPublicField.release();
        }
    }

    public void incrementThreadPublicField() {
        this.threadPublicField++;
        int localVar = this.threadPrivateField;
        this.forceSwitch(30);
        if(localVar != this.threadPublicField) {
            System.err.println(String.format("public-mismatch-%d", getId()));
        } else {
        //    System.out.println(String.format("public-%d, value: %d", getId(), this.threadPublicField));
        }
    }

    public void incrementThreadStaticField() {
        threadStaticField++;
        int localVar = this.threadPrivateField;
        forceSwitch(30);
        if(localVar != threadStaticField) {
        //    System.err.println(String.format("static-mismatch-%d", getId()));
        } else {
        //    System.out.println(String.format("static-%d, value: %d", getId(), threadStaticField));
        }
    }

    public void safeIncrementIntegerWrapper() throws InterruptedException {

//        //vazi za site instanci od edna klasa
//        synchronized (ExampleThread.class) { //nema da raboti so synchronized(this)
//            this.incrementIntegerWrapper();
//        }

    /*
        //bez static semafor ne funkcionira kodot
        try{
            this.semaphoreIntegerWrapperGlobal.acquire();
            this.incrementIntegerWrapper();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            this.semaphoreIntegerWrapperGlobal.release();
        }
     */

    //site tredovi da se incrementiraat samo dokolku wrapper e < 5
        //mora da go izvadime nadvor bidejki promenlivta value nema da ni bidi zastitena
        //bidejki go vadime nadvor aquire treba i realese inaku kje imame deadlock


        semaphoreIntegerWrapperGlobal.acquire();
        if(wrapper.getValue() < 5) {
            this.incrementIntegerWrapper();
        }
        semaphoreIntegerWrapperGlobal.release();

    }

    public void incrementIntegerWrapper() {
        this.wrapper.increment();
        int localVar = this.wrapper.getValue();
        forceSwitch(30);
        if(localVar != this.wrapper.getValue()) {
            System.err.println(String.format("wrapper-mismatch-%d", getId()));
        }   else {
        //    System.out.println(String.format("wrapper-%d, value: %d", getId(), this.wrapper.getValue()));
        }
    }

    @Override
    public void run() {
        //incrementThreadPrivateField();
        //safeIncrementThreadPublicField();
        //incrementThreadStaticField();
        try {
            safeIncrementIntegerWrapper();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
