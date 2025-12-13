package com.ohussar.Window.Graphics;

import com.ohussar.Main;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.IOError;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class Images {
    public static BufferedImage backgroundImage;
    public static BufferedImage popupImage;
    public static BufferedImage buttonImage;
    public static BufferedImage buttonHoverImage;
    public static BufferedImage closeButtonImage;
    public static BufferedImage closeButtonHoverImage;

    public static BufferedImage progressBarFrameImage;
    public static BufferedImage progressBarCenterImage;

    public static NineSlice progressBar;
    public static NineSlice button;
    public static NineSlice buttonHover;
    public static NineSlice buttonLocked;

    public static NineSlice inputHover;
    public static NineSlice input;

    public static BufferedImage title;

    public static NineSlice background9Slice;

    public static ImageIcon hampter;
    public static URL hampterUrl;

    static {
        try {
            hampterUrl = new URL("https://media.tenor.com/yKjcNbCzNUoAAAAM/yessir-4t.gif");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void Init(){
        try {
            backgroundImage = readImage("background.png");
            popupImage = readImage("popup.png");

            buttonImage = readImage("button.png");
            buttonHoverImage = readImage("button_selected.png");
            closeButtonImage = readImage("close.png");
            closeButtonHoverImage = readImage("close_selected.png");

            BufferedImage buttonBorder = readImage("button_border.png");
            BufferedImage buttonHoverBorder = readImage("button_border_selected.png");
            BufferedImage buttonCenter = readImage("button_center.png");
            BufferedImage buttonCenterLocked = readImage("button_center_locked.png");

            BufferedImage inputCenter = readImage("input_text_center.png");

            input = new NineSlice(buttonBorder, inputCenter);
            inputHover = new NineSlice(buttonHoverBorder, inputCenter);

            button = new NineSlice(buttonBorder, buttonCenter);
            buttonHover = new NineSlice(buttonHoverBorder, buttonCenter);
            buttonLocked = new NineSlice(buttonBorder, buttonCenterLocked);

            progressBarCenterImage = readImage("progress_bar_center.png");
            progressBarFrameImage = readImage("progress_bar_frame.png");

            progressBar = new NineSlice(buttonBorder, buttonCenter);

            title = readImage("title.png");

            BufferedImage backc = readImage("background_center.png");
            BufferedImage backb = readImage("background_border.png");

            background9Slice = new NineSlice(backb, backc);


            hampter = new ImageIcon(hampterUrl);


        } catch (IOException e) {
            throw new IOError(e);
        }
    }

    public static BufferedImage readImage(String name) throws IOException {
        return ImageIO.read(Main.class.getResourceAsStream("/images/" + name));
    }
}
