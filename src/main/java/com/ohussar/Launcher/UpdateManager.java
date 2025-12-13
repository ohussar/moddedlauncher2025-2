package com.ohussar.Launcher;

import com.google.gson.JsonElement;
import com.ohussar.HTTP.HttpRequester;
import com.ohussar.Main;

import java.io.IOException;
import java.net.URISyntaxException;

public class UpdateManager {

    public static final String checkUrl = "https://api.github.com/repos/ohussar/moddedlauncher2025-2";
    public static final String downloadUrl = "https://raw.githubusercontent.com/ohussar/moddedlauncher2025-2/master/release/launcher.jar";

    public static void checkForUpdates(){
        JsonElement element;
        try {
            element = HttpRequester.makeRequest(checkUrl);
        } catch (IOException | URISyntaxException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        if(element != null){
            JsonElement desc = element.getAsJsonObject().get("description");
            if(!desc.isJsonNull()) {
                String d = desc.getAsString();

                if(!Main.version.equals(d)){
                    try {
                        HttpRequester.download(downloadUrl, "launcherD.jar", ".", (b) -> {});
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }


    }



}
