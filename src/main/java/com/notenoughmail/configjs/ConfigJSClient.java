package com.notenoughmail.configjs;

import dev.latvian.mods.kubejs.CommonProperties;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(value = ConfigJS.MODID, dist = Dist.CLIENT)
public class ConfigJSClient {

    public ConfigJSClient() {
        ModList.get().getModContainerById(CommonProperties.get().get("configjs_config_mod_id", ConfigJS.MODID)).ifPresent(mod -> {
            mod.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
        });
    }
}
