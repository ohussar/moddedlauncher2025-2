package com.ohussar.HTTP;


import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.Strictness;
import com.google.gson.stream.MalformedJsonException;
import com.ohussar.Window.Components.Trigger;
import com.ohussar.Window.Window;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpRequester {
    public static HttpClient client = HttpClient.newHttpClient();

    public static JsonElement makeRequest(String url) throws IOException, InterruptedException, URISyntaxException, MalformedJsonException {
        HttpRequest request = HttpRequest.newBuilder().GET().uri(new URI(url)).build();
        HttpResponse<String> a = client.send(request, HttpResponse.BodyHandlers.ofString());
        if(a.body().startsWith("<!DOCTYPE html>")){
            throw new IOException("aaa");
        }
        return JsonParser.parseString(a.body());
    }
    public static JsonElement makeRequest(String url, String token) throws IOException, InterruptedException, URISyntaxException, MalformedJsonException {
        HttpRequest request = HttpRequest.newBuilder().GET().header("Authorization", token).uri(new URI(url)).build();
        HttpResponse<String> a = client.send(request, HttpResponse.BodyHandlers.ofString());
        if(a.body().startsWith("<!DOCTYPE html>")){
            throw new IOException("aaa");
        }
        return JsonParser.parseString(a.body());
    }



    public static void download(String downloadUrl, String filename, String path, Trigger hook) throws IOException{
        URL url1 = new URL(downloadUrl);

        HttpURLConnection con = (HttpURLConnection) (url1.openConnection());

        long completeFileSize = con.getContentLengthLong();
        BufferedInputStream in = new BufferedInputStream(con.getInputStream());
        FileOutputStream out = new FileOutputStream(path + File.separator + filename);
        BufferedOutputStream bout = new BufferedOutputStream(out, (int)completeFileSize);

        hook.trigger(new HookInfo("maxValue", (int) completeFileSize));

        int BLOCKSIZE = 1024 * 64 * 100;
        byte[] data = new byte[BLOCKSIZE];
        int x = 0;
        while((x = in.read(data, 0, BLOCKSIZE)) >= 0){
            bout.write(data, 0, x);
            hook.trigger(new HookInfo("write", x));
        }
        data = null;
        bout.close();
        in.close();
        con.disconnect();
    }

    public record HookInfo(String id, int value){}

}