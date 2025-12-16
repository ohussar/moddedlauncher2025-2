package com.ohussar.Util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

public class Util {

    public static void writeToFile(String path, String info){
        File file = new File(path);
        String newP = path.replace(File.separator, "@");
        String[] paths = newP.split("@");
        String foldersBefore = "";
        for(int i = 0; i < paths.length; i++){
            if(i == paths.length-1){
                break;
            }
            if(i!=0) {
                foldersBefore += File.separator + paths[i];
            }else{
                foldersBefore += paths[i];
            }
        }
        File foldersBef = new File(foldersBefore);
        foldersBef.mkdirs();

        if(!file.exists()){
            try {
                Files.createFile(file.toPath());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        try {
            Files.writeString(file.toPath(), info);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String readFile(String path){
        try {
            return Files.readString(Path.of(path));
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

}
