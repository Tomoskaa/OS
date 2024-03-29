########ZADACA 1 FILE SCANNER


public static void main(String[] args) throw IOException {
    int symbol, lineNum = 0;
    BufferedReader reader = new BufferedReader(new FileReader("./test.txt"));
    RandomAccessFile raf = new RandomAccessFile(new File("./test.txt"), "rw");

    //Printanje na 2-ot red
    while((symbol = reader.readLine()) != null) {
        if ((char)symbol == '\n')
            lineNum++;

        if (lineNum == 1 && (char)symbol != '\n')
            System.out.print((char)symbol);
    }

    //Prevrtuvanje i zapishuvanje na 2-ot red
    raf.length(lineNum);
    String reverse = new StringBuilder(raf._).reverse().toString();
    raf.seek(lineNum);
    raf.writeBytes(reverse);

    reader.close();
    raf.close();
}


##################ZADACA 2 


public static void main(String[] args) throw IOException {
    int symbol, lineNum = 0;
    BufferedReader reader = new BufferedReader(new FileReader("./test.txt"));
    RandomAccessFile raf = new RandomAccessFile(new File("./test.txt"), "rw");

    //Printanje na 2-ot red
    while((symbol = reader.readLine()) != null) {
        if ((char)symbol == '\n')
            lineNum++;

        if (lineNum == 1 && (char)symbol != '\n')
            System.out.print((char)symbol);
    }

    //Prevrtuvanje i zapishuvanje na 2-ot red
    raf.length(lineNum);
    String reverse = new StringBuilder(raf._).reverse().toString();
    raf.seek(lineNum);
    raf.writeBytes(reverse);

    reader.close();
    raf.close();
}


######################ZADACA 3 THREAD

package kol2020;

import static java.lang.System.out;

class ShareResource {
    private int counter;

    public ShareResource() {
        this.counter = 0;
    }

    public void increment() {
        this.counter++;
    }

    public int getCounter() {
        return counter;
    }
}

class CustomRunnable implements Runnable {
    private ShareResource resource;
    public CustomRunnable(ShareResource resource) {
        this.resource = resource;
    }

    @Override
    public void run() {
        for(int i = 0; i < 3; i++) {
            this.resource.increment();
            out.println("R");
        }
    }
}

class CustomThread extends Thread {
    private ShareResource resource;
    public CustomThread (ShareResource resource) {
        this.resource = resource;
    }

    @Override
    public void run() {
        for(int i = 0; i < 3; i++) {
            this.resource.increment();
            out.println("T");
        }
    }
}

public class ThreadExample {
    public static void main(String[] args) throws InterruptedException {
        ShareResource resource = new ShareResource();
        Thread t1 = new CustomThread(resource);
        Thread t2 = new Thread(new CustomRunnable(resource));
        Thread t3 = new CustomThread(resource);
        t1.start();
        t2.start();
        t3.start();
        resource.increment();
        int finalCounter = resource.getCounter();
        t3.join();
        resource.increment();


        System.out.println("Vrednosta na final counter e: " + finalCounter);
    }
}

/*
1. Колку нитки беа активни вклучувајќи го и маин? => Активни се 9(10? во маин нема што да принта/повика) нитки
2. Што ќе биде испечатено? => При секое наредно извршување ќе имаме различен резултат, така што излезот не може да се предвиди
3. Вредноста на final counter е? => 1. Вредноста ќе биде 1 бидејќи пред иницијализацијата на final counter, resource.increment() се повикува само еднаш.
    Ако имавме пред  int finalCounter = resource.getCounter(); повикување на функцијата resource.increment(); 2 пати тогаш вредноста ќе беше 2.
    Исто така, се гледа само колку пати е повкан resource.increment(); ама пред иницијализацијата на final count. Ако биде повикан после него нема да има
    никакво влијание врз вредноста.
4. Првите 3 испечатени букви секогаш ќе бидат "TTT"? => Не, при различни извршваења на програмата може да се добивааат различни решенија
5. "run()" на "t2" секогаш ќе заврши пред "run()" на "t3":? => Да
6. "main" секогаш ќе заврши последен? => Да
7. Кога ќе заврши "run()" на "t1"? =>
8. Можно е да се случи да има "deadlock"? => Да, бидејќи имаме една иста променлива resource која е споределена а при тоа не е заштитена.
 */
