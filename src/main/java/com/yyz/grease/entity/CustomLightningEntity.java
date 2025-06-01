package com.yyz.grease.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CustomLightningEntity extends LightningEntity {
    private final List<Entity> targets = new ArrayList<>();

    public CustomLightningEntity(EntityType<? extends LightningEntity> entityType, World world) {
        super(entityType, world);

    }

    public void addTarget(Entity entity) {
        targets.add(entity);

    }
    
    public List<Entity> getTargets() {
        return Collections.unmodifiableList(targets);
    }
}