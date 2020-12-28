package ispitOS;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class Bank {

    static Semaphore deponiranje;
    static Semaphore vadenje;
    static Semaphore smetka;
    static Semaphore lock;
    static int pari;

    public static void init() {
        deponiranje = new Semaphore(0);
        vadenje = new Semaphore(0);
        smetka = new Semaphore(0);
        lock = new Semaphore(1);
        pari = 0;
    }

    Account[] accounts = new Account[5];
    Integer total = 0;

    void addAccount(Account account) {
        this.accounts[total++] = account;
    }

    public static void main(String[] args) throws InterruptedException {
        Bank bank = new Bank();
        bank.addAccount(new Account("AAA", 100.0));
        bank.addAccount(new Account("BBB", 100.0));
        bank.addAccount(new Account("CCC", 100.0));
        bank.addAccount(new Account("DDD", 100.0));
        bank.addAccount(new Account("EEE", 100.0));

        init();

        for(Thread t : bank) {
            t.start();
        }

        for(Thread t : bank) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        for(Thread t : bank) {
            if(t.isAlive()) {
                t.interrupt();
                System.out.println("Possible deadlock");
            }
        }
        System.out.println("Finalna sostojba na site smetki:");
        System.out.println("Celosna istorija na site deponiranja i vadenja:");

        for (int i = 0; i < 1000; ++i) {
            // треба да се извршуваат паралелно овие акции
            new Deposit(bank.accounts[i % 5], 1.0);
            new Withdraw(bank.accounts[i % 5], 0.5);
        }

        for (int i = 0; i < bank.total; ++i) {
            System.out.println(bank.accounts[i]);
        }
        System.out.println(Deposit.history);
        System.out.println(Withdraw.history);
    }


    static class Account extends Thread {
        public String name;
        public Double balance;

        public Account(String name, Double balance) {
            this.name = name;
            this.balance = balance;
        }

        void deposit(Double amount) throws InterruptedException {
            Thread.sleep(new Random().nextInt(1000));
            this.balance += amount;
        }

        void withdraw(Double amount) throws InterruptedException {
            Thread.sleep(new Random().nextInt(1000));
            this.balance -= amount;
        }

        @Override
        public String toString() {
            return this.name + " -> " + this.balance;
        }
    }

    static class Deposit extends Thread {
        final static List<String> history = new LinkedList<>();
        Account account;
        Double amount;

        public Deposit(Account account, Double amount) {
            this.account = account;
            this.amount = amount;
        }

        public void doDeposit() {
            this.account.deposit(amount);
            history.add("+" + this.amount);
        }

        public void execute() throws InterruptedException {
            deponiranje.acquire();
            lock.acquire();
            pari++;
            if(pari == amount) {
                vadenje.release();
                smetka.release();
            }
            lock.release();
            smetka.acquire();
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

    static class Withdraw extends Thread {
        final static List<String> history = new LinkedList<>();
        Account account;
        Double amount;

        public Withdraw(Account account, Double amount) {
            this.account = account;
            this.amount = amount;
        }

        public void doWithdraw() {
            this.account.withdraw(amount);
            history.add("-" + this.amount);
        }

        public void execute() throws InterruptedException {
            vadenje.acquire();
            lock.acquire();
            pari--;
            if(pari == 0) {
                deponiranje.release();
                smetka.release();
            }
            lock.release();
            smetka.acquire();
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