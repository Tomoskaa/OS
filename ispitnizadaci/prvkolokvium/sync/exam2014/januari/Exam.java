package ispitnizadaci.prvkolokvium.sync.exam2014.januari;

import ispitnizadaci.prvkolokvium.sync.klasi.AbstractState;
import ispitnizadaci.prvkolokvium.sync.klasi.ProblemExecution;

import java.util.Date;
import java.util.HashSet;
import java.util.concurrent.Semaphore;
import java.util.function.IntToDoubleFunction;

public class Exam {

    static Semaphore profesorVlezi;
    static Semaphore profesorIzlezi;
    static Semaphore studentVlezi;
    static Semaphore studentIzlezi;
    static Semaphore zapocniIspit;
    static Semaphore lock;
    static int numStudents;
    static int vkupo;

    public static void init() {
        profesorVlezi = new Semaphore(1);
        profesorIzlezi = new Semaphore(0);
        studentVlezi = new Semaphore(0);
        studentIzlezi = new Semaphore(0);
        zapocniIspit = new Semaphore(0);
        lock = new Semaphore(1);
        numStudents = 0;
        vkupo = 0;
    }

    public static class Profesor extends Thread {

        public Profesor() {
        }

        public void execute() throws InterruptedException {

            //kaj profesor gi implementerime onie metodi koi ni se baraat i pritoa samo gi zapazuvame metodite definirani za studentite

            //davam dozvola da vleze prvo profesor, ne smee da ima vleguvaat studenti ako nema profesor
            profesorVlezi.acquire();
            //go povikuvam soodvetniot metod
            teacherEnter();
            //ovdeka samo gi definiram studentite deka vlegle
            studentVlezi.release(50);

            //ova e zaednicka promenliva do koja pristapuvaat i profesorot i studentite
            zapocniIspit.acquire();

            //gi povikuvam 2-ta metodi definirani kaj profesorot

            //seto ova go zastituvam so lock semafor bidejki se spodeleni od povekje studenti
            lock.acquire();
            distributeTests();
            examEnd();
            //otkako kje zavsat so ipitot studentite gi osloboduvam
            studentIzlezi.release(50);
            lock.release();

            //davam dozvola da izlezi profesorot
            profesorIzlezi.acquire();
            lock.acquire();
            //go povikuvam soodvetniot metod
            teacherLeave();
            //bidejki imame povekje ciklusi kade moze da vleguvaat profesori pravam release na profesorot koj vlegol za da moze da vleguva drug
            profesorVlezi.release();
            lock.release();
        }

        public void teacherEnter() {
            System.out.println("Teacher entering in classroom.");
        }

        public void studentEnter() {
            System.out.println("Student entering classroom.");
        }

        public void distributeTests() {
            System.out.println("Starting the exam. Distributing the tests.");
        }

        public void examEnd() {
            System.out.println("Student end the exam.");
        }

        public void studentLeave() {
            System.out.println("The exam is not ended when the student leaves the classroom.");
        }

        public void teacherLeave() {
            System.out.println("There are students in the room in the moment when the teacher.");
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

        int s;
        public Student(int s) {
            this.s = s;
        }

        public void execute() throws InterruptedException {

            //davam dozvola da vleguvaat studenti
            studentVlezi.acquire();
            //go povikuvam soodvetniot metod
            studentEnter();
            //numStudents mi e static promenliva pa mora da ja zastitam
            lock.acquire();
            numStudents++;
            if(numStudents == 50) {
                //numStudents = 0;
                zapocniIspit.release();
            }
            lock.release();

            //davam dozvola da izleguvaat studentite
            studentIzlezi.acquire();
            lock.acquire();
            //go povikuvam soodvetniot metod
            studentLeave();
            //lock.acquire();
            numStudents--;
            vkupo++;
            if(vkupo == 0) {
                vkupo = 0;  //otkako kje izlezat site studenti toga moze da izlezi i profesorot
                profesorIzlezi.release();
            }
            lock.release();

        }

        public void teacherEnter() {
            System.out.println("Teacher entering in classroom.");
        }

        public void studentEnter() {
            System.out.println("Student entering classroom.");
        }

        public void distributeTests() {
            System.out.println("Starting the exam. Distributing the tests.");
        }

        public void examEnd() {
            System.out.println("Student end the exam.");
        }

        public void studentLeave() {
            System.out.println("The exam is not ended when the student leaves the classroom.");
        }

        public void teacherLeave() {
            System.out.println("There are students in the room in the moment when the teacher.");
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

    public static void main(String[] args) {
        HashSet<Thread> threads = new HashSet<>();
        for(int i = 0; i < 10; i++) {
            Student s = new Student(i);
            threads.add(s);
        }
        Profesor p = new Profesor();
        threads.add(p);

        init();

        for(Thread t : threads) {
            t.start();
        }

        for(Thread t : threads) {
            try {
                t.join(1000);
            } catch (InterruptedException e){
                e.printStackTrace();
            }
        }

        for(Thread t : threads) {
            if(t.isAlive()) {
                t.interrupt();
                System.out.println("Possible deadlock");
            }
        }
        System.out.println("Finish.");
    }
}
