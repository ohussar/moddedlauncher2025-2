package com.ohussar.Window;

import com.ohussar.Launcher.StartProcedure;
import com.ohussar.Main;
import com.ohussar.Util.Vector2i;
import com.ohussar.Window.Components.*;
import com.ohussar.Window.Components.Button;
import com.ohussar.Window.Components.Label;
import com.ohussar.Window.Graphics.Images;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Window {

    public static JFrame frame;
    public static JFrame popup;

    public static JFrame configWindow;

    public static Dimension frameSize = new Dimension(256 * Renderer.scaleFactor, 256 * Renderer.scaleFactor);
    public static Dimension popupSize = new Dimension(128 * Renderer.scaleFactor, 48 * Renderer.scaleFactor);
    public static Dimension buttonSize = new Dimension(64 * Renderer.scaleFactor, 16 * Renderer.scaleFactor);

    public static int count = 0;

    private static ProgressBar progressBar;

    private static final BarLabel[] downloadThreadsBar = new BarLabel[Main.downloadThreads];


    public static void Init(){
        Images.Init();
        frame = new JFrame();
        frame.setPreferredSize(frameSize);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setUndecorated(true);
        frame.setResizable(false);
        frame.setLayout(null);

        int downY = frameSize.height - 20*Renderer.scaleFactor;

        Button buttonPlay = new Button(frame, Vector2i.zero(),"Jogar");
        buttonPlay.setPosition(new Coordinate().centerX(buttonPlay.getPreferredSize().width).offset(0, downY).get());
        buttonPlay.onPress(StartProcedure::startProcedure);
        Label usernameTitle = new Label(frame,
                new Coordinate().get()
                , "Nome de usuário: ");
        usernameTitle.setPosition(
                new Coordinate()
                        .offset(4 * Renderer.scaleFactor,
                                downY - usernameTitle.getPreferredSize().height)
                        .get()
        );

        InputText username = new InputText(frame,
                new Coordinate()
                        .offset(3 * Renderer.scaleFactor,
                                downY )
                        .get()
                , "Username");
        username.setSize(90 * Renderer.scaleFactor, buttonSize.height);
        username.setEditable(false);
        username.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        username.setEnabled(true);
        username.setFocusable(true);

        Button buttonConfig = new Button(frame, Vector2i.zero(),"Configurações");
        buttonConfig.setPosition(
                new Coordinate()
                        .centerX(buttonPlay.getPreferredSize().width)
                        .offset(3 * Renderer.scaleFactor + buttonPlay.getPreferredSize().width, downY)
                        .get());
        //buttonConfig.onPress(StartProcedure::startProcedure);


        for (int i = 0; i < Main.downloadThreads; i++){
            ProgressBar bar = new
                    ProgressBar(frame,
                    new Coordinate(frameSize)
                            .centerX(ProgressBar.size.width)
                            .centerY()
                            .offset(0, i*(ProgressBar.size.height+12 * Renderer.scaleFactor))
                            .get()
            );

            Label label = new Label(frame,
                    new Coordinate(frame).get(),
                    "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"
            );
            label.setPosition(
                    new Coordinate(frameSize)
                            .centerX(label.getPreferredSize().width)
                            .centerY()
                            .offset(0, i*(ProgressBar.size.height+12 * Renderer.scaleFactor) - 8 * Renderer.scaleFactor)
                            .get()
            );
            downloadThreadsBar[i] = new BarLabel(bar, label);
            bar.setVisible(false);
            label.setVisible(false);
        }



        new CloseButton(frame);
        new Background(frame, Images.backgroundImage, frameSize);

        frame.setVisible(true);
        frame.pack();
        frame.setLocationRelativeTo(null);


        frame.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(!e.getComponent().equals(username)){
                    username.setEditable(false);
                    username.typing = false;
                }

            }

            @Override
            public void mousePressed(MouseEvent e) {
                if(!e.getComponent().equals(username)){
                    username.setEditable(false);
                    username.typing = false;
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        //createPopup(null);
    }

    public static void createConfigWindow(Object obj){

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

    public static void setDownloadThreadsBarVisible(boolean value){
        for(int i = 0; i < Main.downloadThreads; i++){
            setNthDownloadVisible(i, value);
        }
    }
    public static void setDownloadBarMaxValue(int n, int value){
        downloadThreadsBar[n].progressBar.setMaximum(value);
    }
    public static void updateNthDownloadBarSmooth(int n, int value){
        smoothBarUpdate(downloadThreadsBar[n].progressBar, value);
        //downloadThreadsBar[n].progressBar.setValue(downloadThreadsBar[n].progressBar.getValue() + value);
    }
    public static void updateNthDownloadBar(int n, int value){
        //smoothBarUpdate(downloadThreadsBar[n].progressBar, value);
        downloadThreadsBar[n].progressBar.setValue(downloadThreadsBar[n].progressBar.getValue() + value);
    }
    public static void updateNthDownloadLabel(int n, String name){
        downloadThreadsBar[n].label.setText(name);
        downloadThreadsBar[n].label.setSize(downloadThreadsBar[n].label.getPreferredSize());
        downloadThreadsBar[n].label.setPosition(
                new Coordinate(frameSize)
                        .centerX(downloadThreadsBar[n].label.getPreferredSize().width)
                        .centerY()
                        .offset(0, n*(ProgressBar.size.height+12 * Renderer.scaleFactor) - 8 * Renderer.scaleFactor)
                        .get()
        );
    }
    public static void setNthDownloadVisible(int n, boolean value){
        downloadThreadsBar[n].progressBar.setVisible(value);
        downloadThreadsBar[n].label.setVisible(value);
    }


    public static void smoothBarUpdate(ProgressBar bar, int value){
        int timeTotal = 100;
        int startValue = bar.getValue();
        Thread thread = new Thread(() -> {
            for(int i = 0; i < value/100000; i++){
                bar.setValue(bar.getValue() + 100000);
                try {
                    Thread.sleep(timeTotal / (value/100000));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            bar.setValue(bar.getValue() + value);
            Thread.currentThread().interrupt();
        });
        thread.start();
    }

}
