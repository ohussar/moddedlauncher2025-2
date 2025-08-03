package com.ohussar;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ohussar.Window.Renderer;
import com.ohussar.Window.Window;

import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    private static String serverAdress = "http://localhost:8080";
    private static String password = "?code=testCode";

    public static String forgeAdress = serverAdress+"/ForgeDownloadLink"+password;

    public static void main(String[] args) throws IOException, URISyntaxException, InterruptedException, FontFormatException {
        System.setProperty("sun.java2d.uiScale.enabled", "true");
        System.setProperty("sun.java2d.uiScale", "1");

        Renderer.Init();
        Window.Init();
    }


}