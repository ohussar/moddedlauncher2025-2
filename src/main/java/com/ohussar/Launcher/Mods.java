package com.ohussar.Launcher;

import com.google.gson.JsonElement;
import com.google.gson.stream.MalformedJsonException;
import com.ohussar.HTTP.HttpRequester;
import com.ohussar.Main;
import com.ohussar.Util.ListSplitter;
import com.ohussar.Window.Window;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

public class Mods {
    // Thread safe: pode acontecer que as threads de download acessem simultâneamente o mesmo elemento (remove)
    // se isso não for thread safe, ambas vão baixar o mesmo arquivo...
    public static Set<DownloadInfo> toDownloadRef = Collections.synchronizedSet(new HashSet<>());

    public static void checkAndDownloadMods(){
        JsonElement json = null;
        try {
            json = HttpRequester.makeRequest(Main.modAdress);
        } catch (IOException | InterruptedException | URISyntaxException e ) {
            Window.createrAlert("Checagem de mods falhou! Tentando iniciar mesmo assim");
            return;
        }
        if(json != null)
        {
            String urls = json.getAsJsonObject().get("links").getAsString();
            File modFolder = new File(Main.modFolder);
            if(!modFolder.exists()){
                boolean a = modFolder.mkdirs();
            }
            String[] urlsArray = urls.split(" ");
            String[] filenamesArray = new String[urlsArray.length];

            List<DownloadInfo> toDownload = new ArrayList<>();


            for(int i = 0; i < urlsArray.length; i++){
                String[] sub = urlsArray[i].split("/");
                filenamesArray[i] = sub[sub.length-1];
            }

            FilenameFilter filter = new FilenameFilter() {

                @Override
                public boolean accept(File dir, String name) {
                    if(name.endsWith(".jar")){
                        return true;
                    }
                    return false;
                }
            };
            File[] modsInFolder = modFolder.listFiles(filter);
            if(modsInFolder != null) {
                for (int i = 0; i < modsInFolder.length; i++) {
                    boolean has = false;
                    for (int j = 0; j < filenamesArray.length; j++) {
                        if (filenamesArray[j].equals(modsInFolder[i].getName())) {
                            has = true;
                            if(modsInFolder[i].length() == 0){
                                has = false;
                            }
                        }
                    }
                    if (!has) {
                        modsInFolder[i].delete();
                    }
                }
            }

            for(int i = 0; i < filenamesArray.length; i++){
                boolean has = false;
                if(modsInFolder != null) {
                    for (int j = 0; j < modsInFolder.length; j++) {
                        if (filenamesArray[i].equals(modsInFolder[j].getName())) {
                            has = true;
                            if(modsInFolder[j].length() == 0){
                                has = false;
                            }
                        }
                    }
                }
                if(!has){
                    toDownload.add(new DownloadInfo(urlsArray[i], filenamesArray[i]));
                    toDownloadRef.add(new DownloadInfo(urlsArray[i], filenamesArray[i]));
                }
            }

            List<Thread> downloadThreads = new ArrayList<>();

            if(!toDownload.isEmpty()){
                Window.setDownloadThreadsBarVisible(true);
            }

            Window.setMaxProgressPhase(1, toDownload.size());

            for(int i = 0; i < Main.downloadThreads; i++){
                String threadName = "thread-downloader-"+i;
                int finalI = i;
                Thread thread = new Thread(() -> {
                    if (!toDownloadRef.isEmpty()) {
                        downloadSubList(toDownload);
                    }else{
                        Window.setNthDownloadVisible(finalI, false);
                    }
                    Thread.currentThread().interrupt();
                });
                thread.setName(threadName);
                thread.start();
                downloadThreads.add(thread);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            Thread thread = new Thread(() -> {
                while(true){
                    boolean hasActive = false;
                    for(Thread t : downloadThreads){
                        if(!t.isInterrupted()){
                            hasActive = true;
                        }
                    }
                    if(!hasActive){
                        System.out.println("finished");
                        break;
                    }
                }
                Thread.currentThread().interrupt();
            });
            thread.setName("download-watcher-thread");
            thread.start();
        }
    }

    public static void downloadSubList(List<DownloadInfo> toDownload){
        String curThread = Thread.currentThread().getName();
        int i = -1;
        if(curThread.startsWith("thread-downloader")) {
            String it = curThread.split("thread-downloader-")[1];
            i = Integer.parseInt(it);
        }
        for( DownloadInfo downloadInfo : toDownload){
            if(toDownloadRef.remove(downloadInfo)) {
                // Thread safe
                String filename = downloadInfo.filename;
                String downloadurl = downloadInfo.downloadUrl;
                if(!filename.isEmpty()) {
                    try {
                        Window.updateNthDownloadBar(i, 0);
                        Window.updateNthDownloadLabel(i, downloadInfo.filename);
                        HttpRequester.download(downloadurl, filename, Main.modFolder, Mods::Hook);
                        Window.updateStartGamePhaseProgress(1, 1);
                    } catch (IOException e) {
                        try {
                            HttpRequester.download(downloadurl, filename, Main.modFolder, Mods::Hook);
                            Window.updateStartGamePhaseProgress(1, 1);
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }
            }
        }
        Window.setNthDownloadVisible(i, false);
    }

    public static void Hook(Object obj){
        String curThread = Thread.currentThread().getName();
        if(curThread.startsWith("thread-downloader")){
            String it = curThread.split("thread-downloader-")[1];
            int i = Integer.parseInt(it);
            if(obj instanceof HttpRequester.HookInfo info){
                if(info.id().equals("write")){
                    Window.updateNthDownloadBarSmooth(i, info.value());
                }else{
                    Window.setDownloadBarMaxValue(i, info.value());
                }
            }

        }
    }
    public record DownloadInfo(String downloadUrl, String filename) { }
}
