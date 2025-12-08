package com.ohussar.Util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Util {

    public static void writeToFile(String path, String info){
        File file = new File(path);
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
            return "";
        }
    }

}
