package dev.subscripted.scenariosystembysubscripted.scenariosystem.registery;

import dev.subscripted.scenariosystembysubscripted.Main;
import dev.subscripted.scenariosystembysubscripted.scenariosystem.handler.InventoryHandler;

public class CommandRegistery {

    public static void initCommands(Main instance) {

        instance.getCommand("scene").setExecutor(new InventoryHandler(instance));
        instance.getCommand("scene").setTabCompleter(new InventoryHandler(instance));

    }
}

