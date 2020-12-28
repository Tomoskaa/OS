/*package aud5;

import java.util.concurrent.Semaphore;

public class UpisFinki {

    static Semaphore slobodnoUpisnoMesto;
    static Semaphore enter;
    static Semaphore here;
    static Semaphore done;

    public static void init() {
        slobodnoUpisnoMesto = new Semaphore(4);
        enter = new Semaphore(0);   //nikoj ne smee da vleguva zatoa stavame 0(zabrana)
        here = new Semaphore(0);
        done = new Semaphore(0);
    }

    public static class Clen extends Thread {

        public void execute() throws InterruptedException {
            slobodnoUpisnoMesto.acquire();
            int i = 10;  //broj na studenti koi clenot treba da gi zapisi

            //ako e pogolemo od 0 zapisuva studenti
            while(i > 0) {

                //TODO: zapisuvaj novi studenti

                enter.release();
                here.acquire();
                zapisi();
                done.release();

                //kako sto kje zapisuva studenti otstranuvaj gi od listata na cekanje
                i--;    //nema potreba da se zastituva ovaa promenliva bidejki ne e static i nikoj nema da mozi da ja menuva
            }
            slobodnoUpisnoMesto.release();
        }

        public void zapisi() {
            System.out.println("Zapisuva studenti...");
        }

        @Override
        public void run() {
            try {
                execute();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static class Student extends Thread {

        public void execute() throws InterruptedException {
            enter.acquire();
            ostaviDokumenti();
            here.release();
            done.acquire();
        }

        public void ostaviDokumenti() {
            System.out.println("Ostava dokumenti...");
        }

        @Override
        public void run() {
            try {
                execute();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}


 */