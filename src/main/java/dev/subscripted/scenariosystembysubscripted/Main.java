package dev.subscripted.scenariosystembysubscripted;

import dev.subscripted.scenariosystembysubscripted.scenariosystem.registery.CommandRegistery;
import dev.subscripted.scenariosystembysubscripted.scenariosystem.registery.ListenerRegistery;
import dev.subscripted.scenariosystembysubscripted.scenariosystem.utils.ConfigHandler;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    @Getter
    public static Main instance;

    @Override
    public void onEnable() {
        instance = this;

        loadConfig();
        loadCommands();
        loadListener();

    }

    @Override
    public void onDisable() {

    }

    private static void loadCommands() {
        CommandRegistery.initCommands(instance);
    }

    private static void loadListener() {
        ListenerRegistery.initListeners(instance);
    }

    private static void loadConfig() {
        ConfigHandler.setupFiles(instance);
    }
}
