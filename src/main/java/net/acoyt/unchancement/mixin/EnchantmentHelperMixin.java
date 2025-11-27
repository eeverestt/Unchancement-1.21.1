package net.acoyt.unchancement.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import moriyashiine.enchancement.common.ModConfig;
import net.minecraft.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {
    @ModifyReturnValue(method = "getTridentSpinAttackStrength", at = @At("RETURN"))
    private static float strongerRiptide(float original) {
        return ModConfig.singleLevelMode ? original * 2.3f : original;
    }
}
