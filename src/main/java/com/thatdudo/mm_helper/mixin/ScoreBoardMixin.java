package com.thatdudo.mm_helper.mixin;

import com.thatdudo.mm_helper.config.Config;
import com.thatdudo.mm_helper.config.ConfigManager;
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
        // Used for detecting active mini-game
        if (ConfigManager.getConfig().enabled && Config.onHypixelServer) {
            String displayNameString = displayName.getString();
//            HypixelEnhancer.printChatMsg(Text.of(Formatting.GREEN+name+": "+displayNameString));
            if (displayNameString.equalsIgnoreCase("murder mystery")) {
                if (name.equalsIgnoreCase("prescoreboard") || name.equalsIgnoreCase("mmlobby")) {
                    Config.currentLobby = Config.HypixelLobbies.MurderMysteryLobby;
                }
                else if (Config.currentLobby != Config.HypixelLobbies.MurderMystery && name.equalsIgnoreCase("murdermystery")) {
                    Config.resetLobby(Config.HypixelLobbies.MurderMystery);
                    Config.currentLobby = Config.HypixelLobbies.MurderMystery;
                }
            }
        }
    }
}
