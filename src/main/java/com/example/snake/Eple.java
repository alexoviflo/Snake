package com.example.snake;

import javafx.geometry.Bounds;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.Random;

import static com.example.snake.SnakeGame.*;

public class Eple extends Circle {

    public Eple(double x, double y, double r){
        super(x,y,r);
        setFill(Color.RED);
    }
    public void nyPosisjon(Scene scene){

        Bounds playerBounds = spiller.getBoundsInParent();
        Bounds epleBounds = this.getBoundsInParent();

        double nextX = Math.random() * scene.getWidth();
        double nextY = Math.random() * scene.getHeight();

        if(playerBounds.intersects(epleBounds)){

            point++;

            label.setStyle("-fx-font-size: 20pt;");

            label.setTextFill(Color.RED);

            label.setText("points " + point);

            setCenterX(nextX);
            setCenterY(nextY);
        }
    }
}
