package com.thatdudo.mm_helper.mixin;

import com.mojang.authlib.GameProfile;
import com.thatdudo.mm_helper.MMHelper;
import com.thatdudo.mm_helper.access.PlayerEntityMixinAccess;
import com.thatdudo.mm_helper.config.ConfigManager;
import com.thatdudo.mm_helper.util.MinecraftUtils;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
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
        if (MMHelper.isEnabled()) {
            _isMurder = MMHelper.markedMurders.contains(profile.getId());
            _hasBow = MMHelper.markedDetectives.contains(profile.getId());
        }
    }

    @Inject(at = @At("RETURN"), method = "tick")
    private void onTick(CallbackInfo info) {
        if (MMHelper.isActive()) {
            PlayerEntity player = (PlayerEntity)(Object)this;
            this._isRealPlayer = !player.isSleeping() && !player.isMainPlayer() && MinecraftUtils.isPlayerInTabList(player);

            if ((MMHelper.clientIsDead || MMHelper.roundHasEnded) && isRealPlayer() && !isDeadSpectator()) {
                StatusEffectInstance activeInvisibilityEffect = player.getStatusEffect(StatusEffects.INVISIBILITY);
//                if (player.isInvisible() && activeInvisibilityEffect != null) {
//                    MMHelper.printChatMsg(Text.of(activeInvisibilityEffect.getDuration()+""));
//                }
                _isDeadSpectator = player.getAbilities().allowFlying || player.getAbilities().flying || (activeInvisibilityEffect != null && activeInvisibilityEffect.getDuration() > 30000);
            }
        }
    }

    @Inject(at = @At("RETURN"), method = "getEquippedStack")
    private void onEquip(EquipmentSlot slot, CallbackInfoReturnable<ItemStack> info) {
        if (MMHelper.isActive() && !MMHelper.roundHasEnded) {
            if (!isMurder() && isRealPlayer()) {
                Item heldItem = info.getReturnValue().getItem();
                if (!hasBow() && (heldItem == Items.BOW || heldItem == Items.ARROW)) {
                    _hasBow = true;
                    MMHelper.markedDetectives.add(((PlayerEntity)(Object)this).getGameProfile().getId());
                }
                else if (ConfigManager.getConfig().murdermystery.isMurderItem(heldItem)) {
                    if (!MMHelper.clientIsMurder) {
                        MinecraftUtils.printChatMsg(new TranslatableText("message.murder_mystery.murder_marked", Formatting.RED+((PlayerEntity)(Object)this).getGameProfile().getName()));
                    }
                    _isMurder = true;
                    MMHelper.markedMurders.add(((PlayerEntity)(Object)this).getGameProfile().getId());
                }
            }

        }
    }
}
