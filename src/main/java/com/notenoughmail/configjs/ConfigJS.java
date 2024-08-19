package com.notenoughmail.configjs;

import com.mojang.logging.LogUtils;
import dev.latvian.mods.kubejs.event.EventGroup;
import dev.latvian.mods.kubejs.event.EventTargetType;
import dev.latvian.mods.kubejs.event.TargetedEventHandler;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import org.slf4j.Logger;

@Mod(ConfigJS.MODID)
public class ConfigJS {
    public static final String MODID = "configjs";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final EventGroup GROUP = EventGroup.of("ConfigsEvent");
    public static final TargetedEventHandler<ModConfig.Type> config = GROUP.startup("register", () -> KubeConfigEvent.class).requiredTarget(EventTargetType.fromEnum(ModConfig.Type.class));

    public ConfigJS() {
    }
}
