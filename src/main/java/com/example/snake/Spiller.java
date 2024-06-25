package com.example.snake;

import javafx.geometry.Bounds;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;

import static com.example.snake.SnakeGame.*;

public class Spiller extends Rectangle {
    boolean moveLeft, moveRight, moveUp, moveDown;

    double hastighet = 4;

    public Label label = new Label();

    public static ArrayList<Rectangle> rectangles;


    public Spiller(double x, double y){
        super(x,y, 20,20);
        setFill(Color.RED);
        rectangles = new ArrayList<>();

    }

    public void eat(){
        Bounds playerBounds = this.getBoundsInParent();
        Bounds epleBounds = eple.getBoundsInParent();

        if(playerBounds.intersects(epleBounds)){
            Rectangle rectangle = new Rectangle();
            rectangle.setWidth(20);
            rectangle.setHeight(20);

            rectangles.add(rectangle);

            pane.getChildren().add(rectangle);
        }
    }

    public void hodeOgKropp(){

        for(int i =  rectangles.size() -1; i >0; i--){
            Rectangle nåværende = rectangles.get(i);
            Rectangle ny = rectangles.get(i -1);
            oppdaterPosisjon(nåværende, ny);
        }
        if( rectangles.size() > 0){
            Rectangle ny =rectangles.get(0);
            oppdaterPosisjon(ny, this);

        }

    }

    public boolean kollisjon() {
        if (rectangles.size() <= 1) {
            return false;
        }

        Rectangle head = rectangles.get(0);
        double tipX = head.getX();
        double tipY = head.getY();
        double padding = 10.0;
        if (moveLeft) {
            tipX -= 20 + padding  ;
        } else if (moveRight) {
            tipX += 20 + padding;
        } else if (moveUp) {
            tipY -= 20 + padding;
        } else if (moveDown) {
            tipY += 20 + padding;
        }
        Rectangle tip = new Rectangle(tipX, tipY, 20, 20);

        for (int i = 1; i < rectangles.size(); i++) {
            Rectangle kropp = rectangles.get(i);
            if (tip.intersects(kropp.getBoundsInParent())) {
                return true;
            }
        }

        return false;
    }

    public void oppdaterPosisjon(Rectangle nåværende, Rectangle forgje){

        double forjeX = forgje.getX();
        double forjeY = forgje.getY();

        nåværende.setX(forjeX);
        nåværende.setY(forjeY);


        if(nåværende == rectangles.get(0) || nåværende == rectangles.get(1)){
            nåværende.setFill(Color.RED);
        } else{
            nåværende.setFill(Color.PURPLE);
        }

    }

    public List<Rectangle> getRectangles(){
        return rectangles;
    }

    public void resetSlange(){


        for(Rectangle rect: getRectangles()){
            pane.getChildren().remove(rect);
        }
        getRectangles().clear();

    }

    public void move(Scene scene){
        double nextX = getX();
        double nextY = getY();




        if(moveLeft && nextX > 0){
            nextX -= hastighet;
        }
        if(moveRight && nextX + getWidth() < scene.getWidth()){
            nextX += hastighet;
        }
        if(moveUp && nextY > 0){
            nextY -= hastighet;
        }
        if(moveDown && nextY + getHeight() < scene.getHeight()){
            nextY += hastighet;
        }

        if(moveLeft && nextX < 0){
            gameOver = true;
        }
        if(moveRight && nextX + getWidth() > scene.getWidth()){
            gameOver = true;
        }
        if(moveUp && nextY < 0){

            gameOver = true;
        }
        if(moveDown && nextY + getHeight() > scene.getHeight()){
            gameOver = true;
        }

        setX(nextX);
        setY(nextY);


        if(kollisjon()){
            gameOver = true;

        }
        if(gameOver){
            label.setVisible(true);
            button.setVisible(true);
        }

        eat();

        hodeOgKropp();

        scene.setOnKeyPressed(event -> {

            switch (event.getCode()){
                case W -> {
                    if(!moveDown){
                        moveUp = true;
                         moveLeft = moveRight = false;
                    }
                }
                case D -> {
                    if(!moveLeft){
                        moveRight = true;
                        moveDown  = moveUp = false;
                    }
                }
                case S -> {
                    if(!moveUp){
                        moveDown = true;
                         moveLeft = moveRight = false;
                    }

                }
                case A -> {
                    if(!moveRight){
                        moveLeft = true;
                        moveDown = moveUp  = false;

                    }

                }
            }

        });

    }


}



