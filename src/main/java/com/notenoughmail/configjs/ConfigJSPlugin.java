package com.notenoughmail.configjs;

import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.script.ScriptType;
import dev.latvian.mods.kubejs.util.ClassFilter;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig;

public class ConfigJSPlugin extends KubeJSPlugin {

    @Override
    public void initStartup() {
        if (ConfigJS.common.hasListeners()) {
            ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
            ConfigEventJS event = new ConfigEventJS(builder, "common");
            ConfigJS.common.post(event);
            ForgeConfigSpec spec = builder.build();
            ConfigJS.loadingContext.registerConfig(ModConfig.Type.COMMON, spec, event.getName());
        }
        if (ConfigJS.server.hasListeners()) {
            ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
            ConfigEventJS event = new ConfigEventJS(builder, "server");
            ConfigJS.server.post(event);
            ForgeConfigSpec spec = builder.build();
            ConfigJS.loadingContext.registerConfig(ModConfig.Type.SERVER, spec, event.getName());
        }
        if (ConfigJS.client.hasListeners()) {
            ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
            ConfigEventJS event = new ConfigEventJS(builder, "client");
            ConfigJS.client.post(event);
            ForgeConfigSpec spec = builder.build();
            ConfigJS.loadingContext.registerConfig(ModConfig.Type.CLIENT, spec, event.getName());
        }
    }

    @Override
    public void registerClasses(ScriptType type, ClassFilter filter) {
        filter.deny(ConfigJSPlugin.class);
        filter.deny(ConfigJS.class);
        filter.allow(ConfigEventJS.class);
    }

    @Override
    public void registerEvents() {
        ConfigJS.GROUP.register();
    }
}
