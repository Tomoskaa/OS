package laboratoriska2.zadaca1;

/*public class TwoThreads {
    public static class Thread1 extends Thread {
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

    public static void main(String[] args) {
        new Thread1().start();
        new Thread2().start();
    }

}*/

public class TwoThreads {

    public static class ThreadAB implements Runnable {

        private String string1;
        private String string2;

        public ThreadAB(String string1, String string2) {
            this.string1 = string1;
            this.string2 = string2;
        }

        public void run() {
            System.out.println(string1);
            System.out.println(string2);
        }
    }

    public static void main(String[] args) {
        ThreadAB thread1 = new ThreadAB("A", "B");
        Thread t1 = new Thread(thread1);
        ThreadAB thread2 = new ThreadAB("1", "2");
        Thread t2 = new Thread(thread2);

        t1.start();
        t2.start();
    }
}

//Излезот од програмата не може да се предвиди бидејќи при секое нејзино извршување може добиваме различен резултат