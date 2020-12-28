package aud4.misev;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class Application {

    static final int NUM_RUNS = 150;    //izvrsuvanjeto da odi do kraj zatoa final

    public static void main(String[] args) {
        //definirame se sto ni se sostoi vo crtezot na zadacata 1-Prod., 1-Buffer, i pr. 100-Cons.
        int numConsumers = 100;     //moze da se vcitaat i od standarden vlez tuka se hardkodirani seedno
        init(numConsumers);     //povik na metodot -> inicijalizacija so semafori
        Buffer b = new Buffer(numConsumers);    //spodelen resurs
        Producer p = new Producer(b);
        //bidejki imame povekje consumeri definirame lista od consumer-i
        List<Consumer> consumers = new ArrayList<>();
        for(int i = 0; i < numConsumers; i++) {
            consumers.add(new Consumer(i,b));   //go povikuvame konstruktorot koj prethodno go kreiravme
        }

        //se povikuva ovoj metod za da zapocne izvrsuvanjeto na site thread-ovi koi gi kreiravme
        p.start();
        for(int i = 0; i < numConsumers; i++) {
            consumers.get(i).start();   //lista od consumer-i i go povikuvame sekoj posebno
        }
    }

    //metodot init mora da go povikame na pocetok pred metodot start()!!!!!!!!!!!! inaku dzabe zato so se kje se izvrsi kje se dobie "cuden" rezultat
    //i otposle kje se povika init ne se vrsi sinhronizacija

    //definiranje na semafori so broj na permisii
    public static void init(int numConsumers) {
        Locks.bufferEmpty = new Semaphore(1);       //ima 1 dozvola zatoa sto samo producer-ot na pocetok treba da ja ima taa dozvola i nikoj drug, treba da go napolni buffer-ot pa posle consumer-ite da pristapuvaat
        Locks.bufferLock = new Semaphore(1);        //ovoj semafor samo proveruva dali buffer-ot e poln ili ne i sluzi za producer-ot ako e prazen ima dozvola da go polni buffer-ot ako ne treba da ceka
        Locks.items = new Semaphore[numConsumers];

        for(int i = 0; i <numConsumers; i++) {
            Locks.items[i] = new Semaphore(0);      //treba da cekaat producer-ot da go napolni buffer-ot
        }
    }

}
