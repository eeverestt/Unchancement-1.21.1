package net.acoyt.unchancement.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.TridentItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(TridentItem.class)
public class TridentItemMixin {
    @ModifyExpressionValue(method = "onStoppedUsing", at = @At(value = "CONSTANT", args = "intValue=10"))
    private int fasterTimeForEnchanted(int original, ItemStack stack) {
        return EnchantmentHelper.hasEnchantments(stack) ? 6 : original;
    }
}
