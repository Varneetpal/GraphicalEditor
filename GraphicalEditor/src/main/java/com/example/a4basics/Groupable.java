package com.example.a4basics;

import java.util.ArrayList;

public interface Groupable {
    boolean hasChildren();

    ArrayList<Groupable> getChildren();

    boolean contains(double x, double y);

    void move(double dx, double dy);

    //For the bounding box
    double getLeft();

    double getRight();

    double getTop();

    double getBottom();

    double getCenterX();

    double getCentreY();

    double[] getXs();

    double[] getYs();

    Groupable duplicate();

    void rotate(double a);


}
