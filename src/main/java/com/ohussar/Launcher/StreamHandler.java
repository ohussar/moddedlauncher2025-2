package com.ohussar.Launcher;

import java.io.IOException;
import java.io.InputStream;

public class StreamHandler {
    public static Thread inputStreamHandler;
    public static Thread errorStreamHandler;
    public static void handleErrorStream(InputStream stream){
        if(errorStreamHandler != null){
            errorStreamHandler.interrupt();
            errorStreamHandler = null;
        }
        errorStreamHandler = new Thread(() ->{
            int data;
            while (true) {
                try {
                    if ((data = stream.read()) == -1) break;
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            errorStreamHandler.interrupt();
        });
        errorStreamHandler.setName("error-stream-handler");
        errorStreamHandler.start();
    }
    public static void handleInputStream(InputStream stream){
        if(inputStreamHandler != null){
            inputStreamHandler.interrupt();
            inputStreamHandler = null;
        }
        inputStreamHandler = new Thread(() ->{
            int data;
            while (true) {
                try {
                    if ((data = stream.read()) == -1) break;
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            inputStreamHandler.interrupt();
        });
        inputStreamHandler.setName("input-stream-handler");
        inputStreamHandler.start();
    }
}
