package net.acoyt.unchancement.mixin.client;

import moriyashiine.enchancement.client.event.DirectionBurstRenderEvent;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = DirectionBurstRenderEvent.class, remap = false)
public class DirectionBurstHudElementMixin {
    @Inject(method = "onHudRender", at = @At("HEAD"), cancellable = true)
    public void unchangement$noRender(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        ci.cancel();
    }
}
