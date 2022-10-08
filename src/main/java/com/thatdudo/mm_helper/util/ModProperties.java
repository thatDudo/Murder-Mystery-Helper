package com.thatdudo.mm_helper.util;

import net.fabricmc.loader.api.metadata.ModMetadata;

public class ModProperties {
    public static final String MOD_ID = "mm_helper";
    public static final ModMetadata METADATA = MinecraftUtils.getModMetadata(MOD_ID);
    public static final String MOD_NAME = METADATA.getName();
    public static final Version MOD_VERSION = new Version(METADATA.getVersion().getFriendlyString());
    public static final Version MC_VERSION = new Version("1.18.2");
}
