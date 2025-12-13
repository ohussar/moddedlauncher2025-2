package com.ohussar.Window;

import com.ohussar.Launcher.Config;
import com.ohussar.Launcher.Loader;
import com.ohussar.Launcher.StartProcedure;
import com.ohussar.Main;
import com.ohussar.Util.IntFilter;
import com.ohussar.Util.Vector2i;
import com.ohussar.Window.Components.*;
import com.ohussar.Window.Components.Button;
import com.ohussar.Window.Components.Label;
import com.ohussar.Window.Graphics.Images;

import javax.swing.*;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.io.File;
import java.util.Arrays;

public class Window {

    public static JFrame frame;
    public static JInternalFrame popup;
    public static JInternalFrame configWindow = new JInternalFrame();
    public static JInternalFrame alert = new JInternalFrame();

    public static Dimension frameSize = new Dimension(256 * Renderer.scaleFactor, 256 * Renderer.scaleFactor);
    public static Dimension popupSize = new Dimension(128 * Renderer.scaleFactor, 48 * Renderer.scaleFactor);
    public static Dimension configSize = new Dimension(224 * Renderer.scaleFactor, 224 * Renderer.scaleFactor);
    public static Dimension alertSize = popupSize;

    public static Dimension buttonSize = new Dimension(64 * Renderer.scaleFactor, 16 * Renderer.scaleFactor);

    public static Button playButton;
    public static Button configButton;
    public static Button alertButton;

    private static ProgressBar progressBar;
    public static ProgressBar startGameBar;

    public static InputText ramInput;
    public static InputText usernameInput;

    private static Label usernameTitle;
    public static Label directoryInfo;
    public static Label startGameBarLabel;
    public static Label popupLabel;
    public static Label alertLabel;


    public static Background gif;

    public static int yOffStartGame = 0;

    private static final BarLabel[] downloadThreadsBar = new BarLabel[Main.downloadThreads];

    public static boolean isInitialized = false;

    public static String informationText = "";

    public static void initializeWindow(){
        if(!isInitialized) {
            SplashScreen.frame.dispose();
            Window.Init();
            Config.loadConfigFile();
            Loader.Init();
            Window.updateDirectoryInfo();
            isInitialized = true;
        }
    }


    public static void Init(){

        frame = new JFrame();
        frame.setPreferredSize(frameSize);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setUndecorated(true);
        frame.setResizable(false);
        frame.setLayout(null);
        frame.pack();
        initAlert();
        createConfigWindow(null);
        createPopup(null);


        gif = new Background(frame, Images.hampter, new Dimension(Images.hampter.getIconWidth(), Images.hampter.getIconHeight()),
                new Coordinate(frameSize)
                        .centerX(Images.hampter.getIconWidth())
                        .centerY(Images.hampter.getIconHeight())
                        .offset(0, 32)
                        .get()
        );


        int downY = frameSize.height - 20*Renderer.scaleFactor;

        playButton = new Button(frame, Vector2i.zero(),"Jogar");
        playButton.setPosition(new Coordinate().centerX(playButton.getPreferredSize().width).offset(0, downY).get());
        playButton.onPress(StartProcedure::startProcedure);
        usernameTitle = new Label(frame,
                new Coordinate().get()
                , "Nome de usuário: ");
        usernameTitle.setPosition(
                new Coordinate()
                        .offset(4 * Renderer.scaleFactor,
                                downY - usernameTitle.getPreferredSize().height)
                        .get()
        );

        usernameInput = new InputText(frame,
                new Coordinate()
                        .offset(3 * Renderer.scaleFactor,
                                downY )
                        .get()
                , "Username");
        usernameInput.setSize(90 * Renderer.scaleFactor, buttonSize.height);
        usernameInput.setEditable(false);
        usernameInput.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        usernameInput.setEnabled(true);
        usernameInput.setFocusable(true);
        usernameInput.setTrigger((Object obj) ->{
            Config.updateUsername((String) obj);
        });

        configButton = new Button(frame, Vector2i.zero(),"Configurações");
        configButton.setPosition(
                new Coordinate()
                        .centerX(playButton.getPreferredSize().width)
                        .offset(3 * Renderer.scaleFactor + playButton.getPreferredSize().width, downY)
                        .get());
        configButton.onPress(Window::setConfigVisible);


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
        yOffStartGame = ProgressBar.size.height + 2 * Renderer.scaleFactor;
        startGameBarLabel = new Label(frame, Vector2i.zero(), "AAAAAAAAAAAAAAAAAAAAAAAAA");
        startGameBarLabel.setPosition(
                new Coordinate()
                        .offset(5 * Renderer.scaleFactor, frameSize.height - yOffStartGame + Renderer.scaleFactor + 1)
                        .get()
        );
        startGameBarLabel.setVisible(false);
        startGameBar = new ProgressBar(frame, Vector2i.zero(), new Dimension(250 * Renderer.scaleFactor, ProgressBar.size.height));
        startGameBar.setPosition(
                new Coordinate(frameSize)
                        .centerX(startGameBar.sizeInt.width)
                        .offset(0, frameSize.height - yOffStartGame)
                        .get()
        );
        startGameBar.setVisible(false);
        startGameBar.setMaximum(1000);


        Label label = new Label(frame, Vector2i.zero(), "");

        label.setSize(frameSize);

        label.setText("<html>" + informationText + "</html>");

        label.setPosition(
                new Coordinate()
                        .offset(0, -60 * Renderer.scaleFactor)
                        .get()
        );

        Dimension size =  new Dimension(640, 360);


       Background back = new Background(frame, Images.title, size,
                new Coordinate(frameSize)
                        .centerX(size.width)
                        .offset(0, -75)
                        .get()
        );
       back.setOpaque(false);
       back.setEnabled(false);
       back.setFocusable(false);
       new CloseButton(frame);

       new Background(frame, Images.backgroundImage, frameSize);

        frame.setVisible(true);
        frame.pack();
        frame.setLocationRelativeTo(null);


        frame.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(!e.getComponent().equals(usernameInput)){
                    usernameInput.setEditable(false);
                    usernameInput.typing = false;
                }

            }

