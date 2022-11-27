package de.nike.extramodules2.client.entity;

import de.nike.extramodules2.entity.projectiles.DraconicLightningChainEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Mod.EventBusSubscriber
public class EntityDataAdder {

    public static final HashMap<Integer, ChainData> chains = new HashMap<>();

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void addEntity(EntityJoinWorldEvent event) {
        if(chains.containsKey(event.getEntity().getId())) {
            ChainData data = chains.get(event.getEntity().getId());
            DraconicLightningChainEntity entity = (DraconicLightningChainEntity) event.getEntity();
            entity.setOwner(data.getOwner());
            entity.setOrigin(data.getOrigin());
            entity.setTarget(data.getTarget());
        }
    }

}
