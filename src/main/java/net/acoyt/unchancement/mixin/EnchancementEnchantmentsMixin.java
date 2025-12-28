package net.acoyt.unchancement.mixin;

import moriyashiine.enchancement.common.init.ModEnchantmentEffectComponentTypes;
import moriyashiine.enchancement.common.init.ModEnchantments;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ModEnchantments.class)
public class EnchancementEnchantmentsMixin {
    @Inject(method = "create", at = @At(value = "HEAD"), cancellable = true)
    private static void addEffects(Identifier id, RegistryEntryList<Item> supportedItems, int maxLevel, AttributeModifierSlot slot, ModEnchantments.EffectsAdder effectsAdder, CallbackInfoReturnable<Enchantment> cir) {
        if (id.equals(ModEnchantments.AMPHIBIOUS.getValue())) {
            Enchantment.Builder builder = Enchantment.builder(
                    Enchantment.definition(supportedItems, 5, maxLevel, Enchantment.leveledCost(5, 6), Enchantment.leveledCost(20, 6), 2, slot)
            );

            effectsAdder.addEffects(builder);

            builder.addEffect(
                    ModEnchantmentEffectComponentTypes.FLUID_WALKING);

            cir.setReturnValue(builder.build(id));
        }
    }
}
