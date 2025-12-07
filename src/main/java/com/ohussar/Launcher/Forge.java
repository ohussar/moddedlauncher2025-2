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
            json = HttpRequester.makeRequest(Main.forgeAdress);
        } catch (IOException | InterruptedException | URISyntaxException e) {

            throw new RuntimeException(e);
        }


        if(json != null)
        {
            String url = json.getAsJsonObject().get("url").getAsString();
            try {
                HttpRequester.download(url, "Minecraft.zip", "./", Forge::hook);
                Unzip.unzip("./Minecraft.zip");
                Window.closePopup();
            } catch (IOException e) {
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
