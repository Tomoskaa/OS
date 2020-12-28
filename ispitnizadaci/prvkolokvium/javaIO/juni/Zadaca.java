package ispitnizadaci.prvkolokvium.javaIO.juni;

import java.io.*;


//NE MI RABOTI PREFRLANJETO NA SODRZINATA VO WRIATABLE-CONTENT.TXT OTKAKO KJE GO IZBRISAM OUT IMENIKOT!!!!!!!!!!!!!!!!!!

public class Zadaca {

    public static void main(String[] args) throws IOException {
        String in = "D:\\OS\\resources\\in";
        String out = "D:\\OS\\resources\\out";

        manage(in,out);
    }

    public static void manage(String in, String out) throws IOException {
        File source = null;
        File destination = null;
        RandomAccessFile raf = null;
        FileInputStream fis = null;
        File sodrzina = null;

        try {
            source = new File(in);
            destination = new File(out);
            sodrzina = new File("D:\\OS\\resources\\writable-content.txt");
            raf = new RandomAccessFile(sodrzina, "rw");


            //dokolki imenikot in ne postoi se pecati poraka
            if (!(source.exists() && source.isDirectory())) {
                System.err.println("Ne postoi");
                return;
            }

            //dokolku imenikot out postoi treba da se izbrise negovata sodrzina, vo nego ima samo datoteki
            if (destination.exists()) {
                File[] files = destination.listFiles();
                for (File f : files) {
                    f.delete();
                }
            }

            //vo posebna funkcija gi listam site datoteki od edna papka koi zavrsuvaat na .dat
            File[] files = source.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File file, String s) {
                    File f = new File(s);

                    if (f.isDirectory())
                        return true;
                    if (f.getName().endsWith(".dat"))
                        return true;

                    return false;
                }
            });

            for (File f : files) {

                //dokolku vo datotekata moze da se zapisuva treba da se premesti vo imenikot out
                if (f.canRead()) {
                    f.renameTo(new File(destination.getAbsolutePath(), f.getName()));
                    System.out.println("pomestuvam " + f.getAbsolutePath());
                } else if (f.isHidden()) {
                    System.out.println("zbunet sum " + f.getAbsolutePath());
                    f.delete();
                } else {
                    fis = new FileInputStream(f);
                    //so raf.seek ja vrakjame dolzinata na datotekata koja se cita
                    raf.seek(sodrzina.length());
                    //citame bit po bit
                    int c = 0;
                    while((c = fis.read()) != -1) {
                        raf.write(c);
                    }
                    System.out.println("dopisuvam " + f.getAbsolutePath());
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if(fis != null) {
                fis.close();
            }
            if(raf != null) {
                raf.close();
            }
        }
    }
}

//https://pastebin.com/B5Arcxxa?fbclid=IwAR2WT3dZaL00z9BQX8qC9vvtuJrepRZ57AtduDghhu6QlT0P_XKO3NOZN_c

