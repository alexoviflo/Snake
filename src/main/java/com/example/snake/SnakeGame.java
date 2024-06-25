package com.example.snake;


import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.*;
import java.util.Timer;
import java.util.TimerTask;

public class SnakeGame extends Application {

    public static Pane pane = new Pane();

    public static boolean gameOver = false;

    public static  Scene scene = new Scene(pane = new Pane(), 500, 500);

  static   double snakeX = Math.random() * scene.getWidth();
  static double snakeY = Math.random()  * scene.getHeight();

   static double epleX = Math.random() * scene.getWidth();
   static double epleY = Math.random() * scene.getHeight();


   public static Eple eple = new Eple(epleX, epleY, 10);

  public static Spiller  spiller = new Spiller(snakeX, snakeY);



    public static Label label = new Label();

    public static   int point = 0;

    public static Label gAme_over = new Label("GAME OVER");

  public static Button button = new Button("RESTART");

    int numRows = 8;


    double width = pane.getWidth() / numRows;
    double height = pane.getHeight() / numRows;


    public Timeline timeline;
    @Override
    public void start(Stage stage) throws IOException {

        for(int i = 0; i < width; i++){
            for(int j = 0; j < height; j++){
                Rectangle rectangle = new Rectangle(width*i, height*j, width, height);
                if((i+j)  % 2 == 0){
                    rectangle.setFill(Color.BLUE);
                }else {
                    rectangle.setFill(Color.GREEN);
                }
                pane.getChildren().add(rectangle);

            }
        }
        button.setOnAction(actionEvent -> {
           insertScore(point);
            seHighscore();
            gAme_over.setVisible(false);
            spiller.setX(Math.random() * scene.getWidth());
            spiller.setY(Math.random() * scene.getHeight());
            eple.setCenterX(Math.random() * scene.getHeight());
            eple.setCenterY(Math.random() * scene.getHeight());
            spiller.resetSlange();
            point = 0;
            label.setText("points " + point);
            gameOver = false;
            timeline.stop();



            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {

                    Platform.runLater(() ->{
                        timeline.play();

                    });
                    timer.cancel();
                }
            }, 1000);

        });


        gAme_over.setStyle("-fx-font-size: 20pt;");
        gAme_over.setTextFill(Color.RED);
        gAme_over.setLayoutX((pane.getWidth() - gAme_over.getWidth()) / 2);
        gAme_over.setLayoutY((pane.getHeight() - gAme_over.getHeight()) / 2);

        button.setLayoutX(pane.getWidth() - button.getWidth() -100);

        pane.getChildren().addAll(spiller, eple, label,button,gAme_over);

       gAme_over.setVisible(false);
       button.setVisible(false);
        Duration frameDuration = Duration.millis(16);
      timeline = new Timeline(new KeyFrame(frameDuration, event ->{

            if(gameOver){
                timeline.stop();
                return;
            }

            spiller.move(scene);

            eple.nyPosisjon(scene);

        })
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        stage.setTitle("SLANGESPILL");
        stage.setScene(scene);
        stage.show();
    }

    public void insertScore(int score) {

         String defaultUsername = "anonym";
        // lager en ny textfield for spillren sin input
        TextField usernameField = new TextField(defaultUsername);
        pane.getChildren().add(usernameField);
        usernameField.setPromptText("skriv ditt brukernavn");

        //lager en ny alert til å få brukeren tilå skrive sitt brukernavn
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("lagre score");
        alert.setHeaderText("skriv ditt brukernavn for å lagre scoren:");
        alert.getDialogPane().setContent(usernameField);
        alert.showAndWait();

        //henter brukernavnet som er laget av spilleren
        String username = usernameField.getText();


        if(username.isEmpty()){
            username = defaultUsername;
        }else{
            defaultUsername = username;
        }

        try {

            // Create a new connection to the database
            Connection conn = DriverManager.getConnection("jdbc:sqlite:C:/Users/alexa/Desktop/DB/mydb.db");
            
            // Create a PreparedStatement to insert the score into the database
            PreparedStatement pstmt = conn.prepareStatement("INSERT INTO highscore(brukernavn, score) VALUES (?, ?)");

            // Set the parameters for the PreparedStatement
            pstmt.setString(1, username);
            pstmt.setInt(2, score);

            // Execute the PreparedStatement to insert the score into the database
            pstmt.executeUpdate();

            // Close the PreparedStatement and the database connection
            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void seHighscore(){

        try{

            //lager en ny connection til databasen
            Connection conn = DriverManager.getConnection("jdbc:sqlite:C:/Users/alexa/Desktop/DB/mydb.db");

            //lager en statement til execute select for å hente highscoren
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT brukernavn, score FROM highscore ORDER BY score DESC LIMIT 10");

            //lager en string builder for å lage highscoren i en string
            StringBuilder sb = new StringBuilder("High score:\n");
            while(rs.next()){
                String brukernavn = rs.getString("brukernavn");
                int score = rs.getInt("score");
                sb.append(brukernavn).append(": ").append(score).append("\n");
            }
            //lukker resultatet, statement, og database koblingen
            rs.close();
            stmt.close();
            conn.close();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("High scores");
            alert.setHeaderText(null);
            alert.setContentText(sb.toString());
            alert.showAndWait();


        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public Label getLabel() {
        return label;
    }

    public Button getButton() {
        return button;
    }

    public static void main(String[] args) {
        launch();
    }

    public void setGameOver(){
        gameOver = true;
    }
}