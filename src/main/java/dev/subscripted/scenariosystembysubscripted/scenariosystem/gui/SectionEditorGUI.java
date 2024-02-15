package dev.subscripted.scenariosystembysubscripted.scenariosystem.gui;

import dev.subscripted.scenariosystembysubscripted.Main;
import dev.subscripted.scenariosystembysubscripted.scenariosystem.utils.ConfigHandler;
import dev.subscripted.scenariosystembysubscripted.scenariosystem.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.configuration.ConfigurationSection;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SectionEditorGUI {

    private static Main plugin = Main.getInstance();

    public static void openNamedGUI(Player player, String name) {
        Bukkit.getScheduler().runTask(plugin, () -> {
            ConfigurationSection sections = ConfigHandler.getConfig().getConfigurationSection("sections");
            Inventory inventory = Bukkit.createInventory(player, 45, "§b§lScenario §8• §7Bereich [§r" + name + "§7]§r");

            for (int i = 0; i < 27; i++) {
                if (inventory.getItem(i) == null) {
                    inventory.setItem(i, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setDisplayName(" ").build());
                }
            }

            for (int i = 27; i < 45; i++) {
                if (inventory.getItem(i) == null) {
                    inventory.setItem(i, new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setDisplayName(" ").build());
                }
            }

            ItemBuilder back = new ItemBuilder(Material.ARROW).setDisplayName("§8§lZurück");
            ItemBuilder aspects = new ItemBuilder(Material.STRUCTURE_VOID).setDisplayName("§7§lAspect").addLoreLine("§8Leer");
            ItemBuilder rename = new ItemBuilder(Material.LEGACY_BOOK_AND_QUILL).setDisplayName("§e§lUmbenennen").addLoreLine("§7Du kannst per Chateingabe").addLoreLine("§7den Namen des Abschnittes ändern");
            ItemBuilder delete = new ItemBuilder(Material.REDSTONE_BLOCK).setDisplayName("§c§lAspekt Löschen").addLoreLine("§7Beim interagieren wirst du").addLoreLine("§7in den Löschmodus gebracht").addLoreLine(" ").addLoreLine("§7Interagiere sobald du den Modus").addLoreLine("§7aktiviert hast mit dem Aspekt den").addLoreLine("§7du zurücksetzen willst!");

            inventory.setItem(10, aspects.build());
            inventory.setItem(12, aspects.build());
            inventory.setItem(14, aspects.build());
            inventory.setItem(16, aspects.build());
            inventory.setItem(39, rename.build());
            inventory.setItem(41, delete.build());
            inventory.setItem(36, back.build());

            player.playSound(player.getLocation(), Sound.UI_TOAST_IN, 1f, 2f);
            player.openInventory(inventory);

            if (sections == null) {
                sections = ConfigHandler.getConfig().createSection("sections");
            }
            if (!sections.contains(name)) {
                ConfigurationSection section = sections.createSection(name);
                String creationDateTime = getCurrentDateTime();
                section.set("created_at", creationDateTime);
                try {
                    ConfigHandler.saveConfig();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ItemBuilder sectionItem = new ItemBuilder(Material.LODESTONE).setDisplayName(name).addLoreLine("§7Erstellt am: " + creationDateTime);
                inventory.addItem(sectionItem.build());
            }
        });
    }

    private static String getCurrentDateTime() {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        return dateFormat.format(date).replace("-", "/");
    }
}
