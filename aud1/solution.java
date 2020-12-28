package aud1;

import java.io.*;

public class solution {

    // Driver Code
    public static void main(String args[])
    {

        // Get the file to be executed
        File f = new File("D:\\program.txt");

        // Check if this file
        // can be executed or not
        // using canExecute() method
        if (f.canExecute()) {

            // The file is can be executed
            // as true is returned
            System.out.println("Executable");
        }
        else {

            // The file is cannot be executed
            // as false is returned
            System.out.println("Non Executable");
        }
    }
}