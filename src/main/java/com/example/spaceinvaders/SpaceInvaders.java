package com.example.spaceinvaders;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class SpaceInvaders extends Application {

    private Pane root = new Pane();

    private double t = 0;

    private Sprite player = new Sprite(300, 750, 40, 40, "player", Color.BLUE);

    private Parent createContent() {
        root.setPrefSize(600, 800);

        root.getChildren().add(player);

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                update();
            }
        };

        timer.start();

        nextLevel();

        return root;
    }

    private void nextLevel() {
        for (int i = 0; i < 5; i++) {
            Sprite s = new Sprite(90 + i * 100, 150, 30, 30, "enemy", Color.RED);

            root.getChildren().add(s);
        }
    }

    private List<Sprite> sprites() {
        return root.getChildren().stream().map(n ->(Sprite)n).collect(Collectors.toList());
    }

    private void update() {
        t += 0.015;
        sprites().forEach(s -> {
            if ("enemybullet".equals(s.type)) {
                s.moveDown();

                if (s.getBoundsInParent().intersects(player.getBoundsInParent())) {
                    player.dead = true;
                    s.dead = true;
                }
            } else if ("playerbullet".equals(s.type)) {
                s.moveUp();

                sprites().stream().filter(e -> e.type.equals("enemy")).forEach(enemy -> {
                    if (s.getBoundsInParent().intersects(enemy.getBoundsInParent())) {
                        enemy.dead = true;
                        s.dead = true;
                    }
                });
            } else if ("enemy".equals(s.type)) {
                if (t > 2) {
                    if (Math.random() < 0.3) {
                        shoot(s);
                    }
                }
            }
        });

        root.getChildren().removeIf(n -> {
            Sprite s = (Sprite) n;
            return s.dead;
        });

        if (t > 2) {
            t = 0;
        }
    }

    private void shoot(Sprite player) {
        Sprite s = new Sprite((int) player.getTranslateX() + 20, (int) player.getTranslateY(), 5, 20, player.type + "bullet", Color.BLACK);

        root.getChildren().add(s);
    }



    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(SpaceInvaders.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(createContent());

        scene.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case A -> player.moveLeft();
                case D -> player.moveRight();
                case SPACE -> {
                    if (!player.dead) {
                        shoot(player);
                    }
                }
            }
        });
        stage.setScene(scene);
        stage.show();
    }

    private static class Sprite extends Rectangle {
        boolean dead = false;
        final  String type;

        Sprite(int x, int y, int width, int height, String type, Color color) {
            super(width, height, color);

            this.type = type;
            setTranslateX(x);
            setTranslateY(y);
        }


        void moveLeft() {
            setTranslateX(getTranslateX() -5);
        }

        void moveRight() {
            setTranslateX(getTranslateX() + 5);
        }

        void moveUp() {
            setTranslateY(getTranslateY() - 5);
        }

        void  moveDown() {
            setTranslateY(getTranslateY() + 5);
        }
    }

    public static void main(String[] args) {
        launch();
    }
}