package com.ohussar.Window;

import com.ohussar.Util.Vector2i;

import javax.swing.*;
import java.awt.*;

public class Coordinate {

    private Vector2i coordinate;
    private Dimension frameSize;
    public Coordinate(){
        this.coordinate = Vector2i.zero();
        this.frameSize = Window.frameSize;
    }
    public Coordinate(Container frame){
        this.coordinate = Vector2i.zero();
        this.frameSize = frame.getSize();
    }
    public Coordinate(Dimension frameSize){
        this.coordinate = Vector2i.zero();
        this.frameSize = frameSize;
    }
    private Coordinate(Vector2i coordinate){
        this.coordinate = coordinate;
        this.frameSize = Window.frameSize;
    }

    public Coordinate centerX(){
        this.coordinate.x = frameSize.width / 2;
        return this;
    }
    public Coordinate centerX(int width){
        this.coordinate.x = frameSize.width / 2 - width/2;
        return this;
    }

    public Coordinate centerY(){
        this.coordinate.y = frameSize.height / 2;
        return this;
    }
    public Coordinate centerY(int height){
        this.coordinate.y = frameSize.height / 2 - height/2;
        return this;
    }

    public Coordinate offset(int x, int y){
        this.coordinate.x += x;
        this.coordinate.y += y;
        return this;
    }

    public Vector2i get(){
        return this.coordinate;
    }

}
