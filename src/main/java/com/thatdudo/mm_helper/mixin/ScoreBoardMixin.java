package com.thatdudo.mm_helper.mixin;

import com.thatdudo.mm_helper.MMHelper;
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
        if (MMHelper.onHypixelServer) {
            String displayNameString = displayName.getString();
//            MMHelper.printChatMsg(Text.of(Formatting.GREEN+name+": "+displayNameString));
            if (displayNameString.equalsIgnoreCase("murder mystery")) {
                if (name.equalsIgnoreCase("prescoreboard") || name.equalsIgnoreCase("mmlobby")) {
                    MMHelper.setCurrentLobby(MMHelper.HypixelLobbies.MurderMysteryLobby);
                }
                else if (MMHelper.currentLobby != MMHelper.HypixelLobbies.MurderMystery && name.equalsIgnoreCase("murdermystery")) {
                    MMHelper.setCurrentLobby(MMHelper.HypixelLobbies.MurderMystery);
                }
            }
        }
    }
}
