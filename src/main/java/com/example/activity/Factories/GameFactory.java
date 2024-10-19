package com.example.activity.Factories;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.*;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.particle.ParticleComponent;
import com.almasb.fxgl.particle.ParticleEmitter;
import com.almasb.fxgl.particle.ParticleEmitters;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.texture.AnimatedTexture;
import com.almasb.fxgl.texture.Texture;
import com.example.activity.GameApp;
import com.example.activity.components.EnemyComponent;
import com.example.activity.components.PlayerComponent;
import com.example.activity.components.VisionComponent;
import javafx.geometry.Point2D;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import static com.almasb.fxgl.dsl.FXGLForKtKt.*;
import static com.example.activity.EntityType.*;

public class GameFactory implements EntityFactory{
    @Spawns("Player")
    public Entity spawnPlayer(SpawnData data){
        return entityBuilder()
                .type(PLAYER)
                .bbox(new HitBox(new Point2D(22,20),BoundingShape.box(40,55)))
//                .viewWithBBox(new Rectangle(36,36))
                .with(new PlayerComponent())
                .at(new Point2D(360,360))
                .with(new CollidableComponent(true))
                .build();

    }
    @Spawns("Enemy")
    public Entity spawnEnemy(SpawnData data){
        return entityBuilder()
                .type(ENEMY)
                .bbox(new HitBox(new Point2D(70,55),BoundingShape.box(30,50)))
//                .viewWithBBox(new Rectangle(50,60,36,40))
                .with(new EnemyComponent(FXGL.<GameApp>getAppCast().getPlayer(),50))
                .with(new VisionComponent())
                .at(new Point2D(100,100))
                .with(new CollidableComponent(true))
                .build();
    }
    @Spawns("EnemyVision")
    public Entity spawnVision(SpawnData data){
        return entityBuilder()
                .type(VISION)
                .bbox(new HitBox(new Point2D(0,0),BoundingShape.circle(120)))
//                .viewWithBBox(new Circle(120))
                .with(new CollidableComponent(true))
                .build();
//                .build();
    }
}
