package com.mrqueequeg.hypixel_enhancer.mixin;

import com.mojang.authlib.GameProfile;
import com.mrqueequeg.hypixel_enhancer.HypixelEnhancer;
import com.mrqueequeg.hypixel_enhancer.access.PlayerEntityMixinAccess;
import com.mrqueequeg.hypixel_enhancer.config.Config;
import com.mrqueequeg.hypixel_enhancer.config.ConfigManager;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin implements PlayerEntityMixinAccess {

    private boolean _isMurder = false;
    private boolean _isRealPlayer = false;
    private boolean _hasBow = false;
    private boolean _isDeadSpectator = false;

    @Unique
    @Override
    public boolean isMurder() {
        return _isMurder;
    }

    @Unique
    @Override
    public boolean hasBow() {
        return _hasBow;
    }

    @Unique
    @Override
    public boolean isRealPlayer() {
        return _isRealPlayer;
    }

    @Unique
    @Override
    public boolean isDeadSpectator() {
        return _isDeadSpectator;
    }

    @Inject(at = @At("RETURN"), method = "<init>")
    private void onInit(World world, BlockPos pos, float yaw, GameProfile profile, CallbackInfo info) {
        if (ConfigManager.getConfig().enabled) {
            _isMurder = Config.MurderMystery.markedMurders.contains(profile.getId());
            _hasBow = Config.MurderMystery.markedDetectives.contains(profile.getId());
        }
    }

    @Inject(at = @At("RETURN"), method = "tick")
    private void onTick(CallbackInfo info) {
        if (ConfigManager.getConfig().enabled) {
            if (Config.MurderMystery.isActive()) {

                // Things which have to be checked regularly
                PlayerEntity player = (PlayerEntity)(Object)this;
                _isRealPlayer = !player.isSleeping() && !player.isMainPlayer() && HypixelEnhancer.isPlayerInTabList(player);

                if ((Config.MurderMystery.clientIsDead || Config.roundHasEnded) && isRealPlayer() && !isDeadSpectator()) {
                    StatusEffectInstance activeInvisibilityEffect = player.getStatusEffect(StatusEffects.INVISIBILITY);
//                    if (player.isInvisible() && activeInvisibilityEffect != null) {
//                        HypixelEnhancer.printChatMsg(Text.of(activeInvisibilityEffect.getDuration()+""));
//                    }
                    _isDeadSpectator = player.getAbilities().allowFlying || player.getAbilities().flying || (activeInvisibilityEffect != null && activeInvisibilityEffect.getDuration() > 30000);
                }

            }
        }
    }

    @Inject(at = @At("RETURN"), method = "getEquippedStack")
    private void onEquip(EquipmentSlot slot, CallbackInfoReturnable<ItemStack> info) {
        if (ConfigManager.getConfig().enabled) {
            if (Config.MurderMystery.isActive() && !Config.roundHasEnded) {

                if (!isMurder() && isRealPlayer()) {
                    Item heldItem = info.getReturnValue().getItem();
                    if (!hasBow() && (heldItem == Items.BOW || heldItem == Items.ARROW)) {
                        _hasBow = true;
                        Config.MurderMystery.markedDetectives.add(((PlayerEntity)(Object)this).getGameProfile().getId());
                    }
                    else if (Config.MurderMystery.isMurderItem(heldItem)) {
                        if (!Config.MurderMystery.clientIsMurder) {
                            HypixelEnhancer.printChatMsg(new TranslatableText("message.murder_mystery.murder_marked", Formatting.RED+((PlayerEntity)(Object)this).getGameProfile().getName()));
                        }
                        _isMurder = true;
                        Config.MurderMystery.markedMurders.add(((PlayerEntity)(Object)this).getGameProfile().getId());
                    }
                }

            }
        }
    }
}
