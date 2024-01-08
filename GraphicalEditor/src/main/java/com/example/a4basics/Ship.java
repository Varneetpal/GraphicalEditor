package com.example.a4basics;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.stream.DoubleStream;

public class Ship implements Groupable{
    double translateX, translateY;
    double[] xs = {0,20,0,-20,0};
    double[] ys = {24,-20,-12,-20,24};
    double shipWidth, shipHeight;
    double[] displayXs, displayYs;
    WritableImage buffer;
    PixelReader reader;
    double clickX, clickY;


    public Ship(double newX, double newY) {
        Canvas shipCanvas;
        GraphicsContext gc;

        translateX = newX;
        translateY = newY;
        double minVal = DoubleStream.of(xs).min().getAsDouble();
        double maxVal = DoubleStream.of(xs).max().getAsDouble();
        shipWidth = maxVal - minVal;
        minVal = DoubleStream.of(ys).min().getAsDouble();
        maxVal = DoubleStream.of(ys).max().getAsDouble();
        shipHeight = maxVal - minVal;
        displayXs = new double[xs.length];
        displayYs = new double[ys.length];
        for (int i = 0; i < displayXs.length; i++) {
            displayXs[i] = xs[i] + shipWidth/2;
            displayYs[i] = ys[i] + shipHeight/2;
        }

        shipCanvas = new Canvas(shipWidth,shipHeight);
        gc = shipCanvas.getGraphicsContext2D();
        gc.setFill(Color.BLACK);
        gc.fillPolygon(displayXs, displayYs, displayXs.length);
        buffer = shipCanvas.snapshot(null,null);
        reader = buffer.getPixelReader();

        for (int i = 0; i < displayXs.length; i++) {
            displayXs[i] = xs[i] + translateX;
            displayYs[i] = ys[i] + translateY;
        }
    }


//    public Groupable duplicate(){
//
//    }

    @Override
    public double[] getXs(){
        return displayXs;
    }

    @Override
    public double[] getYs(){
        return displayYs;
    }

    @Override
    public Groupable duplicate() {
        return new Ship(translateX, translateY);
    }

    public void addShip(Groupable child) {

    }

    @Override
    public boolean hasChildren() {
        return false;
    }

    @Override
    public ArrayList<Groupable> getChildren() {
        return null;
    }

    public boolean contains(double x, double y) {
        clickX = x - translateX + shipWidth/2;
        clickY = y - translateY + shipHeight/2;
        // check bounding box first, then bitmap
        boolean inside = false;
        if (clickX >= 0 && clickX <= shipWidth && clickY >= 0 && clickY <= shipHeight) {
            if (reader.getColor((int) clickX, (int) clickY).equals(Color.BLACK)) inside = true;
        }
        return inside;
    }

    @Override
    public void move(double dx, double dy) {
        for (int i = 0; i < displayXs.length; i++) {
            displayXs[i] += dx;
            displayYs[i] += dy;
        }
        translateX += dx;
        translateY += dy;

    }

    @Override
    public double getLeft() {
        //double minVal = DoubleStream.of(xs).min().getAsDouble();
        double minX1 = translateX - shipWidth/2;
        return minX1;
        //return 0;
    }

    @Override
    public double getRight() {
        //double maxVal = DoubleStream.of(xs).max().getAsDouble();
        double maxX2 = translateX + shipWidth/2;
        return maxX2;
    }

    @Override
    public double getTop() {
        double miny1 = translateY - shipHeight/2;
        return miny1;
    }

    @Override
    public double getBottom() {
        double maxy2 = translateY + shipHeight/2;
        return maxy2;
    }

    @Override
    public double getCenterX() {
        return translateX;
    }

    @Override
    public double getCentreY() {
        return translateY;
    }

    public boolean detectShip(double x1, double y1, double x2, double y2){
        return translateX >= x1 && translateY <= x2 && translateY >= y1 && translateY <= y2;


        }

    public void moveShip(double dx, double dy) {
        for (int i = 0; i < displayXs.length; i++) {
            displayXs[i] += dx;
            displayYs[i] += dy;
        }
        translateX += dx;
        translateY += dy;
    }

    @Override
    public void rotate(double a) {
        rotate(a,translateX,translateY);
    }

    public void rotate(double a, double cx, double cy) {
        double x, y;
        double radians = a * Math.PI / 180;
        for (int i = 0; i < displayXs.length; i++) {
            x = displayXs[i] - cx;
            y = displayYs[i] - cy;
            displayXs[i] = rotateX(x, y, radians) + cx;
            displayYs[i] = rotateY(x, y, radians) + cy;
        }
    }

    private double rotateX(double x, double y, double thetaR) {
        return Math.cos(thetaR) * x - Math.sin(thetaR) * y;
    }

    private double rotateY(double x, double y, double thetaR) {
        return Math.sin(thetaR) * x + Math.cos(thetaR) * y;
    }
}
