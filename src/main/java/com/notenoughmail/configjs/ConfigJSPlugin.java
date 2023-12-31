package com.notenoughmail.configjs;

import com.notenoughmail.configjs.hacks.EnumWriter;
import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.script.BindingsEvent;
import dev.latvian.mods.kubejs.script.ScriptType;
import dev.latvian.mods.kubejs.util.ClassFilter;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

public class ConfigJSPlugin extends KubeJSPlugin {

    @Override
    public void initStartup() {
        // The mod container shuffling is required in order for mods like Forge Config Screens
        // and Create to recognize the configs as belonging to ConfigJS
        final ModContainer activeContainer = ModLoadingContext.get().getActiveContainer();
        ModList.get().getModContainerById(ConfigJS.MODID).ifPresent(container -> {
            ModLoadingContext.get().setActiveContainer(container);
        });

        if (ConfigJS.common.hasListeners()) {
            ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
            ConfigEventJS event = new ConfigEventJS(builder, "common");
            ConfigJS.common.post(event);
            ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, builder.build(), event.getName());
        }
        if (ConfigJS.server.hasListeners()) {
            ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
            ConfigEventJS event = new ConfigEventJS(builder, "server");
            ConfigJS.server.post(event);
            ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, builder.build(), event.getName());
        }
        if (ConfigJS.client.hasListeners()) {
            ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
            ConfigEventJS event = new ConfigEventJS(builder, "client");
            ConfigJS.client.post(event);
            ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, builder.build(), event.getName());
        }

        ModLoadingContext.get().setActiveContainer(activeContainer);
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
