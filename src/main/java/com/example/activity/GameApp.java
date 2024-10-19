package com.example.activity;

import com.almasb.fxgl.app.ApplicationMode;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.SceneFactory;
import com.almasb.fxgl.audio.Music;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.FXGLForKtKt;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.level.Level;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.physics.*;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.time.TimerAction;
import com.example.activity.Factories.GameFactory;
import com.example.activity.collision.PlayerEnemyCollision;
import com.example.activity.collision.VisionPlayerCollision;
import com.example.activity.components.EnemyComponent;
import com.example.activity.components.PlayerComponent;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Map;
import java.util.Random;

import static com.almasb.fxgl.dsl.FXGL.getAppHeight;
import static com.almasb.fxgl.dsl.FXGL.getAppWidth;
import static com.almasb.fxgl.dsl.FXGL.getGameScene;
import static com.almasb.fxgl.dsl.FXGL.getPhysicsWorld;
import static com.almasb.fxgl.dsl.FXGL.inc;
import static com.almasb.fxgl.dsl.FXGL.loopBGM;
import static com.almasb.fxgl.dsl.FXGL.run;
import static com.almasb.fxgl.dsl.FXGL.setLevelFromMap;
import static com.almasb.fxgl.dsl.FXGLForKtKt.*;
public class GameApp extends GameApplication {
    static Entity player;
    static Entity enemy,vis;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
        protected void initSettings(GameSettings settings) {
            settings.setWidth(720);
            settings.setHeight(720);
            settings.setTitle("Automata Theory Bonus Activity");
//            settings.setConfigClass(Config.class10);

        settings.setApplicationMode(ApplicationMode.DEVELOPER);
        settings.setDeveloperMenuEnabled(true);

//        settings.setMainMenuEnabled(true);
//        settings.setSceneFactory(());
        }

    @Override
    protected void initInput() {
        FXGL.getInput().addAction(new UserAction("Up") {
            @Override
            protected void onActionEnd() {
                player.getComponent(PlayerComponent.class).setPlayer_state(0);// == P: INPUT == input to transition to IDLE state

            }

            @Override
            protected void onAction() {
//                player.getComponent(PlayerComponent.class).transition(1);
                player.translateY(-1);
            }
            @Override
            protected void onActionBegin(){
                player.getComponent(PlayerComponent.class).setPlayer_state(1); // == P: INPUT == W input to transition to MOVING state
            }

        }, KeyCode.W);
        FXGL.getInput().addAction(new UserAction("Down") {
            @Override
            protected void onActionEnd() {
                player.getComponent(PlayerComponent.class).setPlayer_state(0);// == P: INPUT == input to transition to IDLE state

            }

            @Override
            protected void onAction() {
                player.translateY(1);
            }

            @Override
            protected void onActionBegin(){
                player.getComponent(PlayerComponent.class).setPlayer_state(1); // == P: INPUT == input S to transition to MOVING state
            }
        }, KeyCode.S);
        FXGL.getInput().addAction(new UserAction("left") {
            @Override
            protected void onActionEnd() {
                player.getComponent(PlayerComponent.class).setPlayer_state(0);// == P: INPUT == input to transition to IDLE state

            }

            @Override
            protected void onAction() {
                player.translateX(-1);
            }
            @Override
            protected void onActionBegin(){
                player.getComponent(PlayerComponent.class).setPlayer_state(1);  // == P: INPUT == input A to transition to MOVING state
                player.getComponent(PlayerComponent.class).changeDirection(-1);
            }
        }, KeyCode.A);
        FXGL.getInput().addAction(new UserAction("Right") {
            @Override
            protected void onActionEnd() {
                player.getComponent(PlayerComponent.class).setPlayer_state(0); // == P: INPUT == input to transition to IDLE state

            }

            @Override
            protected void onAction() {
                player.translateX(1);
            }
            @Override
            protected void onActionBegin(){
                player.getComponent(PlayerComponent.class).setPlayer_state(1); // == P: INPUT == input D to transition to MOVING state
                player.getComponent(PlayerComponent.class).changeDirection(1);
            }
        }, KeyCode.D);
        FXGL.getInput().addAction(new UserAction("Attack") {
            @Override
            protected void onActionEnd() {
                player.getComponent(PlayerComponent.class).setPlayer_state(0); // == P: INPUT == input to transition to IDLE state

            }

            @Override
            protected void onActionBegin() {
                player.getComponent(PlayerComponent.class).setPlayer_state(2);
            }
        }, MouseButton.PRIMARY); // == P: INPUT == input to transition to ATTACKING state

    }

    @Override
    protected void initGame() {
//        super.initGame();
        getGameWorld().addEntityFactory(new GameFactory());
        Level level = setLevelFromMap("tmx/map-iguess.tmx");
        player = spawn("Player");

        enemy = spawn("Enemy");
//        vis = spawn("EnemyVision");


        getGameScene().getViewport().setLazy(true);
        getGameScene().getViewport().setBounds(0, 0, 72 * 32, 72 * 32);
        getGameScene().getViewport().bindToEntity(player, getAppWidth() / 2.0, getAppHeight() / 2.0);

        // == P: INPUT == input to transition to DEAD states - kills the player for enemy
        runOnce(()->{
            Random rand = new Random();
            int n = rand.nextInt(0,2);

            if(n==0){
                System.out.println("dead na unta ka");
                enemy.getComponent(EnemyComponent.class).transition(5);
            }
            else if(n==1){
                player.getComponent(PlayerComponent.class).transition(3);
            }
            return null;
        },Duration.seconds(35));


    }

    @Override
    protected void initPhysics() {
        PhysicsWorld physics = getPhysicsWorld();
        PlayerEnemyCollision col = new PlayerEnemyCollision();
        VisionPlayerCollision vision = new VisionPlayerCollision(enemy);
        physics.addCollisionHandler(col);
        physics.addCollisionHandler(vision);


    }

    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put("player-state","Spawn State");
        vars.put("enemy-state","Spawn State");
    }

    @Override
    protected void initUI() {
        Text player_state = new Text("");
        player_state.setFont(Font.font(25));
        player_state.setTranslateX(100);
        player_state.setTranslateY(95);
        player_state.textProperty().bind(getsp("player-state"));
        addUINode(player_state);

        Text p_state = new Text("Player State");
        p_state.setFont(Font.font(12));
        p_state.setTranslateX(20);
        p_state.setTranslateY(90);
        addUINode(p_state);

        Text enemy_state = new Text("");
        enemy_state.setFont(Font.font(25));
        enemy_state.setTranslateX(500);
        enemy_state.setTranslateY(95);
        enemy_state.textProperty().bind(getsp("enemy-state"));
        addUINode(enemy_state);

        Text e_state = new Text("Enemy State");
        e_state.setFont(Font.font(12));
        e_state.setTranslateX(420);
        e_state.setTranslateY(90);
        addUINode(e_state);

    }

    @Override
    protected void onPreInit() {
//        FXGL.getSettings().setGlobalMusicVolume(0.5);
//        getSettings().setGlobalSoundVolume(0.25);
//        Music m = loopBGM("bgm-music.mp3");
    }
    public static Entity getPlayer() {
        return player;
    }
    public static Entity getEnemy(){
        return enemy;
    }
}