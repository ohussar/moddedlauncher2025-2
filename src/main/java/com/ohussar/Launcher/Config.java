package com.ohussar.Launcher;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.ohussar.Main;
import com.ohussar.Util.Util;
import com.ohussar.Window.Window;

import java.io.File;

public class Config {

    public static final String configFile = "config.json";
    public static String jsonReaded = "";

    public static String username = "";
    public static int ram = 0;
    public static String rootPath = "";

    public static boolean isForgeInstalled;

    public static void loadConfigFile(){
        String fileContent = Util.readFile(configFile);
        JsonElement element = JsonParser.parseString(fileContent);
        String username = "";
        String rootPath = "";
        int ram = 0;
        if(!element.isJsonNull()){
            JsonObject obj = element.getAsJsonObject();
            username = obj.get("username").getAsString();
            ram = obj.get("ram").getAsInt();
            rootPath = obj.get("root_path").getAsString();
            isForgeInstalled = obj.get("forge_installed").getAsBoolean();
            jsonReaded = fileContent;
        }else{
            jsonReaded = "{\"username\": \"\", \"ram\": 0, \"root_path\": default}, \"forge_installed\": false}";
        }

        if(Window.usernameInput != null){
            if(!username.isEmpty()){
                Window.usernameInput.setText(username);
                Config.username = username;
            }else{
                Window.usernameInput.setFirst(true);
            }
        }
        if(Window.ramInput != null){
            if(ram != 0){
                Window.ramInput.setText(Integer.toString(ram));
                Config.ram = ram;
            }else{
                Window.ramInput.setText("4096");
                updateRam(4096);
            }
        }
        updateRootPath(rootPath);
    }

    public static void setForgeInstalled(boolean p){
        isForgeInstalled = p;
        JsonElement element = JsonParser.parseString(jsonReaded);
        if(!element.isJsonNull()){
            JsonObject obj = element.getAsJsonObject();
            obj.addProperty("forge_installed", p);
            Util.writeToFile(configFile, element.toString());
        }
    }

    public static void updateRootPath(String newRootPath){
        if(newRootPath.isEmpty()){
            newRootPath = "default";
        }
        if(newRootPath.equals("default")){
            Main.rootPath = ".";
        }else{
            Main.rootPath = newRootPath;
        }
        rootPath = newRootPath;
        Main.minecraftPath = PathMaker.buildPath(Main.rootPath, "Minecraft", ".minecraft");
        JsonElement element = JsonParser.parseString(jsonReaded);
        if(!element.isJsonNull()){
            JsonObject obj = element.getAsJsonObject();
            obj.addProperty("root_path", newRootPath);
            Util.writeToFile(configFile, element.toString());
        }
        Loader.Init();
    }

    public static void updateRam(int newValue){
        Config.ram = newValue;
        JsonElement element = JsonParser.parseString(jsonReaded);
        if(!element.isJsonNull()){
            JsonObject obj = element.getAsJsonObject();
            obj.addProperty("ram", newValue);
            Util.writeToFile(configFile, element.toString());
        }
    }

    public static void updateUsername(String username){
        Config.username = username;
        JsonElement element = JsonParser.parseString(jsonReaded);
        if(!element.isJsonNull()){
            JsonObject obj = element.getAsJsonObject();
            obj.addProperty("username", username);
            Util.writeToFile(configFile, element.toString());
        }
    }


}
