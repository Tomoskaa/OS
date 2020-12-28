/*
package laboratoriska2.zadaca1;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class TwoThreads {

    // Thread1 & Thread2 se dadeni, no ne ni trebaat
      /*public static class Thread1 extends Thread {
        public void run() {
            System.out.println("A");
            System.out.println("B");
        }
    }

    public static class Thread2 extends Thread {
        public void run() {
            System.out.println("1");
            System.out.println("2");
        }
    }

    public static class ThreadAB implements Runnable{

        String eden;
        String dva;

        public ThreadAB(String a, String b){
            this.eden = a;
            this.dva = b;
        }

        @Override
        public void run() {
            System.out.println("Print: " + eden + " " + dva);
        }
    }


    public class Thread12 implements Runnable{

        String eden;
        String dva;

        public Thread12(String a, String b){
            this.eden = a;
            this.dva = b;
        }

        @Override
        public void run() {
            System.out.println("Print: " + eden + " " + dva);
        }
    }

    public static void main(String[] args) throws IOException {

        System.out.println("Vnesi dva stringa:");
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String line = in.readLine();
        String pom[] = line.split(" ");

        ThreadAB thread = new ThreadAB(pom[0], pom[1]);

        new Thread(thread).start();

    }

}

 */
