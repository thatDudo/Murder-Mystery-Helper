package com.thatdudo.mm_helper.mixin;

import com.thatdudo.mm_helper.MMHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin {

    @Inject(at = @At("RETURN"), method = "setTitle")
    private void onSetTitle(Text title, CallbackInfo info) {
        if (MMHelper.isEnabled()) {
            // Detect if round has ended (only works in english)
            String s = title.getString().split("\n")[0].toLowerCase();
            if (s.startsWith("you win") || s.startsWith("you lose")) {
                MMHelper.roundHasEnded = true;
            }
        }
    }
}
