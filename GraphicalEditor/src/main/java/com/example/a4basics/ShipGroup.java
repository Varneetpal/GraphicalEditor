package com.example.a4basics;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

public class ShipGroup implements Groupable{



    ArrayList<Groupable> children;
    //ShipModel model;

    double minX1, minY1, maxX2, maxY2;
    ArrayList<Double> temp;

    public ShipGroup(){
        children = new ArrayList<>();
        //model = new ShipModel();
        minX1 = 1000;
        minY1 = 1000;
        maxX2 = 0;
        maxY2 = 0;
        temp = new ArrayList<>();
    }

    public void addShip(Groupable child){
        System.out.println("initial...."+children);
        children.add(child);
        System.out.println("Done adding");
        System.out.println("Final....."+children);

    }





    @Override
    public Groupable duplicate(){ //Make it again
        ShipGroup twin = new ShipGroup();

        for (Groupable g: children){
            twin.getChildren().add(g.duplicate());
        }
        return twin;
    }

    @Override
    public void rotate(double a) {
        children.forEach(g->{
//            g.rotate(a, getCenterX(), getCentreY());
        });
    }

    public void rotate(double a, double cx, double cy) {

//        children.forEach(g->{
//            g.rotate(a, getCenterX(), getCentreY());
//        });


//        double x, y;
//        double radians = a * Math.PI / 180;
//        for (int i = 0; i < displayXs.length; i++) {
//            x = displayXs[i] - cx;
//            y = displayYs[i] - cy;
//            displayXs[i] = rotateX(x, y, radians) + cx;
//            displayYs[i] = rotateY(x, y, radians) + cy;
//        }
    }


    @Override
    public boolean hasChildren() {
        return true;
    }

    @Override
    public ArrayList<Groupable> getChildren() {
        return children;
    }


    @Override
    public boolean contains(double x, double y) {
        for(Groupable g: children){
            if(g.contains(x,y)){
                return true;
            }
        }
        return false;
    }



    @Override
    public void move(double dx, double dy) {
        children.forEach(s->{
            s.move(dx, dy);
        });
    }

    @Override
    public double getLeft()
    {
        AtomicReference<Double> left = new AtomicReference<>(children.get(0).getLeft());
        this.children.forEach(g->{
            if(g.getLeft()< left.get()){
                left.set(g.getLeft());
            }
        });
        return left.get();
    }

    @Override
    public double getRight()
    {
        AtomicReference<Double> right = new AtomicReference<>(children.get(0).getRight());
        this.children.forEach(g->{
            if(g.getRight()> right.get()){
                right.set(g.getRight());
            }
        });
        return right.get();
    }

    @Override
    public double getTop()
    {
        AtomicReference<Double> top = new AtomicReference<>(children.get(0).getTop());
        this.children.forEach(g->{
            if(g.getTop()< top.get()){
                top.set(g.getTop());
            }
        });
        return top.get();
    }

    @Override
    public double getBottom()
    {
        AtomicReference<Double> bottom = new AtomicReference<>(children.get(0).getBottom());
        this.children.forEach(g->{
            if(g.getBottom()> bottom.get()){
                bottom.set(g.getBottom());
            }
        });
        return bottom.get();
    }


    @Override
    public double getCenterX() {
        double c = (getLeft()+getRight())/2;
        return c;
    }

    @Override
    public double getCentreY() {
        System.out.println("Center contains......."+getBottom()/2);
        System.out.println("Top:      "+getTop());
        System.out.println("Bottom:         "+ getBottom());
        return (getBottom()+getTop())/2;
    }

    @Override
    public double[] getXs() {
        return new double[0];
    }

    @Override
    public double[] getYs() {
        return new double[0];
    }
}
