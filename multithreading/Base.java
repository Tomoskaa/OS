package multithreading;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static java.lang.Thread.*;

class ThreadExecutor extends Thread {

    //se dodava parametar na konsturktor pri kreiranje na samata niska i kje ima lokalni promenlivi
    private String name;
    private Incrementor incrementor;

    public ThreadExecutor(String name, Incrementor incrementor) {
        this.name = name;
        this.incrementor = incrementor;
    }
    @Override
    public void run() {
        //kodot sto se naogja vo metodot run kje se izvrsi vo istata niska sto go povikala toj metod
        //nema da se kreira nova niska, nema da se kreira nov thread, tuku vo ramki na main thread - ot kje ni se izvrsi kodot sto se naogja vo run
        // System.out.println("Text from thread");

        //sakam da kreiram 2 niski koi kje go izvrsuvaat kodot paralelno
        //za da mozat da se identifikuvaat niskite na sekoja i se zadava ime
        for(int i = 0; i < 20; i++) {
            //System.out.println(name + ":" + i);
            incrementor.safeSynchronizedIncrement();
        }

        //od run metodot na ThreadExecutor se kreira nova niska isto od tipot ThreadExecutor koja kje se izvrsuva paralelno
        ThreadExecutor executor = new ThreadExecutor(name + "child", incrementor);
        executor.start();
    }
}

//sekoj objekt vo java ima Monitor
//bidejki rezultatot e nepredvidliv i za toa da se promeni potrebno e da se napravi sinhtronizacija
class Incrementor {
    private int count = 0;
    private Lock lock = new ReentrantLock();
    private Semaphore semaphore = new Semaphore(1); //tuka kje ni bara da definirame inicjalen broj na dozvoli(klucevi) koi kje moze da gi zemat niskite
    //koi kje gi pobaat za da moze da pristapat do kriticen region i da moze da izvrsat odredeni naredbi pa da gi oslobodat

    //Thread1 e prviot go povikuva metodot increment, gleda vo monitorot na objektot Incrementor dali ima dozvola da vleze vo toj increment
    //i bidejki mutex-ot e sloboen Thread1 vleguva, koga kje zavrsi Thread1 vremenski slot dobiva Thread2 i toj go povikuva metodot increment
    //no bidejki ima synchronized Thread2 prasuva dali moze da dobie dozvola za da vleze, no Thread1 uste ja nema vrateno dozvolata
    //i Thread2 mora da ceka duri Thread1 kje zavrsi so zgolemuvanje na vrednosta na count, i so toa sekogas kje se dobiva tocen rezultat
    //synchronized void increment() {
    // count++;

    void unsafeIncrement() {
        //synchronized (this) {     ova nema potreba da se pisuva vo slucaj koga kje se pristapuva do 2 razlicni incrementor-i
        synchronized (Incrementor.class) { //
            lock.lock();    //se zafakja prostor so Thread1
            count++;        //kriticen regioon bidejki 2 niski se obiduvaat da pristapat
            //lock.unlock();  //se osloboduva prostor
            //vo slucaj da se zaboravi unlock() togas samo Thread1 kje zavrsi so rabota t.e dobiva dozvola a dodeka pak Thread2 kje moze da vleze no
            //nema da ima pristap za da se zavrsi so rabota(izvrsuvanje)
        }
        //}
    }

    void safeIncrementWithSemaphore() throws InterruptedException {     //sekoja niska sto kje go povikuva ovoj metod kje bara dozvola od semaforot za da velze vo kriticniot region -> count++
        semaphore.acquire();    //ako ne e definirano vo (broj)  inicijalno e 1 dozvola
        count++;
        semaphore.release();
    }


    void safeSynchronizedIncrement() {
        synchronized (this) {
            count++;
        }
    }

    void safeLockedIncrement() {
        lock.lock();
        count++;
        lock.unlock();
    }

    //count++ se sveduva na nekolku posledovatelni operacii
    //read count
    //add 1 to count
    //write count to memory
       /*
       try {
            Thread.sleep(1);    //sleep e metod koj nalseduva od Thread i ja blokira tekovnata niska za 1ms
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        */

    int getCount() {
        return this.count;
    }
}


public class Base {


    //drug nacin na izvrsuvanje na niski so pomos na runnable
    public static void threadWithRunnable() {

        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i=0; i < 1000; i++) {
                    System.out.println("New thread !");
                }
            }
        });

        t1.start();
        System.out.println("Main thread exiting");
    }



    public static void main(String[] args) throws InterruptedException {
        threadWithRunnable();

        //kreiranje objekt od klasata Incrementor
        Incrementor incrementor = new Incrementor();
        //Incrementor incrementor = new Incrementor();
        //i tuka kje se smenat incrementorite ako se koristat 2 razlicni

        //ThreadExecutor executor = new ThreadExecutor();   so run metod
        //executor.run();   so run metod

        //dokolku sakame kodot da se izvrsi vo nova niska se pravi so povikuvanje na metodot start()
        // vo pozadina se kreira nova niska i istata se stava vo sostojba ready

        //koga imame 2 thread - a i gi povikuvame so metodot start() tie kje se pecatat izmesano i pri 10 razlicni izvsuvanje kje dobieme 10 razlicni resenija
        //dokolku imame singhronizacija so koristenje na 2 razlicni incrementori
        //Thread executor1 = new ThreadExecutor("Thread 1", incrementor1);
        //Thread executor2 = new ThreadExecutor("Thread 2", incrementor2);
        Thread executor1 = new ThreadExecutor("Thread 1", incrementor);
        Thread executor2 = new ThreadExecutor("Thread 1", incrementor);

        //otkako kje se kreiraat i kje se stavat vo sostojba na cekanje ready i mu ostanuva uste vreme(nekolku ms) i go zema count metodot i nego go pecati
        //zatoa pri pecatenje ni dava 0(no ne mora da znaci deka kje e sekogas)
        executor1.start();
        executor2.start();

        //metod so koj kje se dobie vrednosta na executor-ot se pravi so pomos na join odnosno ceka tie da zavrsat
        //i vo megjuvreme ne se odi na sledno izvrsuvanje, otkako ovie dve kje zavrsat togas se zema vrednosta na count i se pecati
        executor1.join(1000);  //prvata niska neka izvrsuva 5ms kolku stigne da zavrsi tolku posle prodolzi so drugata niska isto 5ms
        executor2.join(1000);


        if(executor1.isAlive() && executor2.isAlive()) {   //proveruva dali prvata i vtorata niska se uste zivi
            System.out.println("Deadlock");
            executor1.interrupt();      //prisilno gi prekinuvame niskite
            executor2.interrupt();
        }


        int count = incrementor.getCount();
        //System.out.println("Result is: " + count);

    }
}
//RAZLIKA MEGJU LOCK I SEMAPHORE
//So semaphore mozes poveke dozvoli da davas
// Lock-ot дозволува само една нишка да го внесе делот што е заклучен и заклучувањето не се дели со други процеси.
//Semaphore so eden permit e isto so lock