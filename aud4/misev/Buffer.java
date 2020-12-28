package aud4.misev;

public class Buffer {

    //broj na slotovi
    private int numItems;

    //broj na consumer-i
    private int numConsumers;

    //treba vrednosta na numItems da ja odredime, odnosno kolku consumer-i imame i toa go pravime so kreiranje na kosnturktor so argumenti
    public Buffer(int numConsumers) {
        this.numConsumers = numConsumers;
    }



    public void fillBuffer() {
        if(numItems != 0) {
            //kje frlime isklucok ako nesto ne e kako sto treba odnosno baferot treba da se polni samo koga kje bide celosno prazen
            throw new RuntimeException("Buffer is not empty!");
        }
        //ako brojot na numItems e 0 toa znaci deka moze da gi polnime slotovite na consumer-ite
        System.out.println("Filling buffer...");

        //so sto kje se polni buffer-ot, odnosno toa kazuva kolku produkti se ostanati (kolku slotovi imame tolku consumer-i)
        numItems = numConsumers;
    }

    //celta na ovoj metod e stom se zeme stavka od buffer-ot treba vrednosta na numItems da se namali za 1
    public void decrementNumberOfItemsLeft() {
        numItems--;
    }

    //pravi proverka dali buffer-ot e prazen
    public boolean isBufferEmpty() {
        //brojot na stavki vo buffer-ot treba da e 0
        return numItems == 0;
    }

    //id -to ni kazuva od koj slot kje zemame soodveten produkt
    public void getItem(int id) {
        //ne treba tuka nisto da se implementira tuku samo da se ispecati od koj slot se zema produktot
        System.out.println(String.format("Get item by id ", id));
    }
}

