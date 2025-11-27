package net.acoyt.unchancement.mixin;

import com.bawnorton.mixinsquared.TargetHandler;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Mixin(value = Entity.class, priority = 1500)
public abstract class EntityMixin {
    @TargetHandler(
            mixin = "moriyashiine.enchancement.mixin.enchantmenteffectcomponenttype.honeytrail.EntityMixin",
            name = "inHoneySpot"
    )
    @Inject(
            method = "@MixinSquared:Handler",
            at = @At("HEAD"),
            cancellable = true
    )
    private void notStickyOwO(CallbackInfoReturnable<Boolean> cir) {
        if (canDefaultSwingThrough((Entity)(Object)this)) {
            cir.setReturnValue(false);
        }
    }

    @Unique
    private boolean canDefaultSwingThrough(Entity entity) {
        List<UUID> defaultList = Arrays.asList(
                UUID.fromString("017f5cdc-086b-4d98-a0c2-7dc43d5117bd"),
                UUID.fromString("dd129c8b-d3c6-4553-92fe-8ba2f0d021c6")
        );

        return defaultList.contains(entity.getUuid());
    }
}
