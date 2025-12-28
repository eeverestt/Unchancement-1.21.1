package net.acoyt.unchancement.mixin;

import moriyashiine.enchancement.common.component.entity.RotationBurstComponent;
import moriyashiine.enchancement.common.init.ModSoundEvents;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = RotationBurstComponent.class, remap = false)
public abstract class RotationBurstComponentMixin {
    @Unique private static final int DEFAULT_DASH_COOLDOWN = 20;

    @Shadow @Final private PlayerEntity obj;
    @Shadow private boolean shouldRefresh;
    @Shadow private int cooldown, wavedashTicks;

    @Shadow public abstract void setCooldown(int cooldown);

    @Inject(method = "canUse", at = @At("HEAD"), cancellable = true)
    public void unchangement$canUse(CallbackInfoReturnable<Boolean> cir) {
        cir.cancel();
        cir.setReturnValue(cooldown == 0 && !obj.isOnGround() && EnchancementUtil.isGroundedOrAirborne(obj));
    }

    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    public void unchangement$use(CallbackInfo ci) {
        ci.cancel();
        obj.playSound(ModSoundEvents.ENTITY_GENERIC_DASH, 1, 1);
        Vec3d velocity = obj.getRotationVector().normalize().multiply(1.1);
        obj.setVelocity(velocity.getX(), velocity.getY(), velocity.getZ());
        obj.fallDistance = 0;
        setCooldown(DEFAULT_DASH_COOLDOWN);
        shouldRefresh = false;
        wavedashTicks = 3;
    }

    @Inject(method = "reset", at = @At("HEAD"), cancellable = true)
    public void unchangement$reset(CallbackInfo ci) {
        ci.cancel();
        setCooldown(DEFAULT_DASH_COOLDOWN);
        shouldRefresh = false;
    }
}
