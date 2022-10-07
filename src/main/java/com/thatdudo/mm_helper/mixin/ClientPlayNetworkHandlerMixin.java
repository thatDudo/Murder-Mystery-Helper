package com.thatdudo.mm_helper.mixin;

import com.thatdudo.mm_helper.MMHelper;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.PlayerAbilitiesS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {

    @Inject(at = @At("RETURN"), method = "onPlayerAbilities")
    private void onOnPlayerAbilities(PlayerAbilitiesS2CPacket packet, CallbackInfo info) {
        if (MMHelper.isActive()) {
            MMHelper.clientIsDead = packet.allowFlying();
        }
    }
}
