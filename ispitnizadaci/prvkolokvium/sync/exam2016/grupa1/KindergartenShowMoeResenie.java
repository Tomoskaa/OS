package ispitnizadaci.prvkolokvium.sync.exam2016.grupa1;

import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.concurrent.Semaphore;


public class KindergartenShowMoeResenie {

    static final int NUM_RUN = 50;

    static final int GROUP = 6;
    static final int TOTAL = 24;

    static Semaphore ucesnici;
    static Semaphore izleziNaBina;
    static Semaphore ciklus;
    static Semaphore lock;

    static int grupaNum;
    static int vkupno;


    public static void init() {
        ucesnici = new Semaphore(6);
        izleziNaBina = new Semaphore(0);
        ciklus = new Semaphore(0);
        lock = new Semaphore(0);

        grupaNum = 0;
        vkupno = 0;
    }

    public static class Child extends Thread {

        int numRuns;
        public Child(int numRuns) {
            this.numRuns = numRuns;
        }

        public void execute() throws InterruptedException {

            //ucesnicite baraat dozvola za da vleguvaat
            ucesnici.acquire();
            //go povikuvame metodot za vleguanje
            participantEnter();

            //so povikuvanje na metodot participantEnter ucesnicite izleguvaat na binata, znaci go zgolemuvame brojacot se dodeka ne se soberat 6 ucesnici
            //bidejki grupaNum e static promenliva mora da ja zastitime, najednostavna implementacija e so lock
            lock.acquire();
            grupaNum++;
            if(grupaNum == GROUP) {
                izleziNaBina.release(GROUP);
            }
            lock.release();
            //postavuvame zabrana za izleguvanje na ucesnicite na binata
            izleziNaBina.acquire();
            //naredno otkako se site ucesnici na binata go povikuvame metodot present() kade sledi prezentacija na pretstavata
            present();

            //go postavuvame tuka lock zatoa sto mora da gi zastitime dvete static promenlivi grupaNum i vkupno
            lock.acquire();

            //kako sto prezentira sekoj od ucesnicite ja namaluvame vrednosta na grupaNum se dodeka ne zavrsat 6-te ucesnici
            grupaNum--;
            //ako nema ucesnici koi treba da prezentiraat toa znaci deka grupata zavrsila so prezentacija i go povikuvame soodvetniot metod
            //i pritoa davame dozvola da se kacuvaat novi ucesnici na binata
            vkupno++;
            if(grupaNum == 0) {
                endGroup();
                ucesnici.release(GROUP);
            }
            if(vkupno == TOTAL) {
                endGroup();
                ciklus.release(TOTAL);
                vkupno = 0;
            }
            lock.release();
            //pominale site 4 ciklusi zabrani nivno povtoruvanje
            ciklus.acquire();
        }

        public void participantEnter() {
            System.out.println("Ucesnik se kacuva na bina");
        }

        public void present() {
            System.out.println("Ucesnikot zapocnuva so prezentacija");
        }

        public void endGroup() {
            System.out.println("Tekovnata grupa zavrsi so prezentiranje.");
        }

        public void endCycle() {
            System.out.println("Site grupi zavrsija so prezentiranje vo tekovniot ciklus.");
        }
    }

    public static void main(String[] args) {
        HashSet<Thread> threads = new HashSet<>();
        int numRuns = 24;
        for(int i = 0; i < NUM_RUN; i++) {
            Child c = new Child(numRuns);
            threads.add(c);
        }

        init();

        for(Thread t : threads) {
            t.start();
        }

        for(Thread t : threads) {
            try {
                t.join(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

}
