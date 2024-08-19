package com.notenoughmail.configjs.mixin;

import dev.latvian.mods.kubejs.CommonProperties;
import dev.latvian.mods.kubejs.script.ConsoleJS;
import net.minecraft.client.Minecraft;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;

@Mixin(value = ConfigurationScreen.TranslationChecker.class, remap = false)
public abstract class TranslationCheckerMixin {

    @Shadow
    @Final
    private Set<String> untranslatables;

    @Shadow
    @Final
    private Set<String> untranslatablesWithFallback;

    @Inject(method = "finish", at = @At(value = "HEAD"), remap = false)
    private void configjs$Finish(CallbackInfo ci) {
        if (CommonProperties.get().get("configjs_log_missing_config_translations", true) && (!untranslatables.isEmpty() || !untranslatablesWithFallback.isEmpty())) {
            final StringBuilder error = new StringBuilder();
            error.append("""
                    Untranslated configuration keys encountered. Please translate them and/or report them to the mod author (if applicable)
                    This message can be turned off by setting the 'configjs_log_missing_config_translations' property in KubeJS's common.json config file to false
                    """);
            if (!untranslatables.isEmpty()) {
                error.append("\nUntranslated keys:");
                for (String key : untranslatables) {
                    error.append("\n\t\"").append(key).append("\" : \"\"");
                }
                error.append("\n");
            }
            if (!untranslatablesWithFallback.isEmpty()) {
                error.append("\nThe following keys have fallbacks. Please check if those are suitable, and translate them if they're not.");
                for (String key : untranslatablesWithFallback) {
                    error.append("\n\t\"").append(key).append("\" : \"\"");
                }
                error.append("\n");
            }
            ConsoleJS.CLIENT.startCapturingErrors();
            ConsoleJS.CLIENT.error(error);
            ConsoleJS.CLIENT.stopCapturingErrors();
            if (Minecraft.getInstance().player != null) {
                Minecraft.getInstance().kjs$tell(ConsoleJS.CLIENT.errorsComponent("/kubejs errors client"));
            }
        }
    }
}
