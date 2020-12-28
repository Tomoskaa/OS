package aud4.misev;

import java.util.concurrent.Semaphore;

//definiranje na site semafori koi ni trebaat vo zadacata
public class Locks {


    //go kontrolira producer-ot, odnosno koga treba da se postavat produkti vo buffer-ot
    //mora da e static za da moze da go koristat site klasi (mora da se napravi sinhronizacija)
    public static Semaphore bufferEmpty;        //kaj producerot treba da imame semafo mutex koj kje ni kazuva dali moze da go polni buffrot ili ne
    //go kontrolira pristapot na spodelena promenliva numItems
    public static Semaphore bufferLock;         //za da se kontrolira pristapot do buffer-ot i polnejeto negovo treba nov semafor
    //za consumer-ite ni e potrebna niza od semafori
    public static Semaphore items[];

    //inicijalizacijata na semaforite se pravi vo ramki na init metodot



}
