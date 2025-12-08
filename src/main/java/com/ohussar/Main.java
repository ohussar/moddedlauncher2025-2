package com.ohussar;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ohussar.Launcher.Loader;
import com.ohussar.Window.Renderer;
import com.ohussar.Window.Window;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    public static String minecraftPath = "."+ File.separator + "Minecraft"  + File.separator + ".minecraft";

    private static String serverAdress = "http://localhost:25523";//"https://server-test.ashycoast-64e998bb.brazilsouth.azurecontainerapps.io";
    private static String password = "?code=testCode";

    public static String forgeAdress = serverAdress+"/ForgeDownloadLink"+password;
    public static String modAdress = serverAdress+"/Mods"+password;

    public static String modFolder = "./Folder/Minecraft/mods";

    public static final int downloadThreads = 2;

    public static void main(String[] args) throws IOException, URISyntaxException, InterruptedException, FontFormatException {
        System.setProperty("sun.java2d.uiScale.enabled", "true");
        System.setProperty("sun.java2d.uiScale", "1");
        Loader.Init();
        Renderer.Init();
        Window.Init();
    }


}