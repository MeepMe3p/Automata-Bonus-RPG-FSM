package com.example.activity.collision;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;
import com.example.activity.components.EnemyComponent;

import static com.example.activity.EntityType.ENEMY;
import static com.example.activity.EntityType.PLAYER;

public class PlayerEnemyCollision extends CollisionHandler {
    public PlayerEnemyCollision() {
        super(PLAYER,ENEMY);
    }

    @Override
    protected void onCollisionBegin(Entity player, Entity enemy) {
        enemy.getComponent(EnemyComponent.class).transition(3); // == E: INPUT == collision of enemy to player aggro -> attack state
    }

}
