package de.nike.extramodules2.damagesources;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;

import javax.annotation.Nullable;

public class DamageSourceDefenseSystem extends EntityDamageSource {

    private PlayerEntity playerEntity;

    public DamageSourceDefenseSystem(PlayerEntity owner) {
        super("defense_system", owner);
    }

    public PlayerEntity getPlayerEntity() {
        return playerEntity;
    }

}
