 /* import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// TODO: 8/30/20 Worker Node must be a Thread
class WorkerNode extends Thread {

    private int [] array;
    private int [] sharedResults;
    private int workerIndex;

    public WorkerNode(int arr[], int[] sharedResults, int workerIndex) {
        this.array = arr;
        this.sharedResults = sharedResults;
        this.workerIndex = workerIndex;
    }

    public void run() {
        int prod = 0;
        // TODO: 8/30/20 Compute product of elements in array
        for(int i = 0; i < 4; i++) {
            for(int j = i; j < array; j++) {
                prod *= array[i];
            }
        }


        // Write result to shared memory
        sharedResults[workerIndex] = prod;
    }
}


// TODO: 8/30/20 Core Node must be a Thread
class CoreNode extends Thread {

    private int [] array;

    public CoreNode(int arr[]) {
        this.array = arr;
    }

    public void run() throws InterruptedException {
        int [] sharedIntermediateResults = {0, 0, 0, 0};
        List<WorkerNode> workerNodeList = new ArrayList();

        for(int i = 0, workerIndex=0; i < array.length; i += 5, workerIndex++) {
            int[] slice = Arrays.copyOfRange(array, i, i + 5);
            WorkerNode workerNode = new WorkerNode(slice, sharedIntermediateResults, workerIndex);
            // TODO: 8/30/20 Run worker node in a separate thread
            workerNode.start();
        }

        // TODO: 8/30/20 Wait for all worker nodes to finish
        workerNode.join();

        // TODO: 8/30/20 Compute and print product
        // TODO: 8/30/20 Compute and print remainder of division with 5
    }

    public int getProd(int[] array) {
        int totalProd = 1;
        for (int sharedResult : array) {
            totalProd *= sharedResult;
        }
        return totalProd;
    }

}

public class DistributedProd2 {

    public static void main(String[] args) {

        // dadena e ovaa niza prvo treba da gi podelime broevite po 4 vo niza t.e 4 workerNodes i da gi pomnozime soodvetno tie broevi i na kraj da
        // se presmeta nivnata suma  da se podeli so 5
        int [] numbers = {1, 2, 3, 4, 5, 6, 7, 8, 9, 1, 2, 3, 4, 5, 6, 7, 8, 9, 1, 2};

        // TODO: 8/30/20 Start a Core Node in a new Thread
        CoreNode coreNode = new Thread();
        coreNode.start();



    }
}

// zadaca 1:
// Dadeno bese byte[](int file, int off, int len) od klasata InputStream prasanjeto e:
// Go dava brojot na bajti tocno kolku sto e len => ova e tocnoto
// Go dava brojot na bajti tocno kolku sto e off
// Go dava brojot na bajti pomalku ili endakvo na len
// Go dava brojot na bajti poekjve ili endakvo na off

// Zadaca 3:
// Da se odredat unikatni adresi na finki koi sto se prebaruvale izminatiov period pri sto da se zaokruzi onaj odgovor koj sto
// ne gi sodrzi adresite na Finki, a adresata na finki e 10.10.X.Y
// ponudeni povekje bea: last -l | print{$3} | awk 10.\10.\... | sort uniq | wc -l
// isto vaka samo so who napred who -l | print{$3} | awk 10.\10.\... | sort uniq | wc -l


// zadaca 4:
// da se napise shell skripta passed_students.sh koja prima datoteki so ekstenzija .csv
// dokolku brojot na argumenti e 0 ili poglem od 1  ili dokumentot ne e so ekstenzija .csv togas da se ispise soodvetna poraka
// dadeni se vo skripta ime, prezime, indeks, broj na poeni za studenti bea nabrojani okolu 7
// treba da se preimenuva datotekata od passed_csv_file -> grades_csv_file kade vo novata datoteka gi stavame site onie
// studenti koi imaa barem 5 poeni



  */