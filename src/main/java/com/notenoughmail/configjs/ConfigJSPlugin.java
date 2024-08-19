package com.notenoughmail.configjs;

import com.notenoughmail.configjs.hacks.EnumWriter;
import dev.latvian.mods.kubejs.CommonProperties;
import dev.latvian.mods.kubejs.event.EventGroupRegistry;
import dev.latvian.mods.kubejs.plugin.ClassFilter;
import dev.latvian.mods.kubejs.plugin.KubeJSPlugin;
import dev.latvian.mods.kubejs.script.BindingRegistry;
import dev.latvian.mods.kubejs.script.ConsoleJS;
import net.neoforged.fml.ModList;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.lowcodemod.LowCodeModContainer;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.Locale;

public class ConfigJSPlugin implements KubeJSPlugin {

    @Override
    public void initStartup() {
        ModList.get()
                .getModContainerById(CommonProperties.get().get("configjs_config_mod_id", ConfigJS.MODID))
                .filter(container -> container.getModId().equals(ConfigJS.MODID) || container instanceof LowCodeModContainer)
                .ifPresentOrElse(container -> {
                    for (ModConfig.Type type : ModConfig.Type.values()) {
                        if (ConfigJS.config.hasListeners(type)) {
                            final ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
                            ConfigJS.config.post(new KubeConfigEvent(builder), type);
                            container.registerConfig(type, builder.build(), ConfigJS.MODID + "-" + type.name().toLowerCase(Locale.ROOT) + ".toml");
                        }
                    }
                }, () -> ConsoleJS.STARTUP.error("Custom configs can only be added to \"configjs\" or a lowcodefml mod, was added to %s".formatted(CommonProperties.get().get("configjs_config_mod_id"))));
    }

    @Override
    public void registerClasses(ClassFilter filter) {
        filter.deny(ConfigJSPlugin.class);
        filter.deny(ConfigJS.class);
        filter.deny("com.notenoughmail.configjs.ConfigJSClient");
        filter.deny(EnumWriter.class); // Especially this
        filter.deny("com.notenoughmail.configjs.mixin");
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
