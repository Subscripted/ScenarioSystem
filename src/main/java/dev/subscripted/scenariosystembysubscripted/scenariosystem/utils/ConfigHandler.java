package dev.subscripted.scenariosystembysubscripted.scenariosystem.utils;


import dev.subscripted.scenariosystembysubscripted.Main;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class ConfigHandler {

    private static File configFile;

    @Getter
    private static FileConfiguration config;

    public static void setupFiles(Main instance) {
        configFile = new File(instance.getDataFolder(), "scenarioconfig.yml");

        if (!configFile.exists()) {
            instance.saveResource("scenarioconfig.yml", false);
        }

        config = YamlConfiguration.loadConfiguration(configFile);
    }

    public static void saveConfig() throws IOException {
        config.save(configFile);
    }
    public static void reloadConfig() throws IOException {
        config = YamlConfiguration.loadConfiguration(configFile);
    }

}
