package com.example.a4basics;

import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Slider;
import javafx.scene.effect.Bloom;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.ArrayList;
import java.util.List;

public class ShipView extends StackPane implements ShipModelSubscriber, RectModelSubscriber {
    Canvas myCanvas;
    GraphicsContext gc;
    ShipModel model;
    InteractionModel iModel;
    Slider slider;


    Rubberband rect;
    double x1, y1, x2, y2;

    public ShipView() {
        VBox view = new VBox();
        slider = new Slider(0, 360, 180); //min angle, max angle and the current angle
        myCanvas = new Canvas(1000,700);
        gc = myCanvas.getGraphicsContext2D();

        rect = new Rubberband(x1, y1, x2, y2);
        view.getChildren().addAll(slider, myCanvas);

        this.getChildren().add(view);
        this.setStyle("-fx-background-color: black");

    }

    public void setModel(ShipModel newModel) {
        model = newModel;
    }

    public void setInteractionModel(InteractionModel newIModel) {
        iModel = newIModel;
    }

    public void setController(ShipController controller) {
        myCanvas.setOnMousePressed(e -> controller.handlePressed(e.getX(),e.getY(), e));
        myCanvas.setOnMouseDragged(e -> controller.handleDragged(e.getX(),e.getY(), e));
        myCanvas.setOnMouseReleased(e -> controller.handleReleased(e.getX(),e.getY(), e));
        slider.valueProperty().addListener(e->controller.handleSlider(slider.getValue()));
    }

    public void drawSelectedGroups(ArrayList<Groupable> group){
        group.forEach(ship-> {
            if (!ship.hasChildren()) {

                    gc.setFill(Color.YELLOW);
                    gc.setStroke(Color.CORAL);


                gc.fillPolygon(ship.getXs(), ship.getYs(), ship.getXs().length);
                gc.strokePolygon(ship.getXs(), ship.getYs(), ship.getXs().length);
            }
            else{
                ArrayList<Groupable> arrChildren = ship.getChildren();
//                gc.setFill(Color.TRANSPARENT);
//                gc.setStroke(Color.WHITE);
//                gc.fillRect(ship.getLeft(), ship.getTop(), ship.getRight()-ship.getLeft(), ship.getBottom()-ship.getTop());
//                gc.strokeRect(ship.getLeft(), ship.getTop(), ship.getRight()-ship.getLeft(), ship.getBottom()-ship.getTop());
//                //System.out.println("Left, Top....: "+ ship.getLeft()+ " "+ ship.getTop()+" "+ship.getRight()+" "+ship.getBottom());

                drawSelectedGroups(arrChildren);
            }
        });
    }

    public void drawUnselectedGroups(ArrayList<Groupable> group){
        group.forEach(ship-> {
            if (!ship.hasChildren()) {

                gc.setFill(Color.CORAL);
                gc.setStroke(Color.YELLOW);


                gc.fillPolygon(ship.getXs(), ship.getYs(), ship.getXs().length);
                gc.strokePolygon(ship.getXs(), ship.getYs(), ship.getXs().length);
            }
            else{
                ArrayList<Groupable> arrChildren = ship.getChildren();
                drawUnselectedGroups(arrChildren);
            }
        });
    }




    public void draw() {
        gc.clearRect(0, 0, myCanvas.getWidth(), myCanvas.getHeight());
        model.ships.forEach(ship -> {

            if (!ship.hasChildren()) {
                if (iModel.getSelectedSet().contains(ship)) {
                    gc.setFill(Color.YELLOW);
                    gc.setStroke(Color.CORAL);

                } else {
                    gc.setStroke(Color.YELLOW);
                    gc.setFill(Color.CORAL);
                }

                gc.fillPolygon(ship.getXs(), ship.getYs(), ship.getXs().length);
                gc.strokePolygon(ship.getXs(), ship.getYs(), ship.getXs().length);
            }
            else{ //if the selected groupable has children then it is already selected........................check it
                ArrayList<Groupable> arrChildren = ship.getChildren();
                if (iModel.getSelectedSet().contains(ship)){
                    gc.setFill(Color.TRANSPARENT);
                    gc.setStroke(Color.WHITE);
                    gc.fillRect(ship.getLeft(), ship.getTop(), ship.getRight()-ship.getLeft(), ship.getBottom()-ship.getTop());
                    gc.strokeRect(ship.getLeft(), ship.getTop(), ship.getRight()-ship.getLeft(), ship.getBottom()-ship.getTop());
                    //System.out.println("Left, Top....: "+ ship.getLeft()+ " "+ ship.getTop()+" "+ship.getRight()+" "+ship.getBottom());


                    drawSelectedGroups(arrChildren);
                }
                else{
                    drawUnselectedGroups(arrChildren);
                }
            }
            });

        //selection rectangle
        if (iModel.rectangleSelected != null){
            gc.setFill(Color.TRANSPARENT);
            gc.setStroke(Color.BLUE);



            double width = iModel.rectangleSelected.x2-iModel.rectangleSelected.x1;
            double height = iModel.rectangleSelected.y2-iModel.rectangleSelected.y1;
            gc.fillRect(iModel.rectangleSelected.x1, iModel.rectangleSelected.y1, width, height);
            gc.strokeRect(iModel.rectangleSelected.x1, iModel.rectangleSelected.y1, width, height);
            }



    }



        @Override
    public void modelChanged() {
        draw();
    }

    @Override
    public void rectModelChanged() {
        draw();
    }
}