            @Override
            public void mousePressed(MouseEvent e) {
                if(!e.getComponent().equals(usernameInput)){
                    usernameInput.setEditable(false);
                    usernameInput.typing = false;
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                for( Component comp : frame.getContentPane().getComponents()){
                    if(comp instanceof Button btn){
                        if(btn.contains(e.getLocationOnScreen())){
                            btn.getModel().setRollover(true);
                        }else{
                            btn.getModel().setRollover(false);
                        }
                    }
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        Thread windowWatcher = new Thread(() -> {
            boolean triggered = false;
           while(true){
               try {
                   Thread.sleep(25);
               } catch (InterruptedException e) {
                   throw new RuntimeException(e);
               }
               if(!configWindow.isVisible() && triggered){
                   triggered = false;
                   Component[] components = frame.getContentPane().getComponents();
                   for(Component c : components){
                       if(c instanceof Button btn){
                           btn.setLocked(false);
                       }else if(c instanceof InputText inp){
                           inp.setLocked(false);
                       }else if(c instanceof CloseButton close){
                           close.setEnabled(true);
                       }
                   }

               }
               if(configWindow.isVisible() && !triggered){
                   triggered = true;

                   Component[] components = frame.getContentPane().getComponents();
                   for(Component c : components){
                       if(c instanceof Button btn){
                           btn.setLocked(true);
                       }else if(c instanceof InputText inp){
                           inp.setLocked(true);
                       }else if(c instanceof CloseButton close){
                           close.setEnabled(false);
                       }
                   }

               }



           }
        });
        windowWatcher.start();

    }


    public static JInternalFrame launcherCustomFrame(Dimension size){
        JInternalFrame f = new JInternalFrame();
        f.setPreferredSize(size);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setResizable(false);
        f.setLayout(null);

        f.getRootPane().setWindowDecorationStyle(JRootPane.NONE);
        f.setBorder(null);
        ((BasicInternalFrameUI) f.getUI()).setNorthPane(null);
        return f;
    }

    public static void createConfigWindow(Object obj){
        configWindow = launcherCustomFrame(configSize);
        configWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        //configWindow.setAlwaysOnTop(true);
        Vector2i loc = new Coordinate(frame).centerX(configSize.width).centerY(configSize.height).get();
        configWindow.setLocation(loc.x, loc.y);
        configWindow.pack();

        Label label = new Label(configWindow,
                new Coordinate()
                        .offset(3 * Renderer.scaleFactor, 0)
                        .get(),
                "Memória RAM: "
        );

        label.setFontSize(Renderer.defaultFontSize);

        label.setPosition(
                new Coordinate()
                        .offset(4 * Renderer.scaleFactor,
                                19 * Renderer.scaleFactor + label.getPreferredSize().height/2 - 1 * Renderer.scaleFactor)
                        .get()
        );

        ramInput = new InputText(configWindow,
                new Coordinate()
                        .offset(4 * Renderer.scaleFactor + label.getPreferredSize().width + 2 * Renderer.scaleFactor,
                                19 * Renderer.scaleFactor)
                        .get()
                , "4096");
        ramInput.setSize(90 * Renderer.scaleFactor, buttonSize.height);
        ramInput.setEditable(false);
        ramInput.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        ramInput.setEnabled(true);
        ramInput.setFocusable(true);
        ramInput.setTrigger((Object o) -> {
            if(!((String) o).isEmpty()) {
                Config.updateRam(Integer.parseInt((String) o));
            }else{
                Config.updateRam(0);
            }
        });
        ((PlainDocument) ramInput.getDocument()).setDocumentFilter(new IntFilter());

        Button btn = new Button(configWindow, Vector2i.zero(), "Alterar diretório");
        int y= 22 * Renderer.scaleFactor + ramInput.getPreferredSize().height;
        btn.setPosition(new Vector2i(
                3 * Renderer.scaleFactor,
                y)
        );

        btn.onPress((a) -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int result = fileChooser.showOpenDialog(null);
            if(result == JFileChooser.APPROVE_OPTION){
                File selected = fileChooser.getSelectedFile();
                if(selected.getAbsolutePath().equals(System.getProperty("user.dir"))){
                    directoryInfo.setText("Diretório: Padrão");
                    directoryInfo.setSize(directoryInfo.getPreferredSize());
                    Config.updateRootPath("");
                }else{
                    directoryInfo.setText("Diretório: " + selected.getAbsolutePath());
                    directoryInfo.setSize(directoryInfo.getPreferredSize());
                    Config.updateRootPath(selected.getAbsolutePath());
                }
            }else{
                //directoryInfo.setText("Diretório: Padrão");
                //Config.updateRootPath("");
            }
        });

        directoryInfo = new Label(configWindow, Vector2i.zero(), "ABCDEDEDEDE");
        directoryInfo.setPosition(
                new Vector2i(5 * Renderer.scaleFactor,
                         y + btn.getPreferredSize().height + Renderer.scaleFactor
                        )
        );
        updateDirectoryInfo();

        new CloseButton(configWindow);
        new Background(configWindow, Images.backgroundImage, configSize);

        configWindow.setVisible(false);
        configWindow.pack();
        frame.add(configWindow);

        //configWindow.setLocationRelativeTo(null);
    }

    public static void updateDirectoryInfo(){
        if(Config.rootPath.equals("default")){
            directoryInfo.setText("Diretório: Padrão");
            directoryInfo.setSize(directoryInfo.getPreferredSize());
        }else{
            directoryInfo.setText("Diretório: " + Config.rootPath);
            directoryInfo.setSize(directoryInfo.getPreferredSize());
        }
    }


    public static void setConfigVisible(Object obj){
        configWindow.setVisible(true);
    }
    public static void setPopupVisible(Object obj){
        popup.setVisible(true);
    }
    public static void hidePopup(Object obj){
        popup.setVisible(false);
    }

    public static void initAlert(){
        alert = launcherCustomFrame(alertSize);
        alert.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        Vector2i loc = new Coordinate(frame).centerX(alertSize.width).centerY(alertSize.height).get();
        alert.setLocation(loc.x, loc.y);
        alert.pack();
        alertLabel = new Label(alert, Vector2i.zero(), "AAAAAAAAAAAAAAAAAAAA");
        alertLabel.setPosition(
                new Coordinate(alertSize)
                        .centerX(alertLabel.getPreferredSize().width)
                        .offset(0, alertLabel.getPreferredSize().height + 8 * Renderer.scaleFactor)
                        .get()
        );
        alertButton = new Button(alert, Vector2i.zero(), "OK");
        alertButton.overrideMinimumSize = true;
        alertButton.setSize(alertButton.getPreferredSize());
        alertButton.setPosition(new Coordinate(alertSize)
                .centerX(alertButton.getPreferredSize().width)
                .offset(0, alertSize.height - alertButton.getPreferredSize().height - 4 * Renderer.scaleFactor)
                .get()
        );
        alertButton.onPress((obj) -> {
            alert.setVisible(false);
        });
        new Background(alert, Images.popupImage, alertSize);

        alert.setVisible(false);
        alert.pack();
        frame.add(alert);
    }


    public static void createrAlert(String message){

        alertLabel.setText("<html>" +message+ "</html>");
        Dimension size = new Dimension(alertSize.width - 8 * Renderer.scaleFactor, alertLabel.getPreferredSize().height*3);
        alertLabel.setSize(size);

        int height = Renderer.getMultilineHeight("<html>" +message+ "</html>", size, Label.offset);
        int yoff = -6*Renderer.scaleFactor;
        if(height == 35) {
            yoff = -8*Renderer.scaleFactor;
        }
        alertLabel.setPosition(
                new Coordinate(alertSize)
                        .centerX(size.width)
                        .centerY(height)
                        .offset(0, yoff)
                        .get()
        );
        alertLabel.repaint();
        alert.setVisible(true);
    }



    public static void createPopup(Object obj){
        popup = launcherCustomFrame(popupSize);
        popup.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        //configWindow.setAlwaysOnTop(true);
        Vector2i loc = new Coordinate(frame).centerX(popupSize.width).centerY(popupSize.height).get();
        popup.setLocation(loc.x, loc.y);
        popup.pack();
        progressBar = new ProgressBar(popup, new Coordinate(popupSize).centerX(ProgressBar.size.width).centerY().get());
        progressBar.setMaximum(500);
        popupLabel = new Label(
                popup,
                new Coordinate(popup).get(),
                "AAAAAAAAAAAAAAAAAAAA");
        popupLabel.setPosition(new Coordinate(popupSize)
                .centerX(popupLabel.getPreferredSize().width)
                .centerY().
                offset(0, -16*Renderer.scaleFactor)
                .get()
        );

        new Background(popup, Images.popupImage, popupSize);

        popup.setVisible(false);
        popup.pack();
        frame.add(popup);
        //popup.setLocationRelativeTo(null);
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
        smoothBarUpdate(progressBar, amount);
        //progressBar.setValue(progressBar.getValue() + amount);
        updateProgressBar();
    }

    public static void setPopLabelText(String text){
        popupLabel.setText(text);
        popupLabel.setSize(popupLabel.getPreferredSize());
    }

    public static void setProgressBarMaxValue(int value){
        progressBar.setMaximum(value);
        updateProgressBar();
    }

    public static void setDownloadThreadsBarVisible(boolean value){
        for(int i = 0; i < Main.downloadThreads; i++){
            setNthDownloadVisible(i, value);
        }
        gif.setVisible(!value);
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
        if(!value) {
            boolean pp = true;
            for (int i = 0; i < Main.downloadThreads; i++) {
                if(downloadThreadsBar[i].progressBar.isVisible()){
                    pp = false;
                }
            }
            if(pp){
                gif.setVisible(true);
            }
        }

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
    public static int[] progressPerPhase = new int[2];
    public static int[] progress = new int[2];
    public static void beginNewPhase(int phase){
        int progressPerPhase = startGameBar.getMaximum() / 2;
        startGameBar.setValue(progressPerPhase * phase);
        startGameBar.repaint();
        progress[phase] = 0;

        if(phase == 0){
            startGameBarLabel.setText("Baixando Forge");
            startGameBarLabel.setSize(startGameBarLabel.getPreferredSize());
        }
        if(phase == 1){
            startGameBarLabel.setText("Baixando mods: 0/0");
            startGameBarLabel.setSize(startGameBarLabel.getPreferredSize());
        }

    }
    public static void setMaxProgressPhase(int phase, int max){
        progressPerPhase[phase] = max;
        if(phase==1){
            startGameBarLabel.setText("Baixando mods: 0/"+max);
            startGameBarLabel.setSize(startGameBarLabel.getPreferredSize());
        }
    }
    public static void updateStartGamePhaseProgress(int startGamePhase, int increase){
        int max = progressPerPhase[startGamePhase];
        progress[startGamePhase] += increase;
        float percentage = (float) progress[startGamePhase] / progressPerPhase[startGamePhase];

        int maxInBar = startGameBar.getMaximum() / 2;

        startGameBar.setValue(maxInBar * startGamePhase + (int)(maxInBar * percentage));
        startGameBar.repaint();
        if(startGamePhase == 0){
            int newP = (int) (percentage * 100);
            startGameBarLabel.setText("Baixando Forge: "+newP+"%");
            startGameBarLabel.setSize(startGameBarLabel.getPreferredSize());
        }
        if(startGamePhase==1){
            startGameBarLabel.setText("Baixando mods: "+progress[startGamePhase]+"/"+max);
            startGameBarLabel.setSize(startGameBarLabel.getPreferredSize());
        }

    }

    public static void offsetButtonsWhenPlayButtonPressed(boolean up){
        Component[] list = {Window.playButton, Window.usernameInput, Window.configButton, Window.usernameTitle};
        Window.startGameBar.setVisible(up);
        Window.startGameBarLabel.setVisible(up);
        int mult = 1;
        if(!up){
            mult = -1;
        }

        for(Component comp : list){

            Vector2i position =
                    new Vector2i(comp.getLocation().x, comp.getLocation().y - (Window.yOffStartGame - 2 * Renderer.scaleFactor)* mult);

            comp.setLocation(position.x, position.y);
        }

    }

}
