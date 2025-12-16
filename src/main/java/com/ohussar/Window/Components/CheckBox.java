package com.ohussar.Window.Components;

import com.ohussar.Util.Vector2i;
import com.ohussar.Window.Graphics.Images;
import com.ohussar.Window.Renderer;

import javax.swing.*;
import java.awt.*;

public class CheckBox extends JCheckBox {

    public Vector2i position;

    public CheckBox(Container frame, String text){
        super(text);
        this.setFocusable(false);
        this.setBorderPainted(false);
        this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        int width = this.getFontMetrics(Renderer.mainFont).stringWidth(getText());

        Dimension finalSize = new Dimension(size.width + width + 4 * Renderer.scaleFactor, size.height);

        this.setSize(finalSize);
        this.setVisible(true);

        frame.add(this);
    }


    public void setPosition(Vector2i position){
        this.setLocation(position.x, position.y);
        this.position = position;
    }

    public static Dimension size = new Dimension(12 * Renderer.scaleFactor, 12 * Renderer.scaleFactor);
    @Override
    protected void paintComponent(Graphics g) {
        this.getParent().repaint();
        if(this.getMousePosition() != null){
            Renderer.renderImage(g, Images.checkBoxHover, Vector2i.zero(), size, null);
        }else{
            Renderer.renderImage(g, Images.checkBox, Vector2i.zero(), size, null);
        }

        if(this.isSelected()){
            Renderer.renderImage(g, Images.marked, Vector2i.zero(), size, null);
        }

        Renderer.renderString(g, getText(), Color.WHITE, getSize(),
                new Vector2i(6 * Renderer.scaleFactor, 11 * Renderer.scaleFactor), null);


    }
}
