package com.ohussar.Window.Components;

import com.ohussar.Util.Vector2i;
import com.ohussar.Window.Graphics.Images;
import com.ohussar.Window.Renderer;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ProgressBar extends JProgressBar {
    public static Dimension size = new Dimension(88 * Renderer.scaleFactor,10 * Renderer.scaleFactor);
    public Dimension sizeInt = ProgressBar.size;

    public ProgressBar(Container frame, Vector2i position) {
        this.setFocusable(false);
        this.setBorderPainted(false);
        this.setSize(this.getPreferredSize());
        setPosition(position);
        this.setVisible(true);
        frame.add(this);
    }

    public ProgressBar(Container frame, Vector2i position, Dimension size){
        this(frame, position);
        this.setSize(size);
        this.sizeInt = size;
    }

    public void setPosition(Vector2i position){
        this.setLocation(position.x, position.y);
    }


    @Override
    public void paint(Graphics g) {
        Renderer.render9Slice(g, Images.progressBar, sizeInt, this);
        //g.drawImage(Images.progressBar, 0, 0, size.width, size.height, this);

        if (getValue() > 0) {
            int width = (int) ( ( (float)(sizeInt.width / Renderer.scaleFactor) * ( (float) getValue() / (float) getMaximum())));
            if (width != 0) {
                BufferedImage cropped = Images.progressBarCenterImage.getSubimage(1, 0, 86, 10);
                g.drawImage(cropped.getScaledInstance(width*Renderer.scaleFactor, 10, 0), Renderer.scaleFactor, 0, width * Renderer.scaleFactor, 10 * Renderer.scaleFactor, this);
            }
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return sizeInt;
    }
}