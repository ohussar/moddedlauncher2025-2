package com.ohussar.Window;

import com.ohussar.Launcher.Forge;
import com.ohussar.Util.Vector2i;
import com.ohussar.Window.Components.Background;
import com.ohussar.Window.Components.Button;
import com.ohussar.Window.Components.CloseButton;
import com.ohussar.Window.Components.Label;
import com.ohussar.Window.Components.ProgressBar;
import com.ohussar.Window.Graphics.Images;

import javax.swing.*;
import javax.xml.xpath.XPathEvaluationResult;
import java.awt.*;

public class Window {

    public static JFrame frame;
    public static JFrame popup;
    public static Dimension frameSize = new Dimension(256 * Renderer.scaleFactor, 256 * Renderer.scaleFactor);
    public static Dimension popupSize = new Dimension(128 * Renderer.scaleFactor, 48 * Renderer.scaleFactor);
    public static Dimension buttonSize = new Dimension(64 * Renderer.scaleFactor, 16 * Renderer.scaleFactor);

    public static int count = 0;

    private static ProgressBar progressBar;

    public static void Init(){
        Images.Init();
        frame = new JFrame();
        frame.setPreferredSize(frameSize);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setUndecorated(true);
        frame.setResizable(false);
        frame.setLayout(null);

        Button button = new Button(frame, Vector2i.zero(),"Jogar");
        button.setPosition(new Coordinate().centerX(button.getPreferredSize().width).offset(0, frameSize.height - 20*Renderer.scaleFactor).get());
        button.onPress(Forge::startProcedure);


        new CloseButton(frame);
        new Background(frame, Images.backgroundImage, frameSize);

        frame.setVisible(true);
        frame.pack();
        frame.setLocationRelativeTo(null);
        createPopup(null);
    }

    public static void createPopup(Object obj){
        popup = new JFrame();
        popup.setPreferredSize(popupSize);
        popup.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        popup.setUndecorated(true);
        popup.setResizable(false);
        popup.setLayout(null);


        progressBar = new ProgressBar(popup, new Coordinate(popupSize).centerX(ProgressBar.size.width).centerY().get());
        progressBar.setMaximum(500);
        Label label = new Label(
                popup,
                new Coordinate(popup).get(),
                "AAAAAAAAAAAAAAAAAAAA");
        label.setPosition(new Coordinate(popupSize)
                .centerX(label.getPreferredSize().width)
                .centerY().
                offset(0, -16*Renderer.scaleFactor)
                .get()
        );


        new Background(popup, Images.popupImage, popupSize);

        popup.setVisible(true);
        popup.pack();
        popup.setLocationRelativeTo(null);
    }
    public static void closePopup(){
        popup.dispose();
        progressBar = null;
    }

    public static void updateProgressBar(){
        progressBar.repaint();
    }
    public static void incrementProgressBarValue(int amount)
    {
        progressBar.setValue(progressBar.getValue() + amount);
        updateProgressBar();
    }

    public static void setProgressBarMaxValue(int value){
        progressBar.setMaximum(value);
        updateProgressBar();
    }

}
