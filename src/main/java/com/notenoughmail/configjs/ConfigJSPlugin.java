package com.notenoughmail.configjs;

import com.notenoughmail.configjs.hacks.EnumWriter;
import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.script.BindingsEvent;
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
            ConfigJS.loadingContext.registerConfig(ModConfig.Type.COMMON, builder.build(), event.getName());
        }
        if (ConfigJS.server.hasListeners()) {
            ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
            ConfigEventJS event = new ConfigEventJS(builder, "server");
            ConfigJS.server.post(event);
            ConfigJS.loadingContext.registerConfig(ModConfig.Type.SERVER, builder.build(), event.getName());
        }
        if (ConfigJS.client.hasListeners()) {
            ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
            ConfigEventJS event = new ConfigEventJS(builder, "client");
            ConfigJS.client.post(event);
            ConfigJS.loadingContext.registerConfig(ModConfig.Type.CLIENT, builder.build(), event.getName());
        }
    }

    @Override
    public void registerClasses(ScriptType type, ClassFilter filter) {
        filter.deny(ConfigJSPlugin.class);
        filter.deny(ConfigJS.class);
        filter.deny(EnumWriter.class); // Especially this
        filter.allow(ConfigEventJS.class.getPackageName());
    }

    @Override
    public void registerEvents() {
        ConfigJS.GROUP.register();
    }

    @Override
    public void registerBindings(BindingsEvent event) {
        event.add("ConfigJS", Bindings.class);
    }
}
