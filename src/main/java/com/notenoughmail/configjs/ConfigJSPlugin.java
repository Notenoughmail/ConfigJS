package com.notenoughmail.configjs;

import com.notenoughmail.configjs.hacks.EnumWriter;
import dev.latvian.mods.kubejs.event.EventGroupRegistry;
import dev.latvian.mods.kubejs.plugin.ClassFilter;
import dev.latvian.mods.kubejs.plugin.KubeJSPlugin;
import dev.latvian.mods.kubejs.script.BindingRegistry;
import net.neoforged.fml.ModList;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.ModConfigSpec;

public class ConfigJSPlugin implements KubeJSPlugin {

    @Override
    public void initStartup() {
        ModList.get().getModContainerById(ConfigJS.MODID).ifPresent(container -> {
            for (ModConfig.Type type : ModConfig.Type.values()) {
                if (ConfigJS.config.hasListeners(type)) {
                    final ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
                    KubeConfigEvent event = new KubeConfigEvent(builder, type);
                    ConfigJS.config.post(event, type);
                    container.registerConfig(type, builder.build(), event.getName());
                }
            }
        });
    }

    @Override
    public void registerClasses(ClassFilter filter) {
        filter.deny(ConfigJSPlugin.class);
        filter.deny(ConfigJS.class);
        filter.deny(EnumWriter.class); // Especially this
        filter.allow(KubeConfigEvent.class.getPackageName());
    }

    @Override
    public void registerEvents(EventGroupRegistry registry) {
        registry.register(ConfigJS.GROUP);
    }

    @Override
    public void registerBindings(BindingRegistry bindings) {
        bindings.add("ConfigJS", Bindings.class);
    }
}
