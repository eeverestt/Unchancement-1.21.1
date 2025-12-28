package net.acoyt.unchancement.mixin;

import moriyashiine.enchancement.common.init.ModEnchantments;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@SuppressWarnings("ALL")
@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
    @ModifyVariable(
            method = "attack(Lnet/minecraft/entity/Entity;)V",
            at = @At(
                    value = "STORE",
                    ordinal = 0
            ),
            ordinal = 0
    )
    private boolean forceCritWithAmphibious(boolean originalBl3, Entity target) {
        if (!((Object) this instanceof PlayerEntity player)) {
            return originalBl3;
        }

        if (player.getHealth() >= player.getMaxHealth() / 2f) {
            return originalBl3;
        }

        RegistryEntry<Enchantment> amphibiousEntry =
                player.getWorld().getRegistryManager()
                        .get(RegistryKeys.ENCHANTMENT)
                        .getEntry(ModEnchantments.AMPHIBIOUS)
                        .orElse(null);

        if (amphibiousEntry == null) {
            return originalBl3;
        }

        boolean hasAmphibious = false;
        for (ItemStack stack : player.getArmorItems()) {
            if (EnchantmentHelper.getLevel(amphibiousEntry, stack) > 0) {
                hasAmphibious = true;
                break;
            }
        }


        return hasAmphibious ? true : originalBl3;
    }
}
