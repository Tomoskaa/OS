package aud1.files.impl;

import aud1.exceptions.FileExistsException;
import aud1.files.FileManager;

import java.io.*;
import java.util.Date;

public class FileManagerImpl implements FileManager {

    @Override
    public File getFileFromString(String file) {
        return new File(file);
    }

    @Override
    public File[] getFilesInFolder(File file) throws FileNotFoundException {
        if (!file.exists()) {
            throw new FileNotFoundException();
        }
        if (!file.isDirectory()) {
            throw new FileNotFoundException();
        }
        return file.listFiles();
    }

    @Override
    public void printFileNames(File file, PrintStream writer) throws FileNotFoundException {
        File[] files = getFilesInFolder(file);
        for (File f : files) {
            writer.println(f.getName());
        }
    }

    @Override
    public File[] getFilesFromString(String filePath) throws FileNotFoundException {
        return new File[0];
    }

    @Override
    public String getAbsPath(String relPath) throws FileNotFoundException {
        File f = new File(relPath);
        if (!f.exists()) {
            throw new FileNotFoundException();
        }
        return f.getAbsolutePath();
    }

    @Override
    public long getFileSize(String file) throws FileNotFoundException {
        File f = new File(file);
        if (!f.exists()) {
            throw new FileNotFoundException();
        }
        return f.length();
    }


    @Override
    public void printFilePermissions(File f, PrintStream writer) throws FileNotFoundException {
        if (!f.exists()) {
            throw new FileNotFoundException();
        }
        writer.println(String.format("Read: %x", f.canRead()));
        writer.println(String.format("Write: %x", f.canWrite()));
        writer.println(String.format("Execute: %x", f.canExecute()));
    }

    @Override
    public void createNewFile(String file) throws FileExistsException, IOException {
        File f = new File(file);
        if (f.exists()) {
            throw new FileExistsException();
        }
        f.createNewFile();
    }

    @Override
    public void createFolder(String folder) throws FileExistsException {
        File f = new File(folder);
        if (f.exists()) {
            throw new FileExistsException();
        }
        f.mkdir();
    }

    @Override
    public File[] filterImagesFilesInDir(String dirPath) throws FileNotFoundException {
        File file = new File(dirPath);
        if (!file.exists()) {
            throw new FileNotFoundException();
        }
        if (!file.isDirectory()) {
            throw new FileNotFoundException();
        }

        return file.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".png") || name.endsWith(".jpg");
            }
        });

    }

    @Override
    public void renameFile(File src, File dest) throws FileNotFoundException, FileExistsException {
        if (!src.exists()) {
            throw new FileNotFoundException();
        }
        if (dest.exists()) {
            throw new FileExistsException();
        }
        src.renameTo(dest);
    }

    @Override
    public Date getLastModified(String filePath) throws FileNotFoundException {
        File f = new File(filePath);
        return new Date(f.lastModified());
    }

    @Override
    public void filterImagesFilesInDirRec(File file, PrintStream out) throws FileNotFoundException {
        File[] files = file.listFiles();
        for (File f: files) {
            if (f.isDirectory()) {
                filterImagesFilesInDirRec(f,out);
            }
            if (f.isFile() && (f.getName().endsWith(".png") || f.getName().endsWith(".jpg"))) {
                out.println(f.getAbsolutePath());
            }
        }
    }

    @Override
    public boolean deleteFolder(File folder) throws FileNotFoundException {
        if (!folder.isDirectory()) {
            throw new FileNotFoundException();
        }
        File[] files = folder.listFiles();

        for (File f: files) {
            if (f.isDirectory()) {      //gi brise i vgnezdenite folderi
                deleteFolder(f);    //delete() go brise folderot no sammo ako e prazen
            }
            f.delete();     //ne gi brise vgnezdenite folderi
        }
        return folder.delete();
    }

}
