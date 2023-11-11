package com.notenoughmail.configjs;

import dev.latvian.mods.kubejs.event.EventJS;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.typings.Param;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.minecraft.world.item.FireworkRocketItem;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.IExtensibleEnum;

import java.util.List;

public class ConfigEventJS extends EventJS {

    private final ForgeConfigSpec.Builder builder;
    private String name;

    public ConfigEventJS(ForgeConfigSpec.Builder builder, String type) {
        this.builder = builder;
        name = ConfigJS.MODID + "-" + type;
    }

    @HideFromJS
    public String getName() {
        return name + ".toml";
    }

    @Info(value = "Sets the name of the config file", params = @Param(name = "name"))
    public void setName(String name) {
        this.name = name;
    }

    @Info(value = "Moves the config left by a tab")
    public void pop() {
        pop(1);
    }

    @Info(value = "Moves the config left by the specified amount of tabs", params = @Param(name = "amount"))
    public void pop(int amount) {
        builder.pop(amount);
    }

    @Info(value = "Moves the config right by a tab under the specified path, paths can be joined with . to shift multiple times", params = @Param(name = "path"))
    public void push(String path) {
        builder.push(path);
    }

    @Info(value = "Adds a comment to the config, can use multiple strings for multiple lines", params = @Param(name = "comments"))
    public void comment(String... comments) {
        builder.comment(comments);
    }

    @Info(value = "Adds and returns an IntValue config", params = {
            @Param(name = "name", value = "The name of the option"),
            @Param(name = "defaultValue", value = "The default value"),
            @Param(name = "min", value = "The minimum allowable value"),
            @Param(name = "max", value = "The maximum allowable value")
    })
    public ForgeConfigSpec.IntValue intValue(String name, int defaultValue, int min, int max) {
        return builder.defineInRange(name, defaultValue, min, max);
    }

    @Info(value = "Adds and returns a LongValue config", params = {
            @Param(name = "name", value = "The name of the option"),
            @Param(name = "defaultValue", value = "The default value"),
            @Param(name = "min", value = "The minimum allowable value"),
            @Param(name = "max", value = "The maximum allowable value")
    })
    public ForgeConfigSpec.LongValue longValue(String name, long defaultValue, long min, long max) {
        return builder.defineInRange(name, defaultValue, min, max);
    }

    @Info(value = "Adds and returns a DoubleValue config", params = {
            @Param(name = "name", value = "The name of the option"),
            @Param(name = "defaultValue", value = "The default value"),
            @Param(name = "min", value = "The minimum allowable value"),
            @Param(name = "max", value = "The maximum allowable value")
    })
    public ForgeConfigSpec.DoubleValue doubleValue(String name, double defaultValue, double min, double max) {
        return builder.defineInRange(name, defaultValue, min, max);
    }

    @Info(value = "Adds and returns a BooleanValue config", params = {
            @Param(name = "name", value = "The name of the option"),
            @Param(name = "defaultValue", value = "The default value")
    })
    public ForgeConfigSpec.BooleanValue booleanValue(String name, boolean defaultValue) {
        return builder.define(name, defaultValue);
    }

    // This seems to occasionally cause issues with probejs' aggressive dumping
    @Info(value = "Adds and returns an EnumValue config", params = {
            @Param(name = "name", value = "The name of the option"),
            @Param(name = "defaultValue", value = "The default value, must be included in enumValues"),
            @Param(name = "enumValues", value = "A list of all allowed values, will always include a 'NULL' value as its first option")
    })
    public ForgeConfigSpec.EnumValue<?> enumValue(String name, String defaultValue, List<String> enumValues) {
        enum Enum implements IExtensibleEnum {
            NULL;

            @HideFromJS
            public static Enum create(String name) {
                throw new IllegalStateException("Enum not extended");
            }
        }
        for (String value : enumValues) {
            Enum.create(value);
        }
        return builder.defineEnum(name, Enum.valueOf(defaultValue));
    }

    @Info(value = "Adds and returns an EnumValue config, with the enum calss being pulled from the provided default enum value", params = {
            @Param(name = "name", value = "The name of the option"),
            @Param(name = "defaultValue", value = "The default value")
    })
    public <T extends Enum<T>> ForgeConfigSpec.EnumValue<T> enumValue(String name, T defaultValue) {
        return builder.defineEnum(name, defaultValue);
    }
}
