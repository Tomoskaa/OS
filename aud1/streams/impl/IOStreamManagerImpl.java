package aud1.streams.impl;

import aud1.streams.IOStreamManager;

import java.io.*;

public class IOStreamManagerImpl implements IOStreamManager {

    @Override
    public void copyFileByteByByte(File from, File to) throws IOException {
        InputStream fis = null;
        OutputStream fos = null;

        try {
            fis = new FileInputStream(from);
            fos = new FileOutputStream(to);

            int c = -1;

            while ((c=fis.read())!=-1) {
                fos.write(c);
                fos.flush();
            }

        } finally {
            fis.close();
            fos.close();
        }
    }

    @Override
    public void copyFileByUsingBuffer(String from, String to) throws IOException {

        BufferedReader reader = null;
        BufferedWriter writer = null;


        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(from)));
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(to)));
            String line = null;
            while ((line=reader.readLine())!=null) {
                writer.write(line);
            }
        } finally {
            if (reader!=null) {
                reader.close();
            }
            if (writer!=null) {
                writer.flush();
                writer.close();
            }
        }
    }

    @Override
    public void printContentOfTxtFile(File f, PrintStream printer) throws IOException {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(f));
            String line = null;
            while ((line=reader.readLine())!=null) {
                printer.println(line);
            }

        } finally {
            if (reader!=null) {
                reader.close();
            }
        }

    }

    @Override
    public void readContentFromStdInput(OutputStream to) throws IOException {
        BufferedReader reader = null;
        BufferedWriter writer = null;

        try {
            reader = new BufferedReader(new InputStreamReader(System.in));
            writer = new BufferedWriter(new OutputStreamWriter(to));
            String line = null;
            while ((line = reader.readLine())!=null) {
                writer.write(line);
                writer.newLine();
            }
        } finally {
            if (writer!=null) {
                writer.flush();
                writer.close();
            }
        }
    }

    @Override
    public void writeToTextFile(File to, String text, Boolean append) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(to,append));
    }

    @Override
    public void memoryUnsafeTextFileCopy(File from, File to) throws FileNotFoundException, IOException {
        BufferedReader reader = null;
        BufferedWriter writer = null;

        StringBuilder fileContent = new StringBuilder();

        try {
            reader =  new BufferedReader(new FileReader(from));
            String line = null;
            while ((line=reader.readLine())!=null) {
                fileContent.append(line).append("\n");
            }
            writer.write(fileContent.toString());
        } catch (IOException e) {
            throw e;
        } finally {
            if (reader!=null) {
                reader.close();
            }
            if (writer!=null) {
                writer.flush();
                writer.close();
            }
        }
    }

    @Override
    public void memorySafeTextFileCopy(File from, File to) throws FileNotFoundException,IOException{
        BufferedReader reader = null;
        BufferedWriter writer = null;

        try {
            reader =  new BufferedReader(new FileReader(from));
            String line = null;
            while ((line=reader.readLine())!=null) {
                writer.write(line);
            }
        } catch (IOException e) {
            throw e;
        } finally {
            if (reader!=null) {
                reader.close();
            }
            if (writer!=null) {
                writer.flush();
                writer.close();
            }
        }
    }

    @Override
    public void readFileWithLineNumber(File from, OutputStream out) throws IOException {
        BufferedReader reader = null;
        PrintWriter writer = null;
        try {
            reader = new BufferedReader(new FileReader(from));
            writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(out)));
            int lineCount = 1;
            String line = null;
            while ((line = reader.readLine())!=null) {
                writer.println(String.format("%d:%s",lineCount++,line));
            }
        } finally {
            if (reader!=null)
                reader.close();
            if (writer!=null) {
                writer.flush();
                writer.close();
            }
        }
    }

    @Override
    public void writeBinaryDataToBFile(File to, Object... objects) throws IOException {
        DataOutputStream dos = null;

        try {
            dos = new DataOutputStream(new FileOutputStream(to));
            for (Object obj:objects) {
                if (obj instanceof String) {
                    dos.writeUTF((String)obj);
                } else if (obj instanceof Integer) {
                    dos.writeInt((Integer)obj);
                } else if (obj instanceof Double) {
                    dos.writeDouble((Double)obj);
                }
            }
        } finally {
            if (dos!=null ){
                dos.flush();
                dos.close();
            }
        }
    }

    @Override
    public void readBinaryDataFromBFile(File from, Object... objects) throws FileNotFoundException, IOException {
        DataInputStream dis = null;
        try {
            dis = new DataInputStream(new FileInputStream(from));
            for (Object o :objects) {
                if (o instanceof String) {
                    o = dis.readUTF();
                }
                if (o instanceof Double) {
                    o = dis.readDouble();
                }
                if (o instanceof Integer) {
                    o = dis.readInt();
                }
            }
        } finally {
            if (dis!=null) {
                dis.close();
            }
        }
    }

    @Override
    public void writeToRandomAccessFile(File from) throws IOException {
        RandomAccessFile raf = null;
        try {
            raf = new RandomAccessFile(from,"rw");
            for (int i=0;i<7;i++) {
                raf.writeDouble(i*1.414);
            }
            raf.writeUTF("THE END");
        } finally {
            if (raf!=null) {
                raf.close();
            }
        }
    }

    @Override
    public void readFromRandomAccessFile(File from, PrintStream out) throws IOException {
        RandomAccessFile raf = null;
        try {
            raf = new RandomAccessFile(from,"r");
            for (int i=0;i<7;i++) {
                out.println(raf.readDouble());
            }
            out.println(raf.readUTF());
        } finally {
            if (raf!=null) {
                raf.close();
            }
        }
    }

    @Override
    public void rewriteInReverseFile(File from, File to) throws IOException {
        RandomAccessFile raf = null;
        RandomAccessFile rafOut = null;
        try {
            raf = new RandomAccessFile(from,"r");
            rafOut = new RandomAccessFile(to,"rw");
            long totalSize = raf.length();
            while (totalSize>0) {
                totalSize-=1;
                raf.seek(totalSize);
                rafOut.write(raf.read());
            }
        } catch (IOException e) {
            throw e;
        } finally {
            if (raf!=null) {
                raf.close();
            }
            if (rafOut!=null) {
                rafOut.close();
            }
        }
    }

}

