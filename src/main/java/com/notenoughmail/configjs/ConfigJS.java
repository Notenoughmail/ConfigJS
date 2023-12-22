package com.notenoughmail.configjs;

import com.mojang.logging.LogUtils;
import dev.latvian.mods.kubejs.event.EventGroup;
import dev.latvian.mods.kubejs.event.EventHandler;
import dev.latvian.mods.rhino.classfile.ByteCode;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.slf4j.Logger;

import java.io.IOException;

@Mod(ConfigJS.MODID)
public class ConfigJS {
    public static final String MODID = "configjs";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final EventGroup GROUP = EventGroup.of("ConfigsEvent");
    public static final EventHandler common = GROUP.startup("common", () -> ConfigEventJS.class);
    public static final EventHandler server = GROUP.startup("server", () -> ConfigEventJS.class);
    public static final EventHandler client = GROUP.startup("client", () -> ConfigEventJS.class);

    public static ModLoadingContext loadingContext = null;

    public ConfigJS() {
        loadingContext = ModLoadingContext.get();
    }
}
