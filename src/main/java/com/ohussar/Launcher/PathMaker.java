package com.ohussar.Launcher;

import java.io.File;

public class PathMaker {


    public static String buildPath(String... pathArgs){
        String startPath = "";
        for(int i = 0; i < pathArgs.length; i++){
            if(i == pathArgs.length - 1){
                startPath += pathArgs[i];
            }else{
                startPath += pathArgs[i] + File.separator;
            }

        }

        return startPath;
    }
}
