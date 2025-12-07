package com.ohussar.Window.Components;

import com.ohussar.Util.Vector2i;
import com.ohussar.Window.Renderer;

import javax.swing.*;
import java.awt.*;

public class Label extends JLabel {
    private Vector2i position;
    private Float fontSize = 16f;
    private Vector2i offset = new Vector2i(0, (int)(11*Renderer.scaleFactor * (fontSize / Renderer.defaultFontSize)));
    public Label(JFrame frame, Vector2i position, String text) {
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
        Renderer.renderString(g, getText(), Color.white, getPreferredSize(),  offset, this);
    }
    public void setPosition(Vector2i position){
        this.setLocation(position.x, position.y);
        this.position = position;
    }

    public void setFontSize(float fontSize){
        this.fontSize = fontSize;
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
