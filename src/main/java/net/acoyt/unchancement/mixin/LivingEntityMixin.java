package net.acoyt.unchancement.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    @Shadow public abstract ItemStack getEquippedStack(EquipmentSlot slot);

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @ModifyReturnValue(method = "computeFallDamage", at = @At(value = "RETURN", ordinal = 1))
    private int lessDamageIfBootEnchants(int original) {
        if (EnchantmentHelper.hasEnchantments(this.getEquippedStack(EquipmentSlot.FEET))) {
            return Math.round((float) original / 1.5f);
        }

        return original;
    }
}
