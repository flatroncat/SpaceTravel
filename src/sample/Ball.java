package sample;


import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class Ball extends Application {

    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Save The World!");
        Pane root = new Pane();
        root.setId("group");
        Scene primaryScene = new Scene(root);
        primaryScene.getStylesheets().addAll(this.getClass().getResource("style.css").toExternalForm());
        primaryStage.setScene(primaryScene);

        Canvas canvas = new Canvas(640,700);
        root.getChildren().add(canvas);

        GraphicsContext gc = canvas.getGraphicsContext2D();


        Enemy enemy = new Enemy();
        Player player = new Player();
        List<Sprite> shoots = new ArrayList<>();
        List<Enemy> enemies = new ArrayList<>();

        Text text = new Text(10,30,"Vidas: " + player.getLifes());
        text.setId("text");
        root.getChildren().add(text);

        Text points = new Text(405, 30, "Puntuación: 0");
        points.setId("points");
        root.getChildren().add(points);

        player.setImage("sample/player.png");
        player.setPosition((640-player.getWidth())/2,700-player.getHeight());

        enemies.add(enemy);

        player.setVelocity(0,0);

        enemy.createEnemies((ArrayList<Enemy>) enemies,10);

        LongValue lastNanoTime = new LongValue(System.nanoTime());



        new AnimationTimer(){

            @Override
            public void handle(long currentNanoTime) {

                double elapsedTime = (currentNanoTime - lastNanoTime.value) / 1000000000.0;
                lastNanoTime.value = currentNanoTime;

                text.setText("Vidas: " + player.getLifes());
                points.setText("Puntuación: " + player.getScore());

                primaryScene.setOnKeyPressed(event -> {
                    switch (event.getCode()){
                        case D:
                            player.setVelocityX(250);
                            break;
                        case A:
                            player.setVelocityX(-250);
                            break;
                        case SPACE:
                            Sprite shoot = new Sprite();
                            shoot.setImage("sample/shoot.png");
                            shoots.add(shoot);
                            shoot.setPosition((player.getPositionX()+11),player.getPositionY());
                            shoot.setVelocityY(-500);
                            break;
                    }
                });

                primaryScene.setOnKeyReleased(event -> {
                    switch (event.getCode()){
                        case D:
                            player.setVelocityX(0);
                            break;
                        case A:
                            player.setVelocityX(0);
                            break;
                    }
                });

                for (Enemy e : enemies){
                    if((e.getPositionY()+e.getHeight())>(primaryStage.getHeight()) && e.isAlive()){
                        player.loseALife();
                        System.out.println(player.getLifes());
                        root.getChildren().remove(e);
                        e.setAlive(false);
                        break;
                    }
                }

                for (int i = 0; i<shoots.size(); i++){
                    if((shoots.get(i).getPositionY()+shoots.get(i).getHeight())<(0)){
                        shoots.remove(i);
                    }
                }

                for(Enemy e: enemies){
                    if(e.isAlive()){
                        e.update(elapsedTime);
                        e.render(gc);
                    }
                }

                //Updates
                player.update(elapsedTime);
                for(Enemy e: enemies){
                    if(e.isAlive()){
                        e.update(elapsedTime);
                    }
                }
                for(Sprite s: shoots){
                    s.update(elapsedTime);
                }



                //render
                canvas.setHeight(primaryStage.getHeight());
                canvas.setWidth(primaryStage.getWidth());
                canvas.getGraphicsContext2D();
                gc.clearRect(0,0,primaryStage.getWidth(),primaryStage.getHeight());
                player.render(gc);
                for(Enemy e: enemies){
                    if(e.isAlive()){
                        e.render(gc);
                    }
                }

                for(Sprite s: shoots){
                    s.render(gc);
                }

                //Colision bala - enemigo
                for (int i=0; i<shoots.size();i++){
                    for(Enemy e: enemies){
                        if(shoots.get(i).intersects(e) ){
                            root.getChildren().remove(e);
                            e.setAlive(false);
                            player.setScore(player.getScore()+1);
                        }
                    }
                }


            }
        }.start();

        primaryStage.show();
    }

}
