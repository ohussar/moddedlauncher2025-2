package com.ohussar.Window.Components;

import com.ohussar.Util.Vector2i;
import com.ohussar.Window.Graphics.Images;
import com.ohussar.Window.Renderer;

import javax.swing.*;
import java.awt.*;

public class CloseButton  extends JButton {

    private Dimension dimension = new Dimension(16 * Renderer.scaleFactor, 16 * Renderer.scaleFactor);

    public CloseButton(JFrame frame) {
        super();
        this.setFocusable(false);
        this.setBorderPainted(false);
        this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        this.setSize(this.getPreferredSize());
        this.setVisible(true);
        this.setLocation(239 * Renderer.scaleFactor, 2 * Renderer.scaleFactor);

        this.addActionListener(e -> {
            System.exit(0);
        });
        
        frame.add(this);
    }



    public void paintComponent(Graphics g) {
        this.getParent().repaint();
        if(this.getMousePosition() != null) {
            Renderer.renderImage(g, Images.closeButtonHoverImage, Vector2i.zero(), dimension, this);
        }else{
            Renderer.renderImage(g, Images.closeButtonImage, Vector2i.zero(), dimension, this);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return dimension;
    }
}
