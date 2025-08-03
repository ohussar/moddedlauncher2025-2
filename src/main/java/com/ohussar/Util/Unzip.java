package com.ohussar.Util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Unzip {
    public static void unzip(String file){
        try {
            File dest = new File("./");
            byte[] buffer = new byte[1024];
            ZipInputStream zis = new ZipInputStream(new FileInputStream(file));
            ZipEntry zipEntry = zis.getNextEntry();
            while(zipEntry != null){
                File newFile = newFile(dest, zipEntry);
                if(!zipEntry.isDirectory()){
                    File parent = newFile.getParentFile();
                    if(!parent.isDirectory() && !parent.mkdirs()){

                    }
                    FileOutputStream fos = new FileOutputStream(newFile);
                    int len;
                    while((len = zis.read(buffer)) > 0){
                        fos.write(buffer, 0, len);
                    }
                    fos.close();
                }
                zipEntry = zis.getNextEntry();
            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    public static File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
        File destFile = new File(destinationDir, zipEntry.getName());

        String destDirPath = destinationDir.getCanonicalPath();
        String destFilePath = destFile.getCanonicalPath();

        if (!destFilePath.startsWith(destDirPath + File.separator)) {
            throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
        }

        return destFile;
    }

}
