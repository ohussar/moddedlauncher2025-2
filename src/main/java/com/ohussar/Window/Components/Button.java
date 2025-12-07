package com.ohussar.Window.Components;

import com.ohussar.Util.Vector2i;
import com.ohussar.Window.Graphics.Images;
import com.ohussar.Window.Renderer;
import com.ohussar.Window.Window;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

public class Button extends JButton{
    private Vector2i position;
    private final Vector2i offset = new Vector2i(0, 11 * Renderer.scaleFactor);

    private Trigger buttonPress = (obj) -> {};
    private Boolean locked = false;

    boolean clicked = false;
    private float fontSize = Renderer.defaultFontSize;

    public Button(JFrame frame, Vector2i position, String text){
        super();
        this.position = position;
        this.setFocusable(false);
        this.setBorderPainted(false);
        this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        this.setText(text);
        this.setSize(this.getPreferredSize());
        this.setLocation(position.x, position.y);
        this.setVisible(true);
        this.addActionListener(e -> {
            if(!locked) {
                buttonPress.trigger(null);
            }
        });
        frame.add(this);
    }

    @Override
    protected void processMouseEvent(MouseEvent e) {
        super.processMouseEvent(e);
        if(e.getID() == MouseEvent.MOUSE_PRESSED){
            clicked = true;
        }
        if(e.getID() == MouseEvent.MOUSE_RELEASED || e.getID() == MouseEvent.MOUSE_EXITED){
            clicked = false;
        }
    }

    public void onPress(Trigger buttonPress)
    {
        this.buttonPress = buttonPress;
    }

    public void setLocked(Boolean locked) {
        this.locked = locked;
        if(locked) {
            this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }else{
            this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }
    }

    public void setPosition(Vector2i position){
        this.setLocation(position.x, position.y);
        this.position = position;
    }

    @Override
    public void paintComponent(Graphics g) {
        this.getParent().repaint();
        Color color = Color.white;

        if(clicked){
            fontSize -= 0.5f;
            if(fontSize < (((float) 2 /3) * Renderer.defaultFontSize)){
                fontSize = ((float) 2/3) * Renderer.defaultFontSize;
            }
            color = Color.GRAY;
        }else{
            fontSize += 0.5f;
            if(fontSize > Renderer.defaultFontSize){
                fontSize = Renderer.defaultFontSize;
            }
        }


        if(!locked) {
            if (this.getMousePosition() != null) {
                Renderer.render9Slice(g, Images.buttonHover, getPreferredSize(), this);
            } else {
                Renderer.render9Slice(g, Images.button, getPreferredSize(), this);
            }
        }else{
            Renderer.render9Slice(g, Images.buttonLocked, getPreferredSize(), this);
            color = Color.DARK_GRAY;
        }
        Dimension size = getPreferredSize();
        Renderer.setFontSizePreference(fontSize);
        Vector2i newOffset = new Vector2i((int) (offset.x * (fontSize/Renderer.defaultFontSize)), (int) (offset.y * (fontSize/Renderer.defaultFontSize)));
        Renderer.renderString(g, getText(), color, size, newOffset, this);
    }
    @Override
    public Dimension getPreferredSize() {

        int width = this.getFontMetrics(Renderer.mainFont).stringWidth(getText());
        int margin = 4 * 2 * Renderer.scaleFactor;

        if(Window.buttonSize.width - (width + margin) < 0){
            return new Dimension(width + margin, Window.buttonSize.height);
        }

        return Window.buttonSize;
    }
}
