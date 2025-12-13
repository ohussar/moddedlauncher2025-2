package com.ohussar.Launcher;

import com.google.gson.JsonElement;
import com.ohussar.HTTP.HttpRequester;
import com.ohussar.Main;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;

public class UpdateManager {

    public static final String checkUrl = "https://api.github.com/repos/ohussar/moddedlauncher2025-2";
    public static final String downloadUrl = "https://raw.githubusercontent.com/ohussar/moddedlauncher2025-2/master/release/launcher.jar";

    public static void checkForUpdates(){

        JsonElement first;
        try {
            first = HttpRequester.makeRequest(Main.serverAdress + "/git" + Main.password);
        } catch (IOException | URISyntaxException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        String token = "";
        if(first != null){
            token = first.getAsJsonObject().get("token").getAsString();
        }
        System.out.println(token);

        JsonElement element;
        try {
            element = HttpRequester.makeRequest(checkUrl, token);
        } catch (IOException | URISyntaxException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        if(element != null){
            JsonElement desc = element.getAsJsonObject().get("description");
            System.out.println(desc);
            if(desc != null && !desc.isJsonNull()) {
                String d = desc.getAsString();

                if(!Main.version.equals(d)){
                    try {
                        HttpRequester.download(downloadUrl, "launcherD.jar", ".", (b) -> {});

                        File launcherFile = new File(PathMaker.buildPath(".", "launcher.jar"));
                        launcherFile.deleteOnExit();
                        String cmd = "cmd.exe /c start update.bat";
                        Process p = Runtime.getRuntime().exec(cmd);
                        System.exit(0);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }


    }



}
