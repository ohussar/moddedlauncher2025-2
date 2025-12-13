package com.ohussar.Launcher;

import com.ohussar.Main;
import com.ohussar.Util.Util;
import com.ohussar.Window.Window;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StartGame {
    public static void startGame(){

        String res = Loader.FINAL_COMMAND;
        res = res.replace("${RAM}", Integer.toString(Config.ram));
        res = res.replace("${auth_player_name}", Config.username);
        res = res.replace("${auth_uuid}",  UUID.nameUUIDFromBytes(Config.username.getBytes()).toString());
        res = res.replace("%CLASSPATH%", "@" + System.getProperty("user.dir") + File.separator + "bakedclasspath.txt");

        if(Config.username.isEmpty()){
            Window.createrAlert("O nome de usuário está vazio!");
            StartProcedure.reset();
            return;
        }

        if(Config.username.length() < 3){
            Window.createrAlert("O nome de usuário precisa ter pelo menos 3 caracteres!");
            StartProcedure.reset();
            return;
        }

        if(Config.username.contains(" ")){
            Window.createrAlert("O nome de usuário não pode conter espaço!");
            StartProcedure.reset();
            return;
        }
        if(Config.ram < 2048 && Config.ram > 0){
            Window.createrAlert("Aloque pelo menos 2048 Mb de ram!");
            StartProcedure.reset();
            return;
        }

        if(Config.ram <= 0){
            Window.createrAlert("Aloque uma quantia válida de ram!");
            StartProcedure.reset();
            return;
        }


        String finalPath = "";
        if(Main.minecraftPath.startsWith("."+File.separator)){ // relative path
            finalPath = System.getProperty("user.dir") + File.separator + Main.minecraftPath.replace("."+File.separator, "");
            Util.writeToFile(finalPath+File.separator+"test.bat", res);
        }else{
            finalPath = Main.minecraftPath;
            Util.writeToFile(finalPath+File.separator+"test.bat", res);
        }

        try {
            Main.minecraftProcess = Runtime.getRuntime().exec("cmd.exe /c cd /d" + finalPath +File.separator + " && cmd /c test.bat");
            StreamHandler.handleInputStream(Main.minecraftProcess.getInputStream());
            StreamHandler.handleErrorStream(Main.minecraftProcess.getErrorStream());
            Window.offsetButtonsWhenPlayButtonPressed(false);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
