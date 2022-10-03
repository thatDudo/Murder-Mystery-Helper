package com.thatdudo.mm_helper.mixin;

import com.thatdudo.mm_helper.access.ArmorStandEntityMixinAccess;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.EulerAngle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ArmorStandEntity.class)
public class ArmorStandEntityMixin implements ArmorStandEntityMixinAccess {
    private boolean _isHoldingDetectiveBow = false;
    private float lastRightArmYaw = Float.NEGATIVE_INFINITY;

    @Unique
    @Override
    public boolean isHoldingDetectiveBow() {
        return _isHoldingDetectiveBow;
    }

    @Inject(at = @At("HEAD"), method = "setRightArmRotation")
    private void onSetRightArmRotation(EulerAngle angle, CallbackInfo info) {
        if (lastRightArmYaw == Float.NEGATIVE_INFINITY) {
            lastRightArmYaw = angle.getYaw();
        }

        if (!_isHoldingDetectiveBow && Math.abs(lastRightArmYaw - angle.getYaw()) >= 0.1) {
            for (ItemStack heldItem : ((ArmorStandEntity)(Object)this).getItemsHand()) {
//                HypixelEnhancer.printChatMsg(Text.of("Rotation diff right: "+(lastRightArmYaw - angle.getYaw())));
//                HypixelEnhancer.printChatMsg(Text.of("holding: "+heldItem.getItem().getName().getString()));
                if (heldItem.getItem() == Items.BOW) {
                    _isHoldingDetectiveBow = true;
                    break;
                }
            }
            lastRightArmYaw = angle.getYaw();
        }
    }
}