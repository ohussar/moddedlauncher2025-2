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


    public static void startProcedure(Object obj) {
        Thread thread = new Thread(() -> {
            JsonElement json = null;
            try {
                json = HttpRequester.makeRequest(Main.forgeAdress);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
            if(json != null)
            {
                String url = json.getAsJsonObject().get("url").getAsString();
                try {
                    HttpRequester.download(url, "Minecraft.zip", "./", Forge::hook);
                    Unzip.unzip("./Minecraft.zip");
                    Window.closePopup();
                    Thread.currentThread().interrupt();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        Window.createPopup(null);
        thread.start();

    }

    public static void hook(Object obj){
        int value = (int) obj;
        Window.incrementProgressBarValue(value);
    }
}
