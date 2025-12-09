package com.ohussar.Launcher;

import com.ohussar.Main;
import com.ohussar.Util.Util;
import com.ohussar.Window.Window;

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
        String finalPath = "";
        if(Main.minecraftPath.startsWith("."+File.separator)){ // relative path
            finalPath = System.getProperty("user.dir") + File.separator + Main.minecraftPath.replace("."+File.separator, "");
            Util.writeToFile(finalPath+File.separator+"test.bat", res);
        }else{
            finalPath = Main.minecraftPath;
            Util.writeToFile(finalPath+File.separator+"test.bat", res);
        }

        try {
            Main.minecraftProcess = Runtime.getRuntime().exec("cmd.exe /c cd " + finalPath +File.separator + " && cmd /c test.bat");
            StreamHandler.handleInputStream(Main.minecraftProcess.getInputStream());
            StreamHandler.handleErrorStream(Main.minecraftProcess.getErrorStream());
            Window.offsetButtonsWhenPlayButtonPressed(false);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
    public static List<String> fixParsedTokens(List<String> rawTokens) {
        List<String> fixedTokens = new ArrayList<>();

        for (int i = 0; i < rawTokens.size(); i++) {
            String current = rawTokens.get(i);

            if (current.endsWith("=") && (i + 1) < rawTokens.size()) {
                String next = rawTokens.get(i + 1);

                String mergedToken = current + next;

                mergedToken = mergedToken.replace("\"", "");

                fixedTokens.add(mergedToken);
                i++;
            } else {
                fixedTokens.add(current);
            }
        }
        return fixedTokens;
    }
    public static List<String> parseCommandString(String commandString) {
        List<String> tokens = new ArrayList<>();

        Pattern pattern = Pattern.compile("([^\\s\"=]+=|\"[^\"]*\"|\\S+)");
        Matcher matcher = pattern.matcher(commandString);

        while (matcher.find()) {
            String token = matcher.group();

            if (token.contains("=\"") && token.endsWith("\"")) {
                int valueStartIndex = token.indexOf('\"');
                String key = token.substring(0, valueStartIndex + 1);
                String value = token.substring(valueStartIndex + 1, token.length() - 1);

                tokens.add(key + value);

            } else if (token.startsWith("\"") && token.endsWith("\"")) {
                tokens.add(token.substring(1, token.length() - 1));
            } else {
                tokens.add(token);
            }
        }
        return tokens;
    }
}
