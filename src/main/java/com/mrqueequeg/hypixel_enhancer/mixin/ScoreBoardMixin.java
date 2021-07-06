package com.mrqueequeg.hypixel_enhancer.mixin;

import com.mrqueequeg.hypixel_enhancer.config.Config;
import com.mrqueequeg.hypixel_enhancer.config.ConfigManager;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardCriterion;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Scoreboard.class)
public class ScoreBoardMixin {

    @Inject(at = @At("HEAD"), method = "addObjective")
    private void onAddObjective(String name, ScoreboardCriterion criterion2, Text displayName, ScoreboardCriterion.RenderType renderType, CallbackInfoReturnable<ScoreboardObjective> info) {
        if (ConfigManager.getConfig().enabled && Config.onHypixelServer) {
            // Used for detecting active mini-game
            String displayNameString = displayName.getString();
            //Logger.sendChatMessage(Text.of(Formatting.GREEN+name+": "+displayNameString));
            if (displayNameString.equalsIgnoreCase("murder mystery")) {
                if (name.equalsIgnoreCase("prescoreboard") || name.equalsIgnoreCase("mmlobby")) {
                    Config.currentLobby = Config.HypixelLobbies.MurderMysteryLobby;
                }
                else if (Config.currentLobby != Config.HypixelLobbies.MurderMystery && name.equalsIgnoreCase("murdermystery")) {
                    Config.reset(Config.HypixelLobbies.MurderMystery);
                    Config.currentLobby = Config.HypixelLobbies.MurderMystery;
                }
            }
        }
    }
}
