package com.notenoughmail.configjs;

import net.minecraftforge.common.ForgeConfigSpec;

public class Bindings {

    public static <T extends Enum<T>> T getOtherValueFromConfig(ForgeConfigSpec.EnumValue<T> configValue, String otherEnumValue) {
        return Enum.valueOf(configValue.getDefault().getDeclaringClass(), otherEnumValue);
    }
}
