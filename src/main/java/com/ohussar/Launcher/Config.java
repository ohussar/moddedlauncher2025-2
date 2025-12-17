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
import java.nio.file.Path;

public class Config {

    public static final String configFile = "config.json";
    public static String jsonReaded = "";

    public static String username = "";
    public static int ram = 0;
    public static String rootPath = "";

    public static boolean isForgeInstalled;
    public static boolean closeOnLaunch;
    public static boolean startOnServer;


    public static void loadConfigFile(){
        String fileContent = Util.readFile(configFile);
        JsonElement element = JsonParser.parseString(fileContent);
        String username = "";
        String rootPath = "";
        boolean close_on_launch = false;
        boolean start_on_server = false;
        int ram = 0;
        if(!element.isJsonNull()){
            jsonReaded = fileContent;
            JsonObject obj = element.getAsJsonObject();
            username = obj.get("username").getAsString();
            ram = obj.get("ram").getAsInt();
            rootPath = obj.get("root_path").getAsString();
            isForgeInstalled = obj.get("forge_installed").getAsBoolean();

            if(obj.has("close_on_launch")){
                close_on_launch = obj.get("close_on_launch").getAsBoolean();
            }else{
                addBoolean("close_on_launch", false);
            }
            if(obj.has("start_on_server")){
                start_on_server = obj.get("start_on_server").getAsBoolean();
            }else{
                addBoolean("start_on_server", false);
            }



        }else{
            jsonReaded = "{\"username\": \"\", \"ram\": 0, \"root_path\": default}, \"forge_installed\": false}";
        }

        closeOnLaunch = close_on_launch;
        startOnServer = start_on_server;

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
        addBoolean("forge_installed", p);
    }


    public static void setCloseOnLaunch(boolean p){
        closeOnLaunch = p;
        addBoolean("close_on_launch", p);
    }

    public static void setStartOnServer(boolean p){
        startOnServer = p;
        addBoolean("start_on_server", p);
    }

    public static void addString(String property, String value){
        JsonElement element = JsonParser.parseString(jsonReaded);
        if(!element.isJsonNull()){
            JsonObject obj = element.getAsJsonObject();
            obj.addProperty(property, value);
            Util.writeToFile(configFile, element.toString());
            jsonReaded = element.toString();
        }
    }
    public static void addBoolean(String property, boolean value){
        JsonElement element = JsonParser.parseString(jsonReaded);
        if(!element.isJsonNull()){
            JsonObject obj = element.getAsJsonObject();
            obj.addProperty(property, value);
            Util.writeToFile(configFile, element.toString());
            jsonReaded = element.toString();
        }
    }
    public static void addInt(String property, int value){
        JsonElement element = JsonParser.parseString(jsonReaded);
        if(!element.isJsonNull()){
            JsonObject obj = element.getAsJsonObject();
            obj.addProperty(property, value);
            Util.writeToFile(configFile, element.toString());
            jsonReaded = element.toString();
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
        Main.modFolder = PathMaker.buildPath(Main.minecraftPath, "mods");


        addString("root_path", newRootPath);
        Loader.Init();
    }

    public static void updateRam(int newValue){
        Config.ram = newValue;
        addInt("ram", newValue);
    }

    public static void updateUsername(String username){
        Config.username = username;
        addString("username", username);
    }


}
