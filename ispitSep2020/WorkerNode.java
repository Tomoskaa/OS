

package ispitSep2020;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DistributedProd2 {
    public static void main(String[] args) {

        // dadena e ovaa niza prvo treba da gi podelime broevite po 4 vo niza t.e 4 workerNodes i da gi pomnozime soodvetno tie broevi i na kraj da
        // se presmeta nivnata suma  da se podeli so 5
        int[] numbers = {1, 2, 3, 4, 5, 6, 7, 8, 9, 1, 2, 3, 4, 5, 6, 7, 8, 9, 1, 2};

        // TODO: 8/30/20 Start a Core Node in a new Thread
        CoreNode coreNode = new Thread();
        coreNode.start();
    }

    // TODO: 8/30/20 Worker Node must be a Thread
    class WorkerNode extends Thread {

        private int[] array;
        private int[] sharedResults;
        private int workerIndex;

        public WorkerNode(int arr[], int[] sharedResults, int workerIndex) {
            this.array = arr;
            this.sharedResults = sharedResults;
            this.workerIndex = workerIndex;
        }

        public void run() {
            int prod = 0;
            // TODO: 8/30/20 Compute product of elements in array
            for (int i = 0; i < 4; i++) {
                for (int j = i; j < array; j++) {
                    prod *= array[i];
                }
            }

            // Write result to shared memory
            sharedResults[workerIndex] = prod;
        }
    }


    // TODO: 8/30/20 Core Node must be a Thread
    class CoreNode extends Thread {

        private int[] array;

        public CoreNode(int arr[]) {
            this.array = arr;
        }

        public void run() throws InterruptedException {
            int[] sharedIntermediateResults = {0, 0, 0, 0};
            List<WorkerNode> workerNodeList = new ArrayList();

            for (int i = 0, workerIndex = 0; i < array.length; i += 5, workerIndex++) {
                int[] slice = Arrays.copyOfRange(array, i, i + 5);
                WorkerNode workerNode = new WorkerNode(slice, sharedIntermediateResults, workerIndex);
                // TODO: 8/30/20 Run worker node in a separate thread
                workerNode.start();
            }

            // TODO: 8/30/20 Wait for all worker nodes to finish
            for(Thread t : workerNodeList) {
                try {
                    t.join(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            // TODO: 8/30/20 Compute and print product

            // TODO: 8/30/20 Compute and print remainder of division with 5
            System.out.println()
        }

        public int getProd(int[] array) {
            int totalProd = 1;
            for (int sharedResult : array) {
                totalProd *= sharedResult;
            }
            return totalProd;
        }

    }

}