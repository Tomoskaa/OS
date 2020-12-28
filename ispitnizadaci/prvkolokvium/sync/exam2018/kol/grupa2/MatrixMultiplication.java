package ispitnizadaci.prvkolokvium.sync.exam2018.kol.grupa2;

import aud5.ProducerController;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;


class Reader extends Thread {
    final String matrixFile;
    int[][] matrix;

    Reader(String matrixFile) {
        this.matrixFile = matrixFile;
    }

    /**
     * This method should execute in background
     */
    @Override
    public void run() {
        // todo: complete this method according to the text description

        DataInputStream in = null;
        try (DataInputStream s = new DataInputStream(new FileInputStream(new File(matrixFile)))){
            // todo: The variable in should provide the readInt() method
            int n = in.readInt();
            this.matrix = new int[n][n];
            System.out.println("Reading matrix: ");
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    matrix[i][j] = in.readInt();
                    System.out.print(matrix[i][j] + " ");
                }
                System.out.println();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class Writer extends Thread {

    private final String outputPath;
    private final int[][] matrix;

    Writer(String outputPath, int[][] matrix) {
        this.outputPath = outputPath;
        this.matrix = matrix;
    }


    @Override
    public void run() {
        int n = matrix.length;
        try(BufferedWriter bf = new BufferedWriter(new FileWriter(outputPath))) {
            System.out.println("Writing matrix: ");
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    // todo: write the element matrix[i][j]
                    System.out.print(matrix[i][j]+" ");
                    bf.write(matrix[i][j]);
                }
                System.out.println();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class Transformer extends Thread {
    final int[][] matrix;
    final int row;
    final int column;

    int result;

    Transformer(int[][] matrix, int row, int column) {
        this.matrix = matrix;
        this.row = row;
        this.column = column;
    }

    @Override
    public void run() {
        try {
            // todo: allow maximum 10 parallel executions
            MatrixMultiplication.lock.acquire();
            int n = matrix.length;
            for (int k = 0; k < n; k++) {
                result += matrix[row][k] * matrix[k][column];
            }
            MatrixMultiplication.lock.release();
            MatrixMultiplication.semaphore.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}

class FileScanner extends Thread {

    final static List<File> matrixFiles = new ArrayList<>();
    final File directoryToScan;

    FileScanner(File directoryToScan) {
        this.directoryToScan = directoryToScan;
    }

    public void run() {
        try {
            List<FileScanner> scanners = new ArrayList<>();

            File[] files = directoryToScan.listFiles();
            // todo: find *.mat files and add them in the matrixFiles list
            for(File matFile : files) {
                if(matFile.isFile() && matFile.getName().endsWith(".mat")) {
                    synchronized (matrixFiles) {
                        matrixFiles.add(matFile);
                    }
                }
            }
            for(File subDirectory : files) {
                if(subDirectory.isDirectory()) {
                    // todo: for each sub-directory, create a new instance of FileScanner
                    FileScanner fs = new FileScanner(subDirectory);
                    scanners.add(fs);
                    //todo: invoke the scanning of the subDirectory in background
                    fs.start();
                }
            }
            for (FileScanner scanner : scanners) {
                //todo: wait for the scanner to finish
                scanner.join();

            }
            System.out.println("Done scanning: " + directoryToScan.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


public class MatrixMultiplication {

    public static Semaphore lock = new Semaphore(10);
    public static Semaphore semaphore = new Semaphore(0);

    public static void main(String[] args) throws FileNotFoundException, InterruptedException {
        List<Transformer> transformers = new ArrayList<>();

        Reader reader = new Reader("data/matrix.mat");
        // todo: execute file reading in background
        reader.start();

        // todo: wait for the matrix to be read
        reader.join();

        // todo: transform the matrix
        int n = 30;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                Transformer t = new Transformer(reader.matrix, i, j);
                transformers.add(t);
                // todo: start the background execution
                t.start();

            }
        }


        // todo: wait for all transformers to finish at once
        semaphore.acquire(n * n);

        int[][] result = new int[n][n];
        for (Transformer t : transformers) {
            result[t.row][t.column] = t.result;
        }

        Writer writer = new Writer("data/out.bin", result);
        // todo: execute file writing in background
        writer.start();

        FileScanner scanner = new FileScanner(new File("data"));
        // todo: execute file scanning in background
        scanner.start();

        // todo: wait for the scanner to finish and show the results
        scanner.join();

        for (File matrixFile : scanner.matrixFiles) {
            System.out.println(matrixFile.getAbsolutePath());
        }

    }
}