package com.ohussar.Window.Components;

import com.ohussar.Util.Vector2i;
import com.ohussar.Window.Renderer;

import javax.swing.*;
import java.awt.*;

public class Label extends JLabel {
    private Vector2i position;
    private Float fontSize = 16f;
    public static Vector2i offset = new Vector2i(0, (int)(11*Renderer.scaleFactor * (16 / Renderer.defaultFontSize)));
    public Label(Container frame, Vector2i position, String text) {
        super();
        this.position = position;
        this.setFocusable(false);
        this.setText(text);
        this.setSize(this.getPreferredSize());
        this.setLocation(position.x, position.y);
        this.setVisible(true);
        frame.add(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Renderer.setFontSizePreference(fontSize);
        if(fontSize != 16f){
            Vector2i offset = new Vector2i(0, (int)(11*Renderer.scaleFactor * (16 / Renderer.defaultFontSize)));
            Renderer.renderString(g, getText(), Color.white, getSize(),  offset, this);
        }else {
            Renderer.renderString(g, getText(), Color.white, getSize(), offset, this);
        }
    }
    public void setPosition(Vector2i position){
        this.setLocation(position.x, position.y);
        this.position = position;
    }

    public void setFontSize(float fontSize){
        this.fontSize = fontSize;
        setSize(getPreferredSize());
    }
    @Override
    public Dimension getPreferredSize() {
        //Renderer.setFontSizePreference(8f);
        int width = this.getFontMetrics(Renderer.mainFont.deriveFont(fontSize)).stringWidth(getText());
        int height = this.getFontMetrics(Renderer.mainFont.deriveFont(fontSize)).getHeight();
        //Renderer.setFontSizePreference(Renderer.defaultFontSize);
        return new Dimension(width, height);
    }
}
