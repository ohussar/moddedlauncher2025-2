package com.ohussar;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ohussar.HTTP.HttpRequester;
import com.ohussar.Launcher.*;
import com.ohussar.Util.Timer;
import com.ohussar.Util.Vector2i;
import com.ohussar.Window.Coordinate;
import com.ohussar.Window.Graphics.Images;
import com.ohussar.Window.Renderer;
import com.ohussar.Window.SplashScreen;
import com.ohussar.Window.Window;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    public static final boolean isDevEnviroment = false;

    public static final String version = "1.3.1";

    public static String rootPath = ".";
    public static String minecraftPath = PathMaker.buildPath(rootPath, "Minecraft", ".minecraft");

    public static final String serverAdress = isDevEnviroment ? "http://localhost:25523" : "https://server-test.ashycoast-64e998bb.brazilsouth.azurecontainerapps.io";

    public static final String password = "?code=testCode";

    public static String forgeAdress = serverAdress+"/ForgeDownloadLink"+password;
    public static String modAdress = serverAdress+"/Mods"+password;

    public static String modFolder = PathMaker.buildPath(minecraftPath, "mods");

    public static final int downloadThreads = 2;

    public static Process minecraftProcess;

    public static void main(String[] args) throws IOException, URISyntaxException, InterruptedException, FontFormatException {
        System.setProperty("sun.java2d.uiScale.enabled", "true");
        System.setProperty("sun.java2d.uiScale", "1");

        Renderer.Init();
        Images.Init();
        SplashScreen.Init();

        UpdateManager.checkForUpdates();


        Timer.createTimer(10000, (i) ->{
            Window.initializeWindow();
        });
        try {
            JsonElement element = HttpRequester.makeRequest(serverAdress + "/Info");
            Window.informationText = element.getAsJsonObject().get("text").getAsString();
            Window.initializeWindow();
        } catch (Exception e){
            Window.informationText = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. In sed ante in augue dictum viverra. Duis eu tellus blandit, efficitur orci et, eleifend risus. Fusce ligula elit, volutpat sit amet dui nec, porta rhoncus tellus.";
            Window.initializeWindow();
        }
        Thread thread = new Thread(() -> {
            while(true){
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                if(minecraftProcess != null && minecraftProcess.isAlive()){
                    if(Window.playButton != null){
                        if(Window.playButton.getText().equals("Jogar")){
                            Window.playButton.setText("Encerrar");
                            //Window.offsetButtonsWhenPlayButtonPressed(true);
                            Window.playButton.onPress((o) -> {
                                ProcessHandle h = minecraftProcess.toHandle();
                                if(h.isAlive()){
                                    h.descendants().forEach(ProcessHandle::destroyForcibly);
                                    h.destroyForcibly();
                                }
                            });
                        }
                    }
                }else{
                    if(Window.playButton != null){
                        if(Window.playButton.getText().equals("Encerrar")){
                            //Window.offsetButtonsWhenPlayButtonPressed(false);
                            Window.playButton.setText("Jogar");
                            Window.playButton.onPress(StartProcedure::startProcedure);
                        }
                    }
                }

            }
        });
        thread.start();
    }


}