package com.example.a4basics;

import javafx.scene.Group;

import java.util.ArrayList;

public class ShipClipboard {

    ArrayList<Groupable> clipboard;

    public ShipClipboard(){
        clipboard = new ArrayList<>();
    }

    public ArrayList<Groupable> getClipboard(){
        return clipboard;
    }

    public void addClip(Groupable ship){
        clipboard.add(ship);
    }
    public void removeClip(Groupable ship){
        clipboard.remove( ship);
    }

    public void removeAll(){
        clipboard.clear();
    }
}
