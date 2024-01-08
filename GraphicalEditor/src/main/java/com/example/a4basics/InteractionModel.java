package com.example.a4basics;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

public class InteractionModel {
    ArrayList<ShipModelSubscriber> subscribers;
    Ship selectedShip;
    ShipModel model;

    RectModelSubscriber rectSubscriber;
    ArrayList<RectModelSubscriber> rectModelSubscribers;

    ArrayList<Ship> selectedSet;  ///new

    ArrayList<Groupable> selectedSetGroup; //groupable

    Rubberband rectangleSelected; //new

    Rectangle r;

    double x1, y1, x2, y2; //new

    public InteractionModel() {
        subscribers = new ArrayList<>();
        selectedSet = new ArrayList<>(); ///new
        selectedSetGroup = new ArrayList<>(); //new groupable
        rectangleSelected = new Rubberband(x1, y1, x2, y2); //new

    }

    public void setModel(ShipModel model){
        this.model = model;

    }
    //setRect


//    public void selectEnclosedShips(){
//        model.ships.forEach(ship -> {
//            if (ship.detectShip(rectangle.x1, rectangle.y1, rectangle.x2, rectangle.y2)) {
//                if (selectedSet.contains(ship)){
//                    selectedSet.remove(ship);
//                }else{
//                    selectedSet.add(ship);
//                }
//
//            }
//        });
//    }


//    public void addShip(Ship newSelection){ //new method
//        selectedSet.add(newSelection);
//        System.out.println("Ship added: "+selectedSet);
//
//        notifySubscribers();
//    }

    public void addShip(Groupable newSelection){ //new method  //original for part 1
        selectedSetGroup.add(newSelection);
        System.out.println("Ship added: "+selectedSetGroup);

        notifySubscribers();
    }

    public Rubberband getRectangleSelected(){
        return this.rectangleSelected;
    }

    public void setRectangleSelected(Rubberband rect){
        this.rectangleSelected = rect;
    }

    public void removeSelectedRectangle(){
        this.rectangleSelected = null;
        System.out.println("Rectangle removed");
        notifySubscribers();
    }

    public void removeShip(Groupable selectedS){ //new method
        selectedSetGroup.remove(selectedS);
        System.out.println("Ship removed "+selectedS);
        System.out.println(this.selectedSetGroup);

        notifySubscribers();
    }


    public void clearArray(){
        selectedSetGroup.clear();
        //System.out.println("Finally deletion possible"+selectedSetGroup);
        System.out.println("Size:       "+selectedSetGroup.size());

        notifySubscribers();
    }

    public ArrayList<Groupable> getSelectedSet(){
        return selectedSetGroup;
    }

    public void clearSelection() {
        selectedShip = null;
        notifySubscribers();
    }

    public void setSelected(Ship newSelection) {
        selectedShip = newSelection;
        notifySubscribers();
    }

    public Rectangle createRect(double x1, double y1, double x2, double y2){
        r = new Rectangle(x1, y1, x2-x1, y2-y1);
        r.setStroke(Color.AQUA);
        r.setFill(Color.HOTPINK);
        //System.out.println("Created rect height: "+r.getX() + "     widhth:"+r.getY());
        return r;
    }

    public Rubberband getRectangle(){
        return rectangleSelected;
    }

    public void setEndPoints(double x, double y){
        r.setHeight(y-(r.getY()));
        r.setWidth(x-(r.getX()));
        System.out.println("Height: "+r.getHeight() + "   Width: "+r.getWidth());
    }

    public boolean isContained(double x, double y) {
        return x >= x1 && x <= x2 && y >= y1 && y <= y2;
    }


    public void addSubscriber(ShipModelSubscriber aSub) {
        subscribers.add(aSub);
    }

    public void setRectSubscriber(RectModelSubscriber aSub){
        rectSubscriber=(aSub);
    }

    public void notifySubscribers() {
        subscribers.forEach(sub -> sub.modelChanged());

    }
}
