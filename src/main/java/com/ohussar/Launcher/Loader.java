package com.ohussar.Launcher;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ohussar.Main;
import com.ohussar.Util.Util;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;

public class Loader {
    public static String path = "." + File.separator + "minecraft.json";
    public static HashMap<String, String> map = new HashMap<>();

    public static String CLASSPATH = "";
    public static String FINAL_COMMAND = "";
    public static void Init(){
        String content = "{}";
        content = Util.readFile(path);
        CLASSPATH = Util.readFile("./classpath.txt");

        map.put("${natives_directory}", PathMaker.buildPath( ".","versions", "aaaa", "natives"));
        map.put("${versions_directory}", PathMaker.buildPath( ".","versions"));
        map.put("${launcher_name}", "minecraft-launcher");
        map.put("${launcher_version}", "2.3.173");
        map.put("${version_name}", "aaaa");
        map.put("${library_directory}",  PathMaker.buildPath( ".","libraries"));
        map.put("${game_directory}", ".");
        map.put("${assets_root}", PathMaker.buildPath( ".","assets"));
        map.put("${assets_index_name}", "17");
        map.put("${auth_access_token}", "null");
        map.put("${clientid}", "null");
        map.put("${auth_xuid}", "null");
        map.put("${user_type}", "mojang");
        map.put("${version_type}", "modified");
        map.put("${classpath_separator}", ";");
        map.put("${classpath}", "%CLASSPATH%");

        JsonElement json = JsonParser.parseString(content);
        JsonObject arguments = json.getAsJsonObject().get("arguments").getAsJsonObject();
        JsonArray jvm = arguments.get("jvm").getAsJsonArray();
        JsonArray game = arguments.get("game").getAsJsonArray();

        for (Map.Entry<String, String> entry : map.entrySet()){
            if(entry.getValue().startsWith("." + File.separator)){
                String rep = entry.getValue().replace("." + File.separator, "");
                String p = "";
                if(Main.minecraftPath.startsWith("." + File.separator)){ // localpath
                    p = Main.minecraftPath.replace("." + File.separator, "");
                    String path = System.getProperty("user.dir") + File.separator + p + File.separator + rep;
                    CLASSPATH = CLASSPATH.replace(entry.getKey(), path);
                }else{
                    String path = Main.minecraftPath + File.separator + rep;
                    CLASSPATH = CLASSPATH.replace(entry.getKey(), path);
                }
            }
            //CLASSPATH = CLASSPATH.replace(entry.getKey(), path);
        }
        System.out.println("Classpath loaded!");

        Util.writeToFile("bakedclasspath.txt", CLASSPATH);

        String osName = System.getProperty("os.name").toLowerCase();
        String finalCommand = PathMaker.buildPath(".",
                "runtime",
                "java-runtime-delta",
                "windows",
                "java-runtime-delta",
                "bin",
                "javaw.exe"
                ) + " -Xss1M -Dos.name=\"Windows 10\" -Dos.version=10.0 ";
        //alterar isso aqui...
        String finalPart = getReplaced("-Xmx${RAM}M -XX:+UnlockExperimentalVMOptions -XX:+UseG1GC -XX:G1NewSizePercent=20 -XX:G1ReservePercent=20 -XX:MaxGCPauseMillis=50 -XX:G1HeapRegionSize=32M -Dfml.ignoreInvalidMinecraftCertificates=true -Dfml.ignorePatchDiscrepancies=true -Djava.net.preferIPv4Stack=true -Dminecraft.applet.TargetDirectory=${versions_directory}" + File.separator + "${version_name} -DlibraryDirectory=${library_directory} -Dlog4j.configurationFile=${assets_root}" + File.separator + "log_configs" + File.separator + "client-1.12.xml " +
                "-DLibLoader.modsFolder=${game_directory}" + File.separator + "mods " + "cpw.mods.bootstraplauncher.BootstrapLauncher ");
        for (int i = 0; i < jvm.size(); i++){
            JsonObject obj = jvm.get(i).getAsJsonObject();
            JsonArray values = obj.get("values").getAsJsonArray();
            boolean can = true;
            if(obj.has("rules")){
                can = false;
            }

            for(int j = 0; j < values.size(); j++){
                if(values.get(j).getAsString().equals("-XX:HeapDumpPath=MojangTricksIntelDriversForPerformance_javaw.exe_minecraft.exe.heapdump")
                && osName.equals("windows 10")){
                    can = true;
                }
            }

            if(can){
                for(int j = 0; j < values.size(); j++){
                    finalCommand += getReplaced(values.get(j).getAsString()) + " ";
                }
            }
        }
        finalCommand += finalPart;
        for (int i = 0; i < game.size(); i++){
            JsonObject obj = game.get(i).getAsJsonObject();
            JsonArray values = obj.get("values").getAsJsonArray();
            boolean can = true;
            if(obj.has("rules")){
                can = false;
            }
            if(can){
                for(int j = 0; j < values.size(); j++){
                    String v = values.get(j).getAsString();
                    finalCommand += getReplaced(v) + " ";
                }
            }
        }

        FINAL_COMMAND =
                finalCommand + "";

        Util.writeToFile(PathMaker.buildPath(Main.minecraftPath, "test.bat"), FINAL_COMMAND);
    }

    private static String getReplaced(String separator) {
        String finalPart = separator;
        for (Map.Entry<String, String> entry : map.entrySet()) {
            finalPart = finalPart.replace(entry.getKey(), entry.getValue());
        }
        return finalPart;
    }

}
