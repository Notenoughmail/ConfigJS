package com.notenoughmail.configjs;

import com.notenoughmail.configjs.hacks.EnumWriter;
import dev.latvian.mods.kubejs.event.EventJS;
import dev.latvian.mods.kubejs.typings.Generics;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.typings.Param;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.IExtensibleEnum;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("unused")
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

    @Info(value = "Sets the name of the config file", params = @Param(name = "name", value = "The name of the file, excluding .toml"))
    public void setName(String name) {
        this.name = name;
    }

    @Info(value = "Moves the config left by a tab")
    public void pop() {
        pop(1);
    }

    @Info(value = "Moves the config left by the specified amount of tabs", params = @Param(name = "amount", value = "The number of tabs to move the config by"))
    public void pop(int amount) {
        builder.pop(amount);
    }

    @Info(value = "Moves the config right by a tab under the specified path, paths can be joined with . to shift multiple times", params = @Param(name = "path", value = "The path to move the config under"))
    public void push(String path) {
        builder.push(path);
    }

    @Info(value = "Adds a comment to the config, can use multiple strings for multiple lines", params = @Param(name = "comments"))
    public void comment(String... comments) {
        builder.comment(comments);
    }

    @Info(value = "Adds and returns an IntValue config", params = {
            @Param(name = "name", value = "The name of the config option"),
            @Param(name = "defaultValue", value = "The default value"),
            @Param(name = "min", value = "The minimum allowable value"),
            @Param(name = "max", value = "The maximum allowable value")
    })
    public ForgeConfigSpec.IntValue intValue(String name, int defaultValue, int min, int max) {
        return builder.defineInRange(name, defaultValue, min, max);
    }

    @Info(value = "Adds and returns a LongValue config", params = {
            @Param(name = "name", value = "The name of the config option"),
            @Param(name = "defaultValue", value = "The default value"),
            @Param(name = "min", value = "The minimum allowable value"),
            @Param(name = "max", value = "The maximum allowable value")
    })
    public ForgeConfigSpec.LongValue longValue(String name, long defaultValue, long min, long max) {
        return builder.defineInRange(name, defaultValue, min, max);
    }

    @Info(value = "Adds and returns a DoubleValue config", params = {
            @Param(name = "name", value = "The name of the config option"),
            @Param(name = "defaultValue", value = "The default value"),
            @Param(name = "min", value = "The minimum allowable value"),
            @Param(name = "max", value = "The maximum allowable value")
    })
    public ForgeConfigSpec.DoubleValue doubleValue(String name, double defaultValue, double min, double max) {
        return builder.defineInRange(name, defaultValue, min, max);
    }

    @Info(value = "Adds and returns a BooleanValue config", params = {
            @Param(name = "name", value = "The name of the config option"),
            @Param(name = "defaultValue", value = "The default value")
    })
    public ForgeConfigSpec.BooleanValue booleanValue(String name, boolean defaultValue) {
        return builder.define(name, defaultValue);
    }

    @Info(value = "Adds and returns an EnumValue config", params = {
            @Param(name = "name", value = "The name of the config option"),
            @Param(name = "defaultValue", value = "The default value, must be included in enumValues"),
            @Param(name = "enumValues", value = "A list of all allowed values")
    })
    @Generics(value = {Enum.class, String.class})
    public <T extends Enum<T>> ForgeConfigSpec.EnumValue<?> enumValue(String name, String defaultValue, List<String> enumValues) {
        final Class<T> enumClass = EnumWriter.getNewEnum(enumValues);
        return builder.defineEnum(name, Enum.valueOf(enumClass, defaultValue));
    }

    @Info(value = "Adds and returns an EnumValue config, with the enum class being pulled from the provided default enum value", params = {
            @Param(name = "name", value = "The name of the config option"),
            @Param(name = "defaultValue", value = "The default value, must be an enum object")
    })
    public <T extends Enum<T>> ForgeConfigSpec.EnumValue<?> enumValue(String name, Object defaultValue) {
        return builder.defineEnum(name, (T) defaultValue); // Cannot use T straight out because Kube likes to write the type recursively and throw a StackOverflowError because of it
    }
}
