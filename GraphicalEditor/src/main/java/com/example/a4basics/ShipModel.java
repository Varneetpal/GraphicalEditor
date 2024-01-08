package com.example.a4basics;

import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.Optional;

public class ShipModel {
    //public ArrayList<Ship> ships;
    ArrayList<ShipModelSubscriber> subscribers;

    ArrayList<RectModelSubscriber> rectSubscribers;
    Rubberband rubberband;

    ArrayList<Groupable> ships;

    Groupable shipgroup;



    public ShipModel() {
        shipgroup = new ShipGroup();
        subscribers = new ArrayList<>();
        ships = new ArrayList<>();

        rectSubscribers = new ArrayList<>();
        //shipgroup = new ShipGroup();
    }

    public void addUngroup(Groupable g){
        ships.add(g);
        notifySubscribers();
    }

    public void removeUngroup(Groupable g){
        ships.remove(g);
        notifySubscribers();
    }

    public void add(Groupable element){ //
        shipgroup.getChildren().add(element);
        notifySubscribers();
    }

    public void addGroup(Groupable shipgroup){
        ships.add(shipgroup);
        notifySubscribers();
    }

    public void remove(Groupable element){
        ships.remove(element);
        notifySubscribers();
    }

    public void createGroup(ArrayList<Groupable> listSelected){
        shipgroup = new ShipGroup();
        System.out.println("Entering group creation");
        listSelected.forEach(s->{
            shipgroup.getChildren().add(s);
            ships.remove(s);
        });
        ships.add(shipgroup);
        listSelected.add(shipgroup);
        shipgroup.getChildren().forEach(ship->{
            listSelected.remove(ship); //when i clear the array, then everything gets unselected when I group them
        });


        System.out.println("List iModel " + listSelected.size()+ listSelected);
        System.out.println("Group :  "+ships);
        System.out.println("Size: "+ships.size());
        notifySubscribers();

    }


    public void extractShips(ArrayList<Groupable> listSelected){
        listSelected.forEach(s->{
            if (s.hasChildren()){
                ArrayList<Groupable> ex = s.getChildren();
                ex.forEach(ship->{
                    listSelected.add(ship); //this line gives an error
//                    ships.add(ship);
                });
            }
        });
        notifySubscribers();
    }


    public Ship createShip(double x, double y) {
        Ship s = new Ship(x,y);
        ships.add(s);
        notifySubscribers();
        return s;
    }


    public Rectangle createRect(double x1, double x2, double y1, double y2){
        Rectangle r = new Rectangle(x1, y1, x2-x1, y2-y1);
        return r;
    }


    public Optional<Groupable> detectHit(double x, double y) { //original code
        return ships.stream().filter(s -> s.contains(x, y)).reduce((first, second) -> second);
    }

    public void moveShip(Groupable b, double dX, double dY) {
        b.move(dX, dY);

        notifySubscribers();
    }


    public void addSubscriber (ShipModelSubscriber aSub) {
        subscribers.add(aSub);
    }


    private void notifySubscribers() {
        subscribers.forEach(sub -> sub.modelChanged());
    }

}

