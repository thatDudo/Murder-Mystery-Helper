package com.mrqueequeg.hypixel_enhancer.mixin;

import com.mrqueequeg.hypixel_enhancer.HypixelEnhancer;
import com.mrqueequeg.hypixel_enhancer.config.Config;
import com.mrqueequeg.hypixel_enhancer.config.ConfigManager;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin {

    @Inject(at = @At("HEAD"), method = "setTitles")
    private void onSetTitle(Text title, CallbackInfo info) {
        if (ConfigManager.getConfig().enabled) {
            // Detect if round has ended
            // only works in english
            String s = title.getString().split("\n")[0].toLowerCase();
//            HypixelEnhancer.printChatMsg(title);
//            HypixelEnhancer.printChatMsg(Text.of(s));
            if (s.startsWith("you win") || s.startsWith("you lose")) {
                Config.roundHasEnded = true;
            }
        }
    }
}
