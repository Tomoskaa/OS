package ispitnizadaci.prvkolokvium.sync.exam2018.kol.grupa1;

import laboratoriska3.zadaca2.Main;

import javax.xml.crypto.Data;
import java.io.*;
import java.util.ArrayList;
import java.util.InputMismatchException;
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

        //try(DataInputStream in = new DataInputStream(new InputStream(matrixFile) {
        try (BufferedReader in = new BufferedReader(new FileReader(new File(matrixFile)))) {
            // todo: The variable in should provide the readLine() method
            // InputStreamReader in = new InputStreamReader(new FileInputStream(matrixFile));
            int n = Integer.parseInt(in.readLine().trim());
            this.matrix = new int[n][n];

            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    matrix[i][j] = Integer.parseInt(in.readLine().trim());
                }
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
        //во позадина ќе ги запише вредностите на елементите од матрицата matrix во датотеката outputPath по еден елемент во секоја линија од фајлот
        try (BufferedWriter dos = new BufferedWriter(new FileWriter(outputPath))) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    // todo: write the element matrix[i][j]
                    dos.write(matrix[i][j] + " ");
                }
                dos.newLine();
                //dos.write("\n");
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
        // todo: allow maximum 15 parallel executions
        try {
            MainG1.lock.acquire();
            int n = matrix.length;
            for (int k = 0; k < n; k++) {
                result += matrix[row][k] * matrix[k][column];
            }
            MainG1.lock.release();
            MainG1.semaphore.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

//Да се имплементира класа FileScanner која ќе се однесува како thread
class FileScanner extends Thread {
    final List<File> matrixFiles = new ArrayList<>();
    final File directoryToScan;

    FileScanner(File directoryToScan) {
        this.directoryToScan = directoryToScan;
    }


    @Override
    public void run() {
        try {
            List<FileScanner> scanners = new ArrayList<>();
            //Оваа класа треба да изврши рекурзивно скенирање на именикот проследен преку својството directoryToScan
            // и треба да ги пронајде сите датотеки со екстензија .mat
            File[] files = directoryToScan.listFiles();
            for (File file : files) {
                if (file.isFile() && file.getName().endsWith(".mat")) {
                    //На крајот од скенирањето на directoryToScan треба да се почекаат резултатите од сите subDirectory
                    // и да се додадат во низата matrixFiles.
                    synchronized (matrixFiles) {
                        // todo: find *.mat files and add them in the matrixFiles list
                        matrixFiles.add(file);
                    }
                }
                if (file.isDirectory()) {
                    // todo: for each sub-directory, create a new instance of FileScanner
                    FileScanner fs = new FileScanner(file);
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
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

public class MainG1 {

    public static Semaphore lock = new Semaphore(15);
    public static Semaphore semaphore = new Semaphore(0);


    public static void main(String[] args) throws FileNotFoundException, InterruptedException {
        List<Transformer> transformers = new ArrayList<>();

        Reader reader = new Reader("data/matrix.mat");
        // todo: execute file reading in background
        reader.start();

        // todo: wait for the matrix to be read
        reader.join();

        // todo: transform the matrix
        int n = reader.matrix.length;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                Transformer t = new Transformer(reader.matrix, i, j);
                transformers.add(t);
                // todo: start the background execution
                t.start();

            }
        }


        // todo: wait for all transformers to finish
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