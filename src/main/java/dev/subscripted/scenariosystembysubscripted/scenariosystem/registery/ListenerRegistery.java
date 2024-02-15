package dev.subscripted.scenariosystembysubscripted.scenariosystem.registery;

import dev.subscripted.scenariosystembysubscripted.Main;
import dev.subscripted.scenariosystembysubscripted.scenariosystem.handler.InventoryHandler;
import org.bukkit.Bukkit;

public class ListenerRegistery {

    public static void initListeners(Main instance) {
        Bukkit.getPluginManager().registerEvents(new InventoryHandler(instance), instance);
    }
}
