/*package laboratoriska2.zadaca3;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class BlockingQueue<T> {

    List<T> contents = new LinkedList<T>();
    //T[] contents;
    int capacity;

    private Semaphore empty;
    private Semaphore full;
    private Semaphore mutex;

    public BlockingQueue(int capacity) {
        contents = (T[]) new Object[capacity];
        this.capacity = capacity;
        this.empty = new Semaphore(capacity);
        this.full = new Semaphore(0);
        this.mutex = new Semaphore(1);
    }

    public synchronized void enqueue(T item) throws InterruptedException {
        empty.acquire();
        mutex.acquire();
        contents.add(item);
        mutex.release();
        full.release();
    }

    public synchronized T dequeue(int item) throws InterruptedException {
        full.acquire();
        mutex.acquire();
        T item = contents.remove(0);
        mutex.release();
        empty.release();
        return item;
    }
}

 */