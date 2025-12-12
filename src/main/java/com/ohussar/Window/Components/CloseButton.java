package com.ohussar.Window.Components;

import com.ohussar.Util.Vector2i;
import com.ohussar.Window.Graphics.Images;
import com.ohussar.Window.Renderer;
import com.ohussar.Window.Window;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;

public class CloseButton  extends JButton {

    private final Dimension dimension = new Dimension(16 * Renderer.scaleFactor, 16 * Renderer.scaleFactor);
    private final Container parentFrame;
    public CloseButton(Container frame) {
        super();
        this.setFocusable(false);
        this.setBorderPainted(false);
        this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        this.setSize(this.getPreferredSize());
        this.setVisible(true);
        parentFrame = frame;

        Dimension s = parentFrame.getSize();
        this.setLocation(s.width - dimension.width - Renderer.scaleFactor, 2 * Renderer.scaleFactor);
        this.addActionListener(e -> {
            if(parentFrame instanceof JFrame f) {
                parentFrame.dispatchEvent(new WindowEvent(f, WindowEvent.WINDOW_CLOSING));
            }else if(parentFrame instanceof JInternalFrame f){
                f.setVisible(false);
            }
        });
        
        frame.add(this);
    }



    public void paintComponent(Graphics g) {
        this.getParent().repaint();
        if(this.getModel().isRollover()) {
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
