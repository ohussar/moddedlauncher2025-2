package com.ohussar.Window.Components;

import com.ohussar.Util.Vector2i;
import com.ohussar.Window.Graphics.Images;
import com.ohussar.Window.Renderer;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ProgressBar extends JProgressBar {

    public static Dimension size = new Dimension(88 * Renderer.scaleFactor,10 * Renderer.scaleFactor);

    public ProgressBar(JFrame frame, Vector2i position) {
        this.setFocusable(false);
        this.setBorderPainted(false);
        this.setSize(this.getPreferredSize());
        this.setLocation(position.x, position.y);
        this.setVisible(true);
        frame.add(this);
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(Images.progressBarFrameImage, 0, 0, size.width, size.height, this);

        if (getValue() > 0) {

            int width = (int) ( ( (float)(size.width / Renderer.scaleFactor) * ( (float) getValue() / (float) getMaximum())));
            if (width != 0) {
                BufferedImage cropped = Images.progressBarCenterImage.getSubimage(0, 0, width, 10);
                g.drawImage(cropped, 0, 0, width * Renderer.scaleFactor, 10 * Renderer.scaleFactor, this);
            }
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return size;
    }
}