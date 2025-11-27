package net.acoyt.unchancement.mixin;

import moriyashiine.enchancement.common.component.entity.DirectionBurstComponent;
import moriyashiine.enchancement.common.init.ModSoundEvents;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = DirectionBurstComponent.class, remap = false)
public abstract class DirectionBurstComponentMixin {
    @Unique private static final int DEFAULT_STRAFE_COOLDOWN = 20;

    @Shadow @Final private PlayerEntity obj;
    @Shadow private int cooldown;

    @Shadow protected abstract void setCooldown(int cooldown);

    @Shadow private boolean shouldRefresh;

    @Inject(method = "canUse", at = @At("HEAD"), cancellable = true)
    public void unchangement$canUse(CallbackInfoReturnable<Boolean> cir) {
        cir.cancel();
        cir.setReturnValue(cooldown == 0 && !obj.isSpectator());
    }

    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    public void unchangement$use(double velocityX, double velocityZ, CallbackInfo ci) {
        ci.cancel();
        double strength = 1.35;
        obj.addVelocity(velocityX * strength, 0, velocityZ * strength);
        obj.playSound(ModSoundEvents.ENTITY_GENERIC_STRAFE, 1, 1);
        obj.setSneaking(false);
        setCooldown(DEFAULT_STRAFE_COOLDOWN);
    }

    @Inject(method = "reset", at = @At("HEAD"), cancellable = true)
    public void unchangement$reset(CallbackInfo ci) {
        ci.cancel();
        setCooldown(DEFAULT_STRAFE_COOLDOWN);
        shouldRefresh = true;
    }
}
