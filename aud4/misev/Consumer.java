package aud4.misev;


//Sto pravi consumer-ot? :) ☻☺☻☺☻☺☻☺☻☺
//zema odredena stavka i stom ja zema go namaluva brojot na item-i koi sto ostanale, pravi proverka dali buffer-ot e prazen
//ako e prazen togas mora da go izvesti producer-ot
public class Consumer extends Thread {

    private Buffer buffer;
    private int id;

    //treba da pristapi do spodelenata promenliva numItems koja se naogja vo buffer-ot toa se pravi so kreiranje na konstruktor
    public Consumer(int id, Buffer buffer) {
        this.buffer = buffer;
        this.id = id;
    }

    public void execute() throws InterruptedException {

        //pred da zeme consumer-ot soodvetna stavka od slotot toj mora da ceka na semaforot koj sto mu go kontrolira pristapot
        Locks.items[id].acquire();


        //getItem() bara nekoe id soodvetno na consumer odnosno kolku consumer-i ima vo ramki na zadacata i niv kje gi instancirame(definirame) isto taka vo konstruktorot
        buffer.getItem(id);


        //sekoj consumer mora da go ceka ovoj semafor so cel da napravi notifikacija na spodeleniot resurs -> numItems
        //semaforot koj go kontrolira ovoj pristap e zaednicki i za producer-ot i za consumer-ot
        Locks.bufferLock.acquire();     //cekanje da se dobie pristap, odnosno do menuvanje


        //so ovie prethodno definirani semafori se zastitiva kodot nadolu odnosno samo eden thread kje moze da pristapuva vo odredeno vreme
        //i da pristapuva do numItems

        //go odreduva brojot na stavki ostanati vo buffer-ot
        buffer.decrementNumberOfItemsLeft();

        //pravi proverka dali buffer-ot e prazen
        if(buffer.isBufferEmpty()) {
            //ako e prazen treba da go izvesti producer-ot za da go napolni povtorno -> toa se pravi so pomos na semafori najzeznat del
            Locks.bufferEmpty.release();
        }

        //bidejki imame acquire() i otkako kje zavrsime so zapisuvanje i iscituvanje na vrednosta na odredena promenliva istata treba da ja oslobodime za da moze
        //druga promenliva da pristapuva do nea
        Locks.bufferLock.release();
    }

    @Override
    public void run() {
        for(int i = 0; i < Application.NUM_RUNS; i++) {
            try {
                execute();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
