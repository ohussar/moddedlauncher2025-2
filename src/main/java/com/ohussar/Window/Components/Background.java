package com.ohussar.Window.Components;

import com.ohussar.Util.Vector2i;
import com.ohussar.Window.Graphics.Images;
import com.ohussar.Window.Renderer;
import com.ohussar.Window.Window;

import javax.swing.*;
import java.awt.*;

public class Background extends JComponent {
    private Image image;
    private Dimension size;
    public Background(JFrame frame, Image image, Dimension dimension) {
        this.setBounds(0, 0, dimension.width, dimension.height);
        this.image = image;
        this.size = dimension;
        frame.add(this);
    }

    @Override
    public void paint(Graphics g) {
        Renderer.renderImage(g, this.image, Vector2i.zero(), this.size, this);
    }
}
