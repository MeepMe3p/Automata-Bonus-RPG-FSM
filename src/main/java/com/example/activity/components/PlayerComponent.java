package com.example.activity.components;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGLForKtKt.*;

public class PlayerComponent extends Component {

    Entity player;
    private AnimationChannel dragon, idle,walking,attacking,deading;
    private AnimatedTexture texture2;
    private int player_state;
    private boolean isWalking,isIdle,isAttacking,isDead;
    public PlayerComponent(){
        Image idleImg = image("idle.png");
        Image walkImg = image("walk.png");
        Image atkImg = image("attack.png");
        Image deadImg = image("Dead.png");
        idle = new AnimationChannel(idleImg,3,50,48,Duration.seconds(1),0,2);
        walking = new AnimationChannel(walkImg,6,50,48,Duration.seconds(1),0,5);
        attacking = new AnimationChannel(atkImg,6,100,65,Duration.seconds(1),0,5);
        deading = new AnimationChannel(deadImg,5,50,48,Duration.seconds(1),0,4);
        texture2 = new AnimatedTexture(idle);

        texture2.loopAnimationChannel(idle);

    }

    @Override
    public void onAdded() {
        player = entity;
        player.getViewComponent().addChild(texture2);
        player.getTransformComponent().setScaleOrigin(new Point2D(16, 16));
        entity.setScaleOrigin(new Point2D(40,0));

        isIdle = true;

    }

    @Override
    public void onUpdate(double tpf) {
        texture2.setFitWidth(90);
        texture2.setFitHeight(90);
        if(isIdle){
            if(texture2.getAnimationChannel() != idle){
                texture2.loopAnimationChannel(idle);
            }
        }else if(isWalking){
            if(texture2.getAnimationChannel() != walking){
                texture2.loopAnimationChannel(walking);
            }
        }else if(isAttacking){
            texture2.setFitWidth(140);
            texture2.setFitHeight(140);
            player.getTransformComponent().setScaleOrigin(new Point2D(0, 0));
            entity.setScaleOrigin(new Point2D(40,0));
            if(texture2.getAnimationChannel() != attacking){
                texture2.loopAnimationChannel(attacking);
            }
        }else if(isDead) {
            if (texture2.getAnimationChannel() != deading) {
                texture2.loopAnimationChannel(deading);
            }
        }
    }

    // == P: TRANSITIONS == transition to a state depending on the indput
    public void transition(int num){
        switch(num){
            case 0: //Let go
                transitionIdlingState();
                break;
            case 1: //WASD
                transitionMovingState();
                break;
            case 2:
                // RIGHT CLICK
                transitionAttackState();
                break;
            case 3:
                // DEAD
                transitionDeadState();
                break;
        }
    }
    // == P: STATE ==   dead state
    private void transitionDeadState() {
        set("player-state","Dead state");
        falseAll();
        isDead = true;
        runOnce(()->{
            getGameController().pauseEngine();
            return null;
        },Duration.seconds(3));
    }
    // == P: STATE ==   attack state

    private void transitionAttackState() {
//        System.out.println("Attacking");
        set("player-state","Attacking state");
        falseAll();
        isAttacking = true;
//        runOnce(()->{
//
//        })

    }
    // == P: STATE ==   idling state

    private void transitionIdlingState(){
//        System.out.println("Idling");
            set("player-state","Idling state");
            falseAll();
            isIdle = true;
    }
    // == P: STATE ==   moving state

    private void transitionMovingState(){
//        System.out.println("Moving");
        set("player-state","Moving state");
        falseAll();
        isWalking = true;
    }



    public int getPlayer_state() {
        return player_state;
    }

    public void setPlayer_state(int player_state) {
        this.player_state = player_state;
        transition(player_state);
    }
    public void changeDirection(int n){
        player.setScaleX(n);
    }
    private void falseAll(){
        isWalking = false;
        isIdle = false;
        isAttacking = false;
    }
}
