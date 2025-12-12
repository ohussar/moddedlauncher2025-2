package com.ohussar.Window;

import com.ohussar.Util.Vector2i;
import com.ohussar.Window.Components.Background;
import com.ohussar.Window.Components.Label;
import com.ohussar.Window.Graphics.Images;

import javax.swing.*;
import java.awt.*;

public class SplashScreen {

    public static JFrame frame;

    public static Dimension frameSize = new Dimension(640 + 4 *  Renderer.scaleFactor, 200);

    public static Label description;

    public static void Init(){

        frame = new JFrame();
        frame.setPreferredSize(frameSize);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setUndecorated(true);
        frame.setResizable(false);
        frame.setLayout(null);
        frame.pack();

        description = new Label(frame, Vector2i.zero(), "Aguardando informações");

        description.setPosition(
                new Coordinate(frameSize)
                        .centerX((int)description.getPreferredSize().getWidth())
                        .offset(2 * Renderer.scaleFactor, frameSize.height - description.getPreferredSize().height - 2 * Renderer.scaleFactor)
                        .get()
        );



        Dimension size = new Dimension(640, 360);
        Background back = new Background(frame, Images.title, size,
                new Coordinate(frameSize)
                        .centerX(size.width)
                        .centerY(size.height)
                        .get()
        );
        new Background(frame, Images.background9Slice, frameSize);


        frame.setVisible(true);
        frame.pack();
        frame.setLocationRelativeTo(null);
    }



}
