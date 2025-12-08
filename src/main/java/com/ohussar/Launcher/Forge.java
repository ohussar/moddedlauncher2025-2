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

    public static void installForge(){
        JsonElement json = null;
        try {
            System.out.println(Main.forgeAdress);
            json = HttpRequester.makeRequest(Main.forgeAdress);
        } catch (IOException | InterruptedException | URISyntaxException e) {

            throw new RuntimeException(e);
        }


        if(json != null)
        {
            String url = json.getAsJsonObject().get("url").getAsString();
            try {
                HttpRequester.download(url, "Minecraft.zip", "./", Forge::hook);
                Thread.sleep(200);
                Unzip.unzip("./Minecraft.zip");
                Window.hidePopup(null);
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }


    public static void hook(Object obj){
        if(obj instanceof HttpRequester.HookInfo info){
            if(info.id().equals("write")){
                Window.incrementProgressBarValue(info.value());
            }else{
                Window.setProgressBarMaxValue(info.value());

            }
        }
    }
}
