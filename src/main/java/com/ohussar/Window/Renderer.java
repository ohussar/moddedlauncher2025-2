package com.ohussar.Window;

import com.ohussar.Main;
import com.ohussar.Util.Vector2i;
import com.ohussar.Window.Graphics.NineSlice;

import javax.swing.*;
import java.awt.*;
import java.awt.image.ImageObserver;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Renderer {
    public static Font mainFont;
    public static int scaleFactor = 3;
    public static float defaultFontSize = 8f * scaleFactor;
    public static void Init() throws IOException, FontFormatException {
        mainFont = Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(Main.class.getResourceAsStream("/fonts/Minecraft.ttf"))).deriveFont(defaultFontSize);
    }
    public static void setFontSizePreference(float size){
        mainFont = mainFont.deriveFont(size);
    }


    public static void renderImage(Graphics g, Image image, Vector2i position, Dimension size, Object observer){
        g.drawImage(image, position.x, position.y, size.width, size.height, (ImageObserver) observer);
    }

    public static void renderString(Graphics g, String text, Dimension size, Vector2i offset, Object observer){
        g.setFont(mainFont);
        g.setColor(Color.white);
        int width = g.getFontMetrics().stringWidth(text);
        int height = g.getFontMetrics().getHeight();

        g.drawString(text, (size.width - width) / 2 + offset.x, (size.height + height / 2 + offset.y) / 2);
        setFontSizePreference(defaultFontSize);
    }

    public static int getMultilineHeight(String text, Dimension size,Vector2i offset){
        JButton bt = new JButton();
        setFontSizePreference(16f);
        FontMetrics m = bt.getFontMetrics(mainFont);
        int height = m.getHeight();
        int startY = 0;
        int endY = 0;
        int h = 0;
        int fk = 0;
        if(text.startsWith("<html>")){
            String finalText = text.replace("<html>", "").replace("</html>", "");
            String[] words = finalText.split(" ");
            String[] lines = new String[4];
            int startIndex = 0;
            for(int k = 0; k < lines.length; k++) {
                String f = "";
                int j = 0;
                for(int i = startIndex; i < words.length; i++){
                    int w = m.stringWidth(f+words[i]+" ");
                    if(w > (size.width)){
                        startIndex = i;
                        break;
                    }
                    f+=words[i]+" ";
                    j=i;
                }
                h += height + 12;
                if(j == words.length-1){
                    fk = k;
                    break;
                }


            }
            return h;
        }
        return height;
    }

    public static void renderString(Graphics g, String text, Color color, Dimension size, Vector2i offset, Object observer){
        g.setFont(mainFont);
        g.setColor(color);
        int width = g.getFontMetrics().stringWidth(text);
        int height = g.getFontMetrics().getHeight();

        if(text.startsWith("<html>")){
            String finalText = text.replace("<html>", "").replace("</html>", "");
            String[] words = finalText.split(" ");
            String[] lines = new String[4];
            int startIndex = 0;
            for(int k = 0; k < lines.length; k++) {
                String f = "";
                int j = 0;
                for(int i = startIndex; i < words.length; i++){
                    int w = g.getFontMetrics().stringWidth(f+words[i]+" ");
                    if(w > (size.width)){
                        startIndex = i;
                        break;
                    }
                    f+=words[i]+" ";
                    j=i;
                }
                Vector2i newOff = new Vector2i(offset.x, offset.y + (height + 12) *k - 24);
                float pref = mainFont.getSize();
                renderString(g, f, color, size, newOff, null);
                setFontSizePreference(pref);
                if(j == words.length-1){
                    setFontSizePreference(defaultFontSize);
                    break;
                }
            }
            setFontSizePreference(defaultFontSize);
            return;
        }


        g.drawString(text, (size.width/2 - width/2) + offset.x, (size.height + height / 2 + offset.y) / 2);
        setFontSizePreference(defaultFontSize);
    }
    public static void renderStringInput(Graphics g, String text, Color color, Dimension size, Vector2i offset, Object observer) {
        g.setFont(mainFont);
        g.setColor(color);
        boolean ended=false;
        if(text.endsWith("|")){
            text = text.replace("|", "");
            ended = true;
        }

        int width = g.getFontMetrics().stringWidth(text);
        int height = g.getFontMetrics().getHeight();
        if(ended){
            text += "|";
        }
        g.drawString(text, (size.width - width) / 2 + offset.x, (size.height + height / 2 + offset.y) / 2);
        setFontSizePreference(defaultFontSize);
    }

    public static void render9Slice(Graphics g, NineSlice slice, Dimension size, Object observer){
        ImageObserver obs = (ImageObserver) observer;
        // top
        g.drawImage(slice.border, 0, 0, size.width, scaleFactor, obs);
        // bottom
        g.drawImage(slice.border, 0, size.height - scaleFactor, size.width, scaleFactor, obs);
        // left
        g.drawImage(slice.border, 0, scaleFactor, scaleFactor, size.height - scaleFactor, obs);
        // right
        g.drawImage(slice.border, size.width - scaleFactor, scaleFactor, scaleFactor, size.height - scaleFactor, obs);
        // center
        g.drawImage(slice.center, scaleFactor, scaleFactor, size.width - scaleFactor*2, size.height - scaleFactor*2, obs);
    }

}
