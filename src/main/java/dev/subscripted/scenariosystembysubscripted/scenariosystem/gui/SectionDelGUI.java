package dev.subscripted.scenariosystembysubscripted.scenariosystem.gui;

import de.einsjustinnn.skyblock.scenariosystem.utils.ConfigHandler;
import de.einsjustinnn.skyblock.scenariosystem.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.List;

public class SectionDelGUI {

    public static void openDelGUI(Player player) {
        Inventory inventory = Bukkit.createInventory(player, 45, "§b§lScenario §8• §7Bereich löschen");

        ConfigurationSection sections = ConfigHandler.getConfig().getConfigurationSection("sections");
        List<String> sectionNames = new ArrayList<>();
        if (sections != null) {
            sectionNames.addAll(sections.getKeys(false));
        }

        for (int i = 0; i < sectionNames.size(); i++) {
            String sectionName = sectionNames.get(i);
            String creationDateTime = sections.getString(sectionName + ".created_at");
            ItemBuilder itemBuilder = new ItemBuilder(Material.LODESTONE).setDisplayName(sectionName).addLoreLine("§7Erstellt am: " + creationDateTime);
            inventory.addItem(itemBuilder.build());
        }

        for (int i = sectionNames.size(); i < 45; i++) {
            inventory.setItem(i, new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setDisplayName(" ").build());
        }

        ItemBuilder glass = new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setDisplayName(" ");
        for (int i = 27; i < 36; i++) {
            inventory.setItem(i, glass.build());
        }

        ItemBuilder del_item = new ItemBuilder(Material.BARRIER).setDisplayName("§c§lLöschen").addLoreLine("§7Um einen Bereich zu löschen musst du").addLoreLine("§7diesen auf dieses Feld ziehen!");
        ItemBuilder back = new ItemBuilder(Material.ARROW).setDisplayName("§8§lZurück");

        inventory.setItem(40, del_item.build());
        inventory.setItem(36, back.build());

        player.playSound(player.getLocation(), Sound.ITEM_BOOK_PAGE_TURN, 1f, 10f);
        player.openInventory(inventory);
    }


    public static void updateDelGUI(Player player) {
        Inventory inventory = Bukkit.createInventory(player, 45, "§b§lScenario §8• §7Bereich löschen");

        ConfigurationSection sections = ConfigHandler.getConfig().getConfigurationSection("sections");
        List<String> sectionNames = new ArrayList<>();
        if (sections != null) {
            sectionNames.addAll(sections.getKeys(false));
        }

        for (int i = 0; i < sectionNames.size(); i++) {
            String sectionName = sectionNames.get(i);
            String creationDateTime = sections.getString(sectionName + ".created_at");
            ItemBuilder itemBuilder = new ItemBuilder(Material.LODESTONE).setDisplayName(sectionName).addLoreLine("§7Erstellt am: " + creationDateTime);
            inventory.addItem(itemBuilder.build());
        }

        for (int i = sectionNames.size(); i < 45; i++) {
            inventory.setItem(i, new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setDisplayName(" ").build());
        }

        ItemBuilder glass = new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setDisplayName(" ");
        for (int i = 27; i < 36; i++) {
            inventory.setItem(i, glass.build());
        }

        ItemBuilder del_item = new ItemBuilder(Material.BARRIER).setDisplayName("§c§lLöschen").addLoreLine("§7Um einen Bereich zu löschen musst du").addLoreLine("§7diesen auf dieses Feld ziehen!");
        ItemBuilder back = new ItemBuilder(Material.ARROW).setDisplayName("§8§lZurück");

        inventory.setItem(40, del_item.build());
        inventory.setItem(36, back.build());

        player.openInventory(inventory);
    }
}
