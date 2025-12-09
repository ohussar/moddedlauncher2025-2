package com.ohussar.Launcher;

import com.google.gson.JsonElement;
import com.ohussar.HTTP.HttpRequester;
import com.ohussar.Main;
import com.ohussar.Util.Unzip;
import com.ohussar.Window.Window;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;

public class Forge {
    public static int zipFilecount = 5041;
    public static void installForge(){
        JsonElement json = null;
        try {
            json = HttpRequester.makeRequest(Main.forgeAdress);
        } catch (IOException | InterruptedException | URISyntaxException e) {
            Config.setForgeInstalled(false);
            throw new RuntimeException(e);
        }


        if(json != null)
        {
            String url = json.getAsJsonObject().get("url").getAsString();
            try {
                HttpRequester.download(url, "Minecraft.zip", "./", Forge::hook);
                Thread.sleep(200);
                Window.setProgressBarMaxValue(zipFilecount);

                Unzip.unzip("./Minecraft.zip", PathMaker.buildPath(".", "Minecraft", ".minecraft"), Forge::nameHook);

                Window.hidePopup(null);
                Config.setForgeInstalled(true);
            } catch (IOException | InterruptedException e) {
                Config.setForgeInstalled(false);
                throw new RuntimeException(e);
            }
        }
    }

    public static void nameHook(Object obj){
        String p = (String) obj;
        String fileName = p.split("@@@@")[0];
        int numb = Integer.parseInt(p.split("@@@@")[1]);
        Window.incrementProgressBarValue(1);
        Window.setPopLabelText("Extraindo: " + fileName);
    }

    public static void hook(Object obj){
        if(obj instanceof HttpRequester.HookInfo info){
            if(info.id().equals("write")){
                Window.incrementProgressBarValue(info.value());
                Window.updateStartGamePhaseProgress(0, info.value());
            }else{
                Window.setProgressBarMaxValue(info.value());
                Window.setMaxProgressPhase(0, info.value());
            }
        }
    }
}
