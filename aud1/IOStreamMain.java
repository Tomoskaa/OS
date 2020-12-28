package aud1;

import aud1.streams.IOStreamManager;
import aud1.streams.impl.IOStreamManagerImpl;

import java.io.File;
import java.io.IOException;

public class IOStreamMain {

    public static void main(String[] args) throws IOException {
        String filePath = "D:\\Data\\OS\\tmp\\in.txt";
        String filePathDest = "D:\\Data\\OS\\tmp\\out.txt";
        IOStreamManager manager = new IOStreamManagerImpl();
        manager.rewriteInReverseFile(new File(filePath), new File(filePathDest));
    }
}
