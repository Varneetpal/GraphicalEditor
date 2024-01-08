package com.example.a4basics;

import javafx.scene.input.*;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.Optional;

public class ShipController {
    InteractionModel iModel;
    ShipModel model;
    double prevX, prevY;
    double dX, dY;

    ShipClipboard clip;

    Rubberband rectangle;


    protected enum State {
        READY, DRAGGING, MAKINGRECT, SIMPLERECT,
    }

    protected State currentState;

    public ShipController() {
        currentState = State.READY;
        clip = new ShipClipboard();
    }

    public void setInteractionModel(InteractionModel newModel) {
        iModel = newModel;
    }

    public void setModel(ShipModel newModel) {
        model = newModel;
    }



    public void handlePressed(double x, double y, MouseEvent event) {
        double mx = event.getX();
        double my = event.getY();
        prevX = x;
        prevY = y;
        switch (currentState) {
            case READY -> {
                // context: on a ship?
//                Optional<Ship> hit = model.detectHit(x, y); //original
                Optional<Groupable> hit = model.detectHit(x, y);
                System.out.println("Hit:........"+hit);

                if (hit.isPresent()) {
                    // on ship, so select
//                    iModel.setSelected(hit.get()); //commented out on my own

                    if (iModel.getSelectedSet().isEmpty()) {
                        iModel.addShip(hit.get());
                    } else {
                        if (event.isControlDown()) {
                            if (iModel.selectedSetGroup.contains(hit.get())) {
                                iModel.removeShip(hit.get());
                                iModel.notifySubscribers();
                            }


                            else{
                                iModel.addShip(hit.get());

                            }
                        }

                    }
                    currentState = State.DRAGGING; //goes to dragging after selecting a ship
                } else {
                    // on background - is Shift down?
                    if (event.isShiftDown()) {
                        // create ship
                        Ship newShip = model.createShip(x, y);
                        iModel.setSelected(newShip); //Check this line because the first line I make is not selected on its own
//
                        currentState = State.DRAGGING;
                    } else { //on background - is control down
                        // create rectangle
                        if (event.isControlDown()) {
                            iModel.setRectangleSelected(new Rubberband(mx, my, mx, my));

                            currentState = State.MAKINGRECT;
                        } else {
                            iModel.clearArray();
                            iModel.setRectangleSelected(new Rubberband(mx, my, mx, my));
                            currentState = State.MAKINGRECT;
                            //currentState = State.READY;
                        }
                    }
                }
            }
        }
    }

    public void handleDragged(double x, double y, MouseEvent event) {
        double mx = event.getX();
        double my = event.getY();
        dX = x - prevX;
        dY = y - prevY;
        prevX = x;
        prevY = y;
        switch (currentState) {

            case DRAGGING -> {
                iModel.selectedSetGroup.forEach(ship -> {
                    model.moveShip(ship, dX, dY);
                });
            }

            case SIMPLERECT -> {
                iModel.getRectangleSelected().setEndPoints(event.getX(), event.getY());
                iModel.notifySubscribers();

            }

            case MAKINGRECT -> {
                iModel.getRectangleSelected().setEndPoints(event.getX(), event.getY());
                iModel.notifySubscribers();
                //currentState = State.SIMPLERECT;
            }
        }
    }

    public void handleReleased(double x, double y, MouseEvent event) {
        double mx = event.getX();
        double my = event.getY();
        switch (currentState) {
            case DRAGGING -> {
                currentState = State.READY;
            }
            case SIMPLERECT -> {
                iModel.getRectangleSelected().setEndPoints(event.getX(), event.getY());
                model.ships.forEach(ship->{
                   iModel.addShip(ship);
                   //iModel.notifySubscribers();
                });
                iModel.removeSelectedRectangle();
                currentState = State.READY;
            }
            case MAKINGRECT -> {
//                iModel.setEndPoints(mx, my);
                iModel.getRectangleSelected().setEndPoints(event.getX(), event.getY());

                model.ships.forEach(ship -> {
                    if (iModel.getRectangleSelected().isContained(ship.getCenterX(), ship.getCentreY())) {
                        System.out.println("Center X:   "+ship.getCenterX()+"  Center Y:       "+ ship.getCentreY());
                        if (iModel.selectedSetGroup.contains(ship)) {
                            iModel.removeShip(ship);
                            //iModel.notifySubscribers();

                        } else {
                            iModel.addShip(ship);
                            //iModel.notifySubscribers();
                        }
                    }
                });
                iModel.removeSelectedRectangle();
                currentState = State.READY;
            }
        }
    }


    public void handleSlider(double value) {
        iModel.getSelectedSet().forEach(s->{
            s.rotate(value);
            System.out.println("rotation working:   "+value);
            iModel.notifySubscribers();
        });
    }

    public void handleKeyPressed(KeyEvent keyEvent) {
        System.out.println(keyEvent.getCode());

        if (keyEvent.getCode()==KeyCode.G){

            model.shipgroup = new ShipGroup();
            iModel.getSelectedSet().forEach(s->{
                model.add(s);
                model.remove(s);
            });
            model.addGroup(model.shipgroup);
            iModel.notifySubscribers();
            iModel.addShip(model.shipgroup);
            model.shipgroup.getChildren().forEach(ship->{
                iModel.removeShip(ship);
                iModel.notifySubscribers();
            });
            System.out.println("List iModel " + iModel.getSelectedSet().size()+ iModel.getSelectedSet());
            System.out.println("Group :  "+model.ships);
            System.out.println("Size: "+model.ships.size());
            iModel.notifySubscribers();

        }

        if (keyEvent.getCode() == KeyCode.U){
            if (iModel.getSelectedSet().size() == 1){

                iModel.getSelectedSet().get(0).getChildren().forEach(s->{
                    model.addUngroup(s); //adding individual elements to the ship list
                    iModel.addShip(s);
                });
                model.removeUngroup(iModel.getSelectedSet().get(0));
                iModel.getSelectedSet().remove(0);
                iModel.notifySubscribers();
            }

        }

        if (keyEvent.isControlDown() && keyEvent.getCode() == KeyCode.C && (!iModel.getSelectedSet().isEmpty())) {
            clip.removeAll();
            clip = new ShipClipboard();
            iModel.getSelectedSet().forEach(s->{
                clip.addClip(s.duplicate());
                System.out.println("Clipboard copy working");
                System.out.println(clip.getClipboard());
            });

        }

        if (keyEvent.isControlDown() && keyEvent.getCode() == KeyCode.X && (!iModel.getSelectedSet().isEmpty())){
            clip.removeAll(); //Because if we cut and then copy, the previous elements from cut will still be there in the clip
            clip = new ShipClipboard();
            iModel.getSelectedSet().forEach(s->{
                clip.addClip(s.duplicate());
                model.remove(s);
            });
            iModel.clearArray();
            System.out.println("Cut works");
            System.out.println(clip.getClipboard());
        }

        if (keyEvent.isControlDown() && keyEvent.getCode() == KeyCode.V && (!clip.getClipboard().isEmpty())){
            System.out.println("Just before pasting: "+clip.getClipboard());
            System.out.println("iModel list before pasting: "+ iModel.getSelectedSet());
            clip.getClipboard().forEach(s->{
                model.ships.add(s);
                iModel.selectedSetGroup.add(s);
                iModel.notifySubscribers();
            });
            System.out.println("Pasting working fine");
            System.out.println(clip.getClipboard());
            System.out.println("iModel list after pasting: "+iModel.getSelectedSet());
            //Cannot clear clip list here because it will prevent us from pasting again
        }




    }
}

