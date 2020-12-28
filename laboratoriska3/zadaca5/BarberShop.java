package laboratoriska3.zadaca5;

import java.util.HashSet;
import java.util.concurrent.Semaphore;

public class BarberShop {

    BarberShop studio;

    int waitingCustomers = 0;
    Semaphore waitingCustomersNumber = new Semaphore(1);
    Semaphore waiting = new Semaphore(5);
    Semaphore customerWaits = new Semaphore(0);
    Semaphore barberIsDone = new Semaphore(0);

    void customerComesIn() throws InterruptedException {
        // TODO: 3/29/20 Synchronize this method, invoked by a Customer thread
        waiting.acquire();
        waitingCustomersNumber.acquire();
        waitingCustomers++;
        waitingCustomersNumber.release();
        customerWaits.release();
        barberIsDone.acquire();
        waiting.release();

    }

    void barber() throws InterruptedException {
        // TODO: 3/29/20 Synchronize this method, invoked by Barber thread
        customerWaits.acquire();
        waitingCustomersNumber.acquire();
        waitingCustomers--;
        waitingCustomersNumber.release();
        barberIsDone.release();
        System.out.println("customer is served");
    }

    static class Customer extends Thread{
        BarberShop bs;
        public Customer( BarberShop bs ){
            this.bs = bs;
        }
        @Override
        public void run(){
            try {
                bs.customerComesIn();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    static class Barber extends Thread{
        BarberShop bs;
        public Barber( BarberShop bs ){
            this.bs = bs;
        }
        @Override
        public void run(){
            try {
                while ((true))
                    bs.barber();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        BarberShop studio = new BarberShop();
        Barber barber = new Barber(studio);
        barber.start();
        HashSet<Customer> customers = new HashSet<>();
        for ( int i = 0; i<10; i++ ){
            Customer c = new Customer(studio);
            customers.add(c);
        }
        for (Customer c: customers){
            c.start();
        }
    }
}

