package de.nike.extramodules2.damagesources;

import com.brandon3055.brandonscore.api.TechLevel;
import com.brandon3055.draconicevolution.api.damage.IDraconicDamage;
import de.nike.extramodules2.entity.projectiles.DraconicLightningChainEntity;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EntityDamageSource;

import javax.annotation.Nullable;

public class DraconicLightningDamageSource extends EntityDamageSource {

    private DraconicLightningChainEntity chainEntity;

    public DraconicLightningDamageSource(@Nullable Entity attacker, DraconicLightningChainEntity chainEntity) {
        super("extramodules2.lightning_flash", attacker);
        this.chainEntity = chainEntity;
    }

    public DraconicLightningChainEntity getChainEntity() {
        return chainEntity;
    }
}
