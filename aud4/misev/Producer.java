package aud4.misev;

import java.util.concurrent.Semaphore;

//producer-ot pravi samo povik do fillBuffer i toj e od tipot Thread i edinstveno sto treba da se implementira e metodot execute()
public class Producer extends Thread {

    private Buffer buffer;

    //numItems koj se naogja vo buffer-ot isto taka treba da bide spodelen i toa se pravi so pomos na konstruktor
    public Producer(Buffer buffer) {
        this.buffer = buffer;
    }

    //negova zadaca e da go zema spodeleniot resurs od buffer-ot -> numItems i da mu postavi novi promenlivi
    public void execute() throws InterruptedException {

        //mora da vovedeme sinhronizacija pri sto uslovot e: ne smee da se polni buffer-ot se dodeka ne e celosno ispraznet odnosno se dodeka ne se otvori 1 - zatvoren
        //pred da go polnime buffer-ot treba da go blokirame izvrsuvanjeto na metodot execute so toa sto go povikuvame soodvetniot semafor
        //ceka da dobie resursi odnosno da dobie producer-ot privilegii
        Locks.bufferEmpty.acquire();

        //mora da ja zastitime i promenlivata koja se naogja vo buffer-ot so toa sto voveduvame drug semafor
        //treba da dobieme privilegija za zapisuvanje na vrednost vo numItems spodelenata promenliva
        //nakratko! => povekje consumer-i se obiduvaat da pristapat do taa promenliva i so acquire() pristap ima samo eden consumer
        Locks.bufferLock.acquire();

        //otkako napravivme konstruktor za spodeluvanje na promelivata vo buffer-ot samo se povikuva toj metod => go polnime buffer-ot
        buffer.fillBuffer();

        //go polnime buffer-ot no ne treba da napravime release zatoa sto pri narednoto izvrsuvanje producer-ot kje dobie sansa pak da go polni buffer-ot
        //iako kje nema potreba za toa, celosno ili delumno e napolnet -> toj mora da polni samo koga e celosno prazen
        //duri i da zaspie eden consumer mora da go priceka da ja zeme stavkata od buffer-ot pa duri posle mozi da polni
        //Lock.bufferEmpty.release();


        //mora da go iziterirame sekoj semafor i da napravime release() za sekoj od niv
        //sekoj thread da mozi da se izvrsi i da si go zeme soodvetniot item
        for(Semaphore s:Locks.items) {
            s.release();
        }

        //ova go pravime za koga eden consumer kje zavrsi odnosno kje go zeme produktot ili kje pristapi do numItems da moze da mu go otstapi redot na drugite
        Locks.bufferLock.release();
    }

    //run metodot treba da napravi povik na execute() n pati, kolku pati sme go definirale da se izvrsi
    @Override
    public void run() {
        //go izrsuvame 150 pati
        for(int i = 0; i < Application.NUM_RUNS; i++) {
            try {
                execute();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
