package com.example.activity.collision;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.CollisionHandler;
import com.example.activity.components.EnemyComponent;

import static com.example.activity.EntityType.*;

public class VisionPlayerCollision extends CollisionHandler {
    private Entity enemy;
    public VisionPlayerCollision(Entity enemy) {
        super(VISION,PLAYER);
        this.enemy = enemy;
        System.out.println(enemy.getType());
    }

    // == E: TRANSITIONS == player not inside the circle "not seen by enemy"

    @Override
    protected void onCollisionBegin(Entity vision, Entity player) {

        enemy.getComponent(EnemyComponent.class).transition(2); // == E: INPUT == collision wih vision and player idle/roaming -> aggro
    }

    @Override
    protected void onCollisionEnd(Entity a, Entity b) {
        enemy.getComponent(EnemyComponent.class).transition(1); // == E: INPUT == player leavers at enemy vision aggro -> roaming
    }
}
