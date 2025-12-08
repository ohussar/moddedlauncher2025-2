package com.ohussar.Window.Components;

import com.ohussar.Util.Vector2i;
import com.ohussar.Window.Graphics.Images;
import com.ohussar.Window.Renderer;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;

public class InputText extends JTextField {
    public boolean typing = false;
    private Vector2i position;
    private final Vector2i offset = new Vector2i(0, 11 * Renderer.scaleFactor);
    private boolean locked = false;
    private int count = 0;
    private boolean cursor = false;
    private boolean isFirst = false;
    public Trigger onTypeTrigger;

    public InputText(Container frame, Vector2i position, String text){
        super();

        this.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                changedUpdate(e);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                changedUpdate(e);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                //Config.saveUsername(username);
            }
        });
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    if(onTypeTrigger != null) {
                        onTypeTrigger.trigger(getText());
                    }
                    typing = false;
                    setEditable(false);
                }
            }
        });

        Thread t = new Thread(() -> {
            while(true) {
                try {
                    Thread.sleep(100);
                    count++;
                    if(count>5) {
                        count = 0;
                        cursor = !cursor;
                    }

                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

        });
        t.start();

        this.position = position;
        this.setFocusable(false);
        this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        this.setText(text);
        this.setSize(this.getPreferredSize());
        this.setLocation(position.x, position.y);
        this.setVisible(true);
        frame.add(this);
    }
    public void setFirst(boolean b){
        isFirst = b;
    }

    public void setTrigger(Trigger trigger){
        onTypeTrigger = trigger;
    }

    public void setLocked(boolean f){
        locked = f;
        if(locked) {
            this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }else{
            this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }
    }



    @Override
    protected void processMouseEvent(MouseEvent e) {
        super.processMouseEvent(e);
        if(!locked) {
            if (e.getButton() == 1) {
                if (getMousePosition() != null) {
                    if(isFirst){
                        this.setText("");
                        isFirst = false;
                    }
                    this.setEditable(true);
                    cursor = true;
                    typing = true;
                    setCaretPosition(getText().length());
                    count = 0;
                }
            }
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        this.getParent().repaint();
        Color color = Color.white;
        if(!typing && !locked) {
            if (this.getMousePosition() != null) {
                com.ohussar.Window.Renderer.render9Slice(g, Images.inputHover, getPreferredSize(), this);
            } else {
                com.ohussar.Window.Renderer.render9Slice(g, Images.input, getPreferredSize(), this);
            }
        }else{
            com.ohussar.Window.Renderer.render9Slice(g, Images.inputHover, getPreferredSize(), this);
            color = Color.GRAY;
        }

        if(locked){
            Renderer.render9Slice(g, Images.buttonLocked, getPreferredSize(), this);
            color = Color.DARK_GRAY;
            Renderer.renderStringInput(g, getText(), color, getPreferredSize(), offset, this);
            return;
        }

        String f = getText();
        if(cursor && isEditable()){
            f = f + "|";
        }
        Renderer.renderStringInput(g, f, color, getPreferredSize(), offset, this);
    }

    @Override
    public Dimension getPreferredSize() {
        return getSize();
    }
}
