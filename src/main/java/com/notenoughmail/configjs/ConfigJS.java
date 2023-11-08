package com.notenoughmail.configjs;

import com.mojang.logging.LogUtils;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;

@Mod(ConfigJS.MODID)
public class ConfigJS {
    public static final String MODID = "configjs";
    private static final Logger LOGGER = LogUtils.getLogger();

    public ConfigJS() {
    }
}
