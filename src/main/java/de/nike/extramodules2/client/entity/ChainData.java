package de.nike.extramodules2.client.entity;

import net.minecraft.entity.Entity;

public class ChainData {

    private final int chainID;
    private final Entity owner;
    private final Entity target;
    private final Entity origin;

    public ChainData(int chainID, Entity owner, Entity origin, Entity target) {
        this.chainID = chainID;
        this.owner = owner;
        this.target = target;
        this.origin = origin;
    }

    public int getChainID() {
        return chainID;
    }

    public Entity getOwner() {
        return owner;
    }

    public Entity getTarget() {
        return target;
    }

    public Entity getOrigin() {
        return origin;
    }
}
