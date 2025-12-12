package com.ohussar.Window.Components;

import com.ohussar.Util.Vector2i;
import com.ohussar.Window.Graphics.Images;
import com.ohussar.Window.Graphics.NineSlice;
import com.ohussar.Window.Renderer;
import com.ohussar.Window.Window;

import javax.swing.*;
import java.awt.*;

public class Background extends JComponent {
    private Image image;
    private Dimension size;
    private NineSlice slice;
    private Vector2i position;

    public Background(Container frame, Image image, Dimension dimension) {
        this.setBounds(0, 0, dimension.width, dimension.height);
        this.image = image;
        this.size = dimension;

        frame.add(this);
    }
    public Background(Container frame, NineSlice image, Dimension dimension) {
        this.setBounds(0, 0, dimension.width, dimension.height);
        this.size = dimension;
        this.slice = image;
        frame.add(this);
    }

    public Background(Container frame, Image image, Dimension dimension, Vector2i position) {

        this.image = image;
        this.size = dimension;
        this.position = position;
        this.setSize(dimension);
        this.setLocation(position.x, position.y);
        frame.add(this);
    }


    public Background(Container frame, NineSlice image, Dimension dimension, Vector2i position) {

        this.size = dimension;
        this.position = position;
        this.setSize(dimension);
        this.slice = image;
        this.setLocation(position.x, position.y);
        frame.add(this);
    }



    @Override
    public boolean contains(int x, int y) {
        return false;
    }

    @Override
    public void paint(Graphics g) {
        if(slice != null){
            Renderer.render9Slice(g, slice, size, this);
            return;
        }

        if(position == null) {
            Renderer.renderImage(g, this.image, Vector2i.zero(), this.size, this);
        }else{
            Renderer.renderImage(g, this.image, Vector2i.zero(), this.size, this);
        }
    }
}
