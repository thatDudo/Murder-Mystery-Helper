package com.mrqueequeg.hypixel_enhancer.mixin;

import com.mrqueequeg.hypixel_enhancer.access.PlayerEntityMixinAccess;
import com.mrqueequeg.hypixel_enhancer.config.Config;
import com.mrqueequeg.hypixel_enhancer.config.ConfigManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin {

    // TODO: Make a shouldGlow variable for performance improvements

    @Inject(at = @At("HEAD"), method = "getTeamColorValue", cancellable = true)
    private void onGetTeamColorValue(CallbackInfoReturnable<Integer> info) {
        if (ConfigManager.getConfig().enabled) {
            Config config = ConfigManager.getConfig();
            if (Config.MurderMystery.isActive()) {
                Object t = (Object) this;
                if (t instanceof PlayerEntity) {
                    if (config.murdermystery.shouldHighlightMurders() && ((PlayerEntityMixinAccess)t).isMurder()) {
                        info.setReturnValue(Config.MurderMystery.murderTeamColorValue);
                    }
                    else if (config.murdermystery.shouldHighlightDetectives() && ((PlayerEntityMixinAccess)t).hasBow()) {
                        info.setReturnValue(Config.MurderMystery.detectiveTeamColorValue);
                    }
                }
                else if (config.murdermystery.shouldHighlightGold() && t instanceof ItemEntity) {
                    if (t instanceof ItemEntity) {
                        if (((ItemEntity)t).getStack().getItem() == Items.GOLD_INGOT) {
                            info.setReturnValue(Config.MurderMystery.goldTeamColorValue);
                        }
                    }
                }
//                else if (t instanceof ArmorStandEntity entity) {
//                    if (entity.isInvisible()&&!entity.isAttackable()&&!entity.isSmall()&&!entity.shouldHideBasePlate()) {
//                        for (ItemStack heldItem : entity.getItemsHand()) {
//                            if (heldItem.getItem() == Items.BOW) {
//                                info.setReturnValue(Config.MurderMystery.bowTeamColorValue);
//                                break;
//                            }
//                        }
//                    }
//                }
            }
        }
    }
}
