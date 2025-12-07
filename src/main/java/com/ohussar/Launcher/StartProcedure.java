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

            Forge.installForge();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            Mods.checkAndDownloadMods();

        });
        Window.createPopup(null);
        thread.start();

    }


}
