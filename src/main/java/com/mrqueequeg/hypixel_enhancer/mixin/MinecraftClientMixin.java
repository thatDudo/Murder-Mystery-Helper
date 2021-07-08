package com.mrqueequeg.hypixel_enhancer.mixin;

import com.mrqueequeg.hypixel_enhancer.HypixelEnhancer;
import com.mrqueequeg.hypixel_enhancer.access.EntityMixinAccess;
import com.mrqueequeg.hypixel_enhancer.access.PlayerEntityMixinAccess;
import com.mrqueequeg.hypixel_enhancer.config.Config;
import com.mrqueequeg.hypixel_enhancer.config.ConfigManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {

    // gets called on round start
    @Inject(at = @At("HEAD"), method = "joinWorld")
    private void onJoinWorld(ClientWorld world, CallbackInfo info) {
        if (ConfigManager.getConfig().enabled && !Config.onHypixelServer) {
            ServerInfo entry = ((MinecraftClient)(Object)this).getCurrentServerEntry();
            if (entry != null) {
                Config.onHypixelServer = entry.address.startsWith("mc.hypixel.");
            }
        }
    }

    @Inject(at = @At("HEAD"), method = "setWorld")
    private void onSetWorld(ClientWorld world, CallbackInfo info) {
        Config.reset();
    }

    @Inject(at = @At("HEAD"), method = "hasOutline", cancellable = true)
    private void onHasOutline(Entity entity, CallbackInfoReturnable<Boolean> info) {
        if (ConfigManager.getConfig().enabled) {
            Config config = ConfigManager.getConfig();
            if (Config.MurderMystery.isActive()) {
                if (entity instanceof PlayerEntity && ((PlayerEntityMixinAccess)entity).isRealPlayer() && !(!config.murdermystery.highlightSpectators && entity.isSpectator())) {
                    if (config.murdermystery.shouldHighlightMurders() && ((PlayerEntityMixinAccess)entity).isMurder()) {
                        ((EntityMixinAccess)entity).setGlowColor(Config.MurderMystery.murderTeamColorValue);
                        info.setReturnValue(true);
                    }
                    else if (config.murdermystery.shouldHighlightDetectives() && ((PlayerEntityMixinAccess)entity).hasBow()) {
                        ((EntityMixinAccess)entity).setGlowColor(Config.MurderMystery.detectiveTeamColorValue);
                        info.setReturnValue(true);
                    }
                    else if (config.murdermystery.shouldHighlightInnocents()) {
                        info.setReturnValue(true);
                    }
                }
                else if (entity instanceof ItemEntity && config.murdermystery.shouldHighlightGold()) {
                    if (((ItemEntity)entity).getStack().getItem() == Items.GOLD_INGOT) {
                        ((EntityMixinAccess)entity).setGlowColor(Config.MurderMystery.goldTeamColorValue);
                        info.setReturnValue(true);
                    }
                }
                else if (entity instanceof ArmorStandEntity && config.murdermystery.shouldHighlightBows()) {
                    ArmorStandEntity armorStandEntity = (ArmorStandEntity) entity;
                    for (ItemStack heldItem : armorStandEntity.getItemsHand()) {
                        if (heldItem.getItem() == Items.BOW) {
                            ((EntityMixinAccess)entity).setGlowColor(Config.MurderMystery.bowTeamColorValue);
                            info.setReturnValue(true);
                            break;
                        }
                    }
                }
            }
        }
    }
}
