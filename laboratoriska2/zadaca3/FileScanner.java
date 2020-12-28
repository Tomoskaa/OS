package laboratoriska2.zadaca3;

import java.io.File;

public class FileScanner extends Thread {

    private String fileToScan;
    //TODO: Initialize the start value of the counter
    private static Long counter = 0L;

    public FileScanner (String fileToScan) {
        this.fileToScan=fileToScan;
        //TODO: Increment the counter on every creation of FileScanner object
        synchronized (this) {
            counter++;
        }
    }

    public static void printInfo(File file)  {

        /*
         * TODO: Print the info for the @argument File file, according to the requirement of the task
         * */
        if(file.isDirectory()) {
            System.out.printf("dir: %s - solutions %d\n", file.getAbsolutePath(), getDirectorySize(file));
        }
        else if(file.isFile()) {
            System.out.printf("file: %s %d\n", file.getAbsolutePath(), file.length());
        }
        else {
            System.err.println("ERROR");
        }
    }

    public static long getDirectorySize(File f) {
        if(!f.isDirectory())
            return 0;

        long length = 0;

        for(File file : f.listFiles()) {
            if(file.isFile())
                length += file.length();
            else if(file.isDirectory())
                length += getDirectorySize(file);
        }
        return length;
    }

    public static Long getCounter () {
        return counter;
    }


    public void run() {

        //TODO Create object File with the absolute path fileToScan.
        File file = new File(fileToScan);

        //TODO Create a list of all the files that are in the directory file.
        File[] files = file.listFiles();


        for (File f : files) {

            /*
             * TODO If the File f is not a directory, print its info using the function printInfo(f)
             * */

            if (!f.isDirectory())
                printInfo(f);

            /*
             * TODO If the File f is a directory, create a thread from type FileScanner and start it.
             * */

            if (f.isDirectory()) {
                FileScanner newDirectoryThread = new FileScanner(f.getAbsolutePath());
                newDirectoryThread.start();

                //TODO: wait for all the FileScanner-s to finish
                try {
                    newDirectoryThread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main (String [] args) {
        String FILE_TO_SCAN = "C:\\Users\\175018\\Desktop\\lab";

        //TODO Construct a FileScanner object with the fileToScan = FILE_TO_SCAN
        FileScanner fileScanner = new FileScanner(FILE_TO_SCAN);

        //TODO Start the thread from type FileScanner
        fileScanner.start();

        //TODO wait for the fileScanner to finish
        try {
            fileScanner.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //TODO print a message that displays the number of thread that were created
        System.out.println(getCounter());

    }
}
