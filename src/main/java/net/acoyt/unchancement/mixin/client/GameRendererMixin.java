package net.acoyt.unchancement.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyReceiver;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @Shadow @Final MinecraftClient client;
    @Unique private boolean enchancement$validBlock = false;
    @Unique private boolean enchancement$validEntity = false;

    @ModifyVariable(method = "findCrosshairTarget", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/util/hit/HitResult;getPos()Lnet/minecraft/util/math/Vec3d;"), ordinal = 0)
    private HitResult cacheReach(HitResult hitResult, Entity camera, double blockInteractionRange, double entityInteractionRange, float tickDelta) {
        enchancement$validBlock = canDefaultSwingThrough() && hitResult.getType() == HitResult.Type.BLOCK && hitResult instanceof BlockHitResult && client.world.getBlockState(((BlockHitResult) hitResult).getBlockPos()).getCollisionShape(client.world, ((BlockHitResult) hitResult).getBlockPos()).isEmpty();
        return hitResult;
    }

    @ModifyReceiver(method = "findCrosshairTarget", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/hit/HitResult;getType()Lnet/minecraft/util/hit/HitResult$Type;"))
    private HitResult treatTransparentAsMissed(HitResult instance) {
        return canDefaultSwingThrough() && enchancement$validBlock ? BlockHitResult.createMissed(instance.getPos(), Direction.EAST, BlockPos.ofFloored(instance.getPos())) : instance;
    }

    @ModifyVariable(method = "findCrosshairTarget", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/entity/projectile/ProjectileUtil;raycast(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Box;Ljava/util/function/Predicate;D)Lnet/minecraft/util/hit/EntityHitResult;"), ordinal = 0)
    private EntityHitResult checkEntityValid(EntityHitResult hitResult, Entity camera, double blockInteractionRange, double entityInteractionRange, float tickDelta) {
        Entity hitEntity = hitResult == null ? null : hitResult.getEntity();
        enchancement$validEntity = canDefaultSwingThrough() && hitEntity != null && client.player != null && client.player.getMainHandStack().contains(DataComponentTypes.ATTRIBUTE_MODIFIERS) && client.getCameraEntity().getPos().squaredDistanceTo(hitEntity.getPos()) < MathHelper.square(entityInteractionRange) && hitEntity instanceof LivingEntity && !hitEntity.isSpectator() && hitEntity.isAttackable() && !hitEntity.equals(client.player.getVehicle());
        return hitResult;
    }

    @ModifyVariable(method = "findCrosshairTarget", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Vec3d;squaredDistanceTo(Lnet/minecraft/util/math/Vec3d;)D", ordinal = 1), ordinal = 4)
    private double ignoreBlockHit(double original) {
        return canDefaultSwingThrough() && enchancement$validBlock && enchancement$validEntity ? Double.MAX_VALUE : original;
    }

    @Unique
    private boolean canDefaultSwingThrough() {
        List<UUID> defaultList = Arrays.asList(
                UUID.fromString("017f5cdc-086b-4d98-a0c2-7dc43d5117bd"),
                UUID.fromString("dd129c8b-d3c6-4553-92fe-8ba2f0d021c6")
        );
        
        return client.player != null && defaultList.contains(client.player.getUuid());
    }
}
