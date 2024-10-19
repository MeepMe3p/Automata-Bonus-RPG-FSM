package com.example.activity.components;


import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.AnimationChannel;
import com.almasb.fxgl.time.LocalTimer;
import com.almasb.fxgl.time.TimerAction;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import static com.almasb.fxgl.dsl.FXGL.run;
import static com.almasb.fxgl.dsl.FXGLForKtKt.*;

public class EnemyComponent extends Component {
    private Point2D velocity = Point2D.ZERO;
    private Entity player;
    private Entity skelly;

    private LocalTimer adjustDirectionTimer = FXGL.newLocalTimer();
    private TimerAction roaming;
    private Duration adjustDelay = Duration.seconds(0.15);
    private int speed;
    private AnimationChannel moving,idling,attacking,dying;
    private AnimatedTexture texture2;
    private boolean isMoving,isAggro,isAttacking,isFollowing,isDead,isIdling;
    VisionComponent vc = new VisionComponent();
    Circle vis;
    Entity vision;
    private TimerAction enemy_movement;



    public EnemyComponent(Entity player, int speed) {

        this.player = player;
        this.speed = speed;
        vis = new Circle();
        vis.setRadius(120);
        vis.setOpacity(.10);
        vis.setTranslateY(65);
        vis.setTranslateX(75);

        Image move = image("Skel_Walk.png");
        Image idle = image("Skel_Idle.png");
        Image attack = image("Skel_Attack.png");
        Image dead = image("Skel_Death.png");
        moving = new AnimationChannel(move, 4,150,150,Duration.seconds(1),0,3);
        idling = new AnimationChannel(idle, 4,150,150,Duration.seconds(1),0,3);
        attacking = new AnimationChannel(attack, 8,150,150,Duration.seconds(1),0,7);
        dying = new AnimationChannel(dead, 4,150,150,Duration.seconds(1),0,3);
        vision = spawn("EnemyVision");

        texture2 = new AnimatedTexture(moving);
        texture2.loopAnimationChannel(moving);
    }
    @Override
    public void onAdded(){
        skelly = entity;
        skelly.getViewComponent().addChild(texture2);
        skelly.getTransformComponent().setScaleOrigin(new Point2D(75, 75));
        entity.setScaleOrigin(new Point2D(50,90));
        adjustVelocity(0.016);
        skelly.getViewComponent().addChild(vis);
        vision.setPosition(skelly.getX()+2, skelly.getY()+48);

        enemy_movement = runOnce(()->{
            skelly.getComponent(EnemyComponent.class).transition(1);
            return null;
        }, Duration.seconds(5));


    }

    // == P: TRANSITIONS == transition to a state depending on the input
    public void transition(int num){
        switch(num){
            case 0:
                transitionIdleState();
                break;
            case 1:
                transitionFollowingState();
                break;
            case 2:
                transitionAggroState();
                break;
            case 3:
                transitionAttackState();
                break;
            case 4:
//                transitionHurt();
                break;
            case 5:
                transitionDeadState();
                break;
        }
    }
    // == E: STATE == player not inside the circle "not seen by enemy"
    private void transitionFollowingState() {
        falseAll();
        isFollowing = true;
        set("enemy-state","Roaming state");

        runOnce(()->{
            isFollowing = false;
            transitionIdleState();
            return null;
        },Duration.seconds(3));
    }
    // == E: STATE == dead enemy rip

    private void transitionDeadState() {
        set("enemy-state","Dead state");
        falseAll();
        isDead =  true;
        enemy_movement.expire();
        runOnce(()->{

            FXGL.getGameController().pauseEngine();
            return null;
        },Duration.seconds(2));
    }

    // == E: STATE == player inside the circle "seen by enemy"

    private void transitionAggroState() {
        falseAll();
        isAggro = true;
//        enemy_movement.expire();
        this.speed = 100;
        set("enemy-state","Aggressive state");
        System.out.println("aggro");
    }
    // == E: STATE ==  transitions to idle every few secs

    private void transitionIdleState(){
        set("enemy-state","Idle state");
        falseAll();
        isIdling= true;
        enemy_movement = runOnce(()->{
            isIdling = false;
            isFollowing = true;
            transitionFollowingState();
            return null;
        },Duration.seconds(5));
        velocity = Point2D.ZERO;
    }
    // == E: STATE == player touches enemy hitbox

    private void transitionAttackState(){
        set("enemy-state","Attacking state");
        falseAll();

        velocity = Point2D.ZERO;
        enemy_movement.pause();
        isAttacking = true;
        enemy_movement = runOnce(()->{
            transitionAggroState();
            return null;
        },Duration.seconds(2));
    }



    private void adjustVelocity(double v) {
        Point2D playerDirection = player.getCenter()
                .subtract(skelly.getCenter())
                .normalize()
                .multiply(speed);
        if(playerDirection.getX() > 0){
            skelly.setScaleX(1);
        }else{
            skelly.setScaleX(-1);
        }
        velocity = velocity.add(playerDirection).multiply(v);
//        isMoving = true;
    }

    @Override
    public void onUpdate(double tpf) {

        if (isFollowing || isAggro) {

            if (adjustDirectionTimer.elapsed(adjustDelay)) {
                adjustVelocity(tpf);
//                System.out.println(velocity);
                adjustDirectionTimer.capture();
            }
            vision.translate(velocity);

            skelly.translate(velocity);

        }
//        if (isMoving) {
//            System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
//            if (texture2.getAnimationChannel() != moving) {
//                texture2.loopAnimationChannel(moving);
//            }
//        } else
            if (isAttacking) {
            if (texture2.getAnimationChannel() != attacking) {
                texture2.loopAnimationChannel(attacking);
            }
        } else if (isDead) {
            if (texture2.getAnimationChannel() != dying) {
                texture2.loopAnimationChannel(dying);
            }
        } else if (isIdling) {
            if (texture2.getAnimationChannel() != idling) {
                texture2.loopAnimationChannel(idling);
            }
        }
    }
    private void falseAll(){
        isAttacking = false;
        isFollowing = false;
        isAggro = false;
        isDead = false;
    }
}
