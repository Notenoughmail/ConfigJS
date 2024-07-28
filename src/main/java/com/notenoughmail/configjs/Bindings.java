package com.notenoughmail.configjs;

import dev.latvian.mods.kubejs.typings.Info;
import net.neoforged.neoforge.common.ModConfigSpec;

public class Bindings {

    @Info(value = "Retrieves the enum value with the given name from the class of a given enum config")
    public static <T extends Enum<T>> T getOtherValueFromEnumConfig(ModConfigSpec.EnumValue<T> configValue, String name) {
        return Enum.valueOf(configValue.getDefault().getDeclaringClass(), name);
    }
}
