package com.example.a4basics;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Rubberband {
    double x1, y1, x2, y2;

    public Rubberband(double x1, double y1, double x2, double y2){
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;

    }


    public boolean isContained(double x, double y) {
        return x >= x1 && x <= x2 && y >= y1 && y <= y2;
    }


    public void setEndPoints(double x2, double y2){
        this.x2 = x2;
        this.y2 = y2;
    }

}
