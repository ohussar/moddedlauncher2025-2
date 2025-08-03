package com.ohussar.Util;

public class Vector2i {
    public int x;
    public int y;
    public Vector2i(int x, int y){
        this.x = x;
        this.y = y;
    }
    public static Vector2i zero(){
        return new Vector2i(0, 0);
    }
}
