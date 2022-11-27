package de.nike.extramodules2.entity;

import com.brandon3055.draconicevolution.entity.projectile.DraconicArrowEntity;
import de.nike.extramodules2.entity.projectiles.DraconicBulletEntity;
import de.nike.extramodules2.entity.projectiles.DraconicLightningChainEntity;
import de.nike.extramodules2.modules.EMModuleTypes;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber (bus = Mod.EventBusSubscriber.Bus.MOD)
public class EMEntities {

    public static EntityType<DraconicBulletEntity> DRACONIC_BULLET;
    public static EntityType<DraconicLightningChainEntity> DRACONIC_CHAIN;

    @SubscribeEvent
    public static void onEntityRegister(RegistryEvent.Register<EntityType<?>> event) {
        DRACONIC_BULLET = (EntityType<DraconicBulletEntity>) EntityType.Builder.<DraconicBulletEntity>of(DraconicBulletEntity::new,EntityClassification.MISC).sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(20).build("draconic_bullet").setRegistryName("draconic_bullet");
        DRACONIC_CHAIN = (EntityType<DraconicLightningChainEntity>) EntityType.Builder.<DraconicLightningChainEntity>of(DraconicLightningChainEntity::new,EntityClassification.MISC).noSave().sized(0.0F, 0.0F).clientTrackingRange(16).updateInterval(Integer.MAX_VALUE).build("draconic_chain").setRegistryName("draconic_chain");
        event.getRegistry().register(DRACONIC_BULLET);
        event.getRegistry().register(DRACONIC_CHAIN);
    }



}
