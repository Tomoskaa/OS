/*package aud5;

import java.net.Inet4Address;
import java.util.Objects;
import java.util.concurrent.Semaphore;

public class SyncToilet {

    static Semaphore toiletSem;
    static Semaphore mLock;
    static Semaphore zLock;

    static int numM;
    static int numZ;

    public static class Toilet {

        public void vlezi() {
            System.out.println("Vleguva...");
        }

        public void izlezi() {
            System.out.println("Izleguva...");
        }
    }

    public static void init() {
        toiletSem = new Semaphore(1);
        mLock = new Semaphore(1);
        zLock = new Semaphore(1);

        numM = 0;
        numZ = 0;
    }

    public static class Man extends Thread {

        private Toilet toilet;

        public Man(Toilet toilet) {
            this.toilet = toilet;
        }

        public void enter() throws InterruptedException {
            //so mLock ja zastituvame promenlivata numM
            mLock.acquire();
            if(numM == 0) {
                toiletSem.acquire();
            }
            //stom vleguva vo toaletot se zgolemuva vrednosta na numM i so toa ako ima mazi koi cekaat vo redicata kje znaat deka
            //kj moze da vleguvaat ako nema pred niv masko
            numM++;
            //bidejku vlezi i izlezi ne se atomicni toa znaci deka treba i niv da gi zastitime pa vlezi() go stavame pred release inaku kje bese nadvor
            this.toilet.vlezi();
            mLock.release();
        }

        public void exit() {
            //bidejki imame i read i write operacija za numM treba da ja zastitime
            //no metodot izlezi() ne e atomicen sto znaci deka treba da ja zastitime i zatoa go stavame pod mLock.acquire()
            mLock.acquire();
            //prvo sto pravime go povikuvame metodot izlezi()
            this.toilet.izlezi();
            //stom maz izlezi od toalet treba da ja namalime vrednosta na numM
            numM--;
            //ako nema mazi koi cekaat vo redicata togas treba da gi izvestime zenskite deka moze da vleguvaat
            if(numM == 0) {
                toiletSem.release();
            }
            mLock.release();
        }

        @Override
        public void run() {
            super.run();
        }
    }

    public static class Woman extends Thread {

        private Toilet toilet;

        public Woman(Toilet toilet) {
            this.toilet = toilet;
        }

        public void enter() throws InterruptedException {
            zLock.acquire();
            if(numZ == 0) {
                toiletSem.acquire();
            }
            numZ++;
            //go povikuvame metodot vlezi() otkako kje dobieme dozvola da vlezeme ne mozi prethodno da se povika PAZI !!!!!
            this.toilet.vlezi();
            zLock.release();
        }

        public void exit() {
            zLock.acquire();
            //prvo go povikuvame metodot izlezi()
            this.toilet.izlezi();
            if(numZ == 0) {
                toiletSem.release();
            }
            numZ--;
            zLock.release();
        }

        @Override
        public void run() {
            super.run();
        }

    }
}

 */
