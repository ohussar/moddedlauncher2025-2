package com.ohussar.Launcher;

import com.google.gson.JsonElement;
import com.ohussar.HTTP.HttpRequester;
import com.ohussar.Main;
import com.ohussar.Util.Unzip;
import com.ohussar.Window.Window;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StartProcedure {
    public static void startProcedure(Object obj) {
        Thread thread = new Thread(() -> {
            Window.offsetButtonsWhenPlayButtonPressed(true);
            Window.beginNewPhase(0);
            if(!Config.isForgeInstalled) {
                Forge.installForge();
            }else{
                Window.hidePopup(null);
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Window.offsetButtonsWhenPlayButtonPressed(false);
                throw new RuntimeException(e);
            }
            Window.beginNewPhase(1);
            Mods.checkAndDownloadMods();
            while(true){
                boolean can = false;

                for(Thread t : Thread.getAllStackTraces().keySet()){
                    if(t.getName().equals("download-watcher-thread")){
                        can = true;
                    }
                }

                if(!can){
                    System.out.println("call");
                    break;
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Window.offsetButtonsWhenPlayButtonPressed(false);
                    throw new RuntimeException(e);
                }
            }

            StartGame.startGame();
        });
        Window.setPopupVisible(null);
        thread.start();

    }


}
