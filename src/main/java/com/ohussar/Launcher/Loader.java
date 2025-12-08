package com.ohussar.Launcher;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ohussar.Main;

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
        try {
            content = Files.readString(Path.of(path));
            CLASSPATH = Files.readString(Path.of("./classpath.txt"));
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }


        map.put("${natives_directory}", Main.minecraftPath
                + File.separator
                + "versions"
                + File.separator
                + "aaaa"
                + File.separator
                + "natives"
        );
        map.put("${versions_directory}", Main.minecraftPath + File.separator + "versions");
        map.put("${launcher_name}", "minecraft-launcher");
        map.put("${launcher_version}", "2.3.173");
        map.put("${version_name}", "aaaa");
        map.put("${library_directory}", Main.minecraftPath + File.separator + "libraries");

        //map.put("${auth_player_name}", "ohussar");
        map.put("${game_directory}", Main.minecraftPath);
        map.put("${assets_root}", Main.minecraftPath + File.separator + "assets");
        map.put("${assets_index_name}", "17");
        //map.put("${auth_uuid}", "4652a9b1476c4079941f419e9ae17667");
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
            CLASSPATH = CLASSPATH.replace(entry.getKey(), entry.getValue());
        }
        System.out.println("Classpath loaded!");

        tryFinishedClassPath();
        String osName = System.getProperty("os.name").toLowerCase();
        String finalCommand = Main.minecraftPath + File.separator +
                "runtime"
                + File.separator
                + "java-runtime-delta"
                + File.separator
                + "windows"
                + File.separator
                + "java-runtime-delta"
                + File.separator
                + "bin"
                + File.separator
                + "javaw.exe -Xss1M -Dos.name=\"Windows 10\" -Dos.version=10.0 ";
        String finalPart = "-Xmx${RAM}M -XX:+UnlockExperimentalVMOptions -XX:+UseG1GC -XX:G1NewSizePercent=20 -XX:G1ReservePercent=20 -XX:MaxGCPauseMillis=50 -XX:G1HeapRegionSize=32M -Dfml.ignoreInvalidMinecraftCertificates=true -Dfml.ignorePatchDiscrepancies=true -Djava.net.preferIPv4Stack=true -Dminecraft.applet.TargetDirectory=${versions_directory}" + File.separator + "${version_name} -DlibraryDirectory=${library_directory} -Dlog4j.configurationFile=${assets_root}" + File.separator + "log_configs" + File.separator + "client-1.12.xml " +
                "-DLibLoader.modsFolder=${game_directory}" + File.separator + "mods " + "cpw.mods.bootstraplauncher.BootstrapLauncher ";
        for (Map.Entry<String, String> entry : map.entrySet()){
            finalPart = finalPart.replace(entry.getKey(), entry.getValue());
        }
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
                    String v = values.get(j).getAsString();
                    for(Map.Entry<String, String> entry : map.entrySet()){
                        v = v.replace(entry.getKey(), entry.getValue());
                    }
                    finalCommand += v + " ";
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

            for(int j = 0; j < values.size(); j++){
                if(values.get(j).getAsString().equals("-XX:HeapDumpPath=MojangTricksIntelDriversForPerformance_javaw.exe_minecraft.exe.heapdump")
                        && osName.equals("windows 10")){
                    can = true;
                }
            }

            if(can){
                for(int j = 0; j < values.size(); j++){
                    String v = values.get(j).getAsString();
                    for(Map.Entry<String, String> entry : map.entrySet()){
                        v = v.replace(entry.getKey(), entry.getValue());
                    }
                    finalCommand += v + " ";
                }
            }
        }

        FINAL_COMMAND =
                finalCommand + "";
        try {
            Files.createFile(Path.of("test.bat"));
            Files.writeString(Path.of("test.bat"), FINAL_COMMAND);
        } catch (IOException e) {
            try {
                Files.writeString(Path.of("test.bat"), FINAL_COMMAND);
            } catch (IOException ex) {

            }
        }
    }

    public static void tryFinishedClassPath(){
        try {
            Files.createFile(Path.of("bakedclasspath.txt"));
            Files.writeString(Path.of("."+File.separator+"bakedclasspath.txt"), CLASSPATH);
        } catch (IOException e) {
            try {
                Files.writeString(Path.of("."+File.separator+"bakedclasspath.txt"), CLASSPATH);
            } catch (IOException ex) {

            }
        }
    }

}
