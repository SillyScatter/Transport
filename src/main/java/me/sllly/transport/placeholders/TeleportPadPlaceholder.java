package me.sllly.transport.placeholders;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.sllly.transport.files.configs.SettingsConfig;
import me.sllly.transport.objects.TeleportPad;
import me.sllly.transport.utils.Util;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TeleportPadPlaceholder extends PlaceholderExpansion {
    @Override
    public @NotNull String getIdentifier() {
        return "transport-time";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Sllly";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0";
    }

    @Override
    public @Nullable String onRequest(OfflinePlayer player, @NotNull String params) {
        TeleportPad teleportPad = TeleportPad.teleportPads.get(params);
        if (teleportPad == null) {
            return "&cIncorrect Placeholder Usage";
        }
        if (!teleportPad.isOn()){
            return SettingsConfig.pausedPlaceholder;
        }
        return Util.formatTime(teleportPad.getTimeUntilTeleport()*1000);
    }
}
