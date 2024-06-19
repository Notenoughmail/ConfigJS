package com.notenoughmail.configjs;

import com.notenoughmail.configjs.hacks.EnumWriter;
import dev.latvian.mods.kubejs.event.EventJS;
import dev.latvian.mods.kubejs.typings.Generics;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.typings.Param;
import dev.latvian.mods.kubejs.util.UtilsJS;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.Set;
import java.util.function.Predicate;

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
    public ConfigEventJS setName(String name) {
        this.name = name;
        return this;
    }

    @Info(value = "Moves the config left by a tab")
    public ConfigEventJS pop() {
        return pop(1);
    }

    @Info(value = "Moves the config left by the specified amount of tabs", params = @Param(name = "amount", value = "The number of tabs to move the config by"))
    public ConfigEventJS pop(int amount) {
        builder.pop(amount);
        return this;
    }

    @Info(value = "Moves the config right by a tab under the specified path, paths can be joined with . to shift multiple times", params = @Param(name = "path", value = "The path to move the config under"))
    public ConfigEventJS push(String path) {
        builder.push(path);
        return this;
    }

    @Info(value = "Swaps the current path with the path specified, paths can be joined with . to shift multiple times", params = @Param(name = "path", value = "The path to swap to"))
    public ConfigEventJS swap(String path) {
        return pop().push(path);
    }

    @Info(value = "Adds a comment to the config, can use multiple strings for multiple lines", params = @Param(name = "comments"))
    public ConfigEventJS comment(String... comments) {
        builder.comment(comments);
        return this;
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
    public <T extends Enum<T>> ForgeConfigSpec.EnumValue<?> enumValue(String name, String defaultValue, String[] enumValues) {
        final Class<T> enumClass = EnumWriter.getNewEnum(enumValues);
        return builder.defineEnum(name, Enum.valueOf(enumClass, defaultValue));
    }

    @Info(value = "Adds and returns an EnumValue config, with the enum class being pulled from the provided default enum value", params = {
            @Param(name = "name", value = "The name of the config option"),
            @Param(name = "defaultValue", value = "The default value, must be an enum object")
    })
    @Generics(Enum.class)
    public <T extends Enum<?>> ForgeConfigSpec.EnumValue<?> enumValue(String name, T defaultValue) {
        return builder.defineEnum(name, UtilsJS.cast(defaultValue)); // Cast because the type param cannot be made <T extends Enum<T>> becasue then Kube crashes from writing that recursively
    }

    @Info(value = "Adds and returns a string config value", params = {
            @Param(name = "name", value = "The name of the config option"),
            @Param(name = "defaultValue", value = "The default value of the config option")
    })
    @Generics(String.class)
    public ForgeConfigSpec.ConfigValue<String> stringValue(String name, String defaultValue) {
        return builder.define(name, defaultValue);
    }

    @Info(value = "Adds a returns a string config option with a validator for the configured value", params = {
            @Param(name = "name", value = "The name of the config option"),
            @Param(name = "defaultValue", value = "The default value of the config option"),
            @Param(name = "validator", value = "The validation for the configured value")
    })
    @Generics(String.class)
    public ForgeConfigSpec.ConfigValue<String> stringValueWithPredicate(String name, String defaultValue, Predicate<String> validator) {
        return builder.define(name, defaultValue, o -> o instanceof CharSequence seq && validator.test(seq.toString()));
    }

    @Info(value = "Adds and returns a string config value option", params = {
            @Param(name = "name", value = "The name of the config option"),
            @Param(name = "defaultValue", value = "The default value of the config option"),
            @Param(name = "allowedValues", value = "An array of strings, the values that are valid for this config option, should include the default value")
    })
    @Generics(String.class)
    public ForgeConfigSpec.ConfigValue<String> stringValue(String name, String defaultValue, String[] allowedValues) {
        comment("Allowed Values: " + String.join(", ", allowedValues));
        return builder.define(name, defaultValue, o -> o instanceof CharSequence seq && Set.of(allowedValues).contains(seq.toString()));
    }
}
