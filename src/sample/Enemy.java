package sample;

import java.util.ArrayList;
import java.util.List;

public class Enemy extends Sprite{
    private boolean alive = true;
    int level = 1;

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public void createEnemies (ArrayList<Enemy> enemies, int dif){

        Thread thread = new Thread(){
            public void run(){
                for (int i = 0; i<dif; i++){
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Enemy enemy = new Enemy();
                    enemy.setImage("sample/meteoro.png");
                    enemy.setPosition((Math.random()*550),0);
                    enemy.setVelocityY(dif*10);
                    enemies.add(enemy);
                }
                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                for (int i = 0; i<dif+5; i++){
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Enemy enemy = new Enemy();
                    enemy.setImage("sample/meteoro.png");
                    enemy.setPosition((Math.random()*550),0);
                    enemy.setVelocityY(dif*20);
                    enemies.add(enemy);
                }

            }
        };
        thread.start();
    }

}
