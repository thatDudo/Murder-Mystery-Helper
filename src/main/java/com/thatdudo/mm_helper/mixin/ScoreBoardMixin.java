package com.thatdudo.mm_helper.mixin;

import com.thatdudo.mm_helper.MMHelper;
import com.thatdudo.mm_helper.config.Config;
import com.thatdudo.mm_helper.config.ConfigManager;
import com.thatdudo.mm_helper.util.MinecraftUtils;
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
            if (displayNameString.equalsIgnoreCase("murder mystery")) {
                if (name.equalsIgnoreCase("prescoreboard") || name.equalsIgnoreCase("mmlobby")) {
                    MMHelper.setCurrentLobby(MMHelper.HypixelLobbies.MurderMysteryLobby);
                }
                else if (name.equalsIgnoreCase("murdermystery")) {
                    MMHelper.setCurrentLobby(MMHelper.HypixelLobbies.MurderMystery);
                }
            }
            else if (displayNameString.equalsIgnoreCase("bed wars")) {
                if (name.equalsIgnoreCase("prescoreboard")) {
                    MMHelper.setCurrentLobby(MMHelper.HypixelLobbies.BedWarsLobby);
                }
                else if (name.equalsIgnoreCase("bforeboard")) {
                    MMHelper.setCurrentLobby(MMHelper.HypixelLobbies.BedWars);
                }
            }
        }
    }
}
