package dev.subscripted.scenariosystembysubscripted.scenariosystem.gui;

import dev.subscripted.scenariosystembysubscripted.scenariosystem.utils.ConfigHandler;
import dev.subscripted.scenariosystembysubscripted.scenariosystem.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class SectionGUI {

    private static final List<ItemStack> sectionItems = new ArrayList<>();

    public static void openSectionMenu(Player player) {
        Inventory sections = Bukkit.createInventory(player, 45, "§b§lScenario §8• §7Bereiche");

        sectionItems.clear();

        ConfigurationSection sectionsConfig = ConfigHandler.getConfig().getConfigurationSection("sections");
        if (sectionsConfig != null) {
            for (String sectionName : sectionsConfig.getKeys(false)) {
                String creationDateTime = sectionsConfig.getString(sectionName + ".created_at", "N/A");
                ItemStack sectionItem = new ItemBuilder(Material.LODESTONE).setDisplayName(sectionName).addLoreLine("§7Erstellt am: " + creationDateTime).build();
                sectionItems.add(sectionItem);
            }
        }

        for (ItemStack item : sectionItems) {
            sections.addItem(item);
        }

        ItemBuilder addSection = new ItemBuilder(Material.GREEN_BANNER).setDisplayName("§a§lBereich Erstellen");
        ItemBuilder removeSection = new ItemBuilder(Material.RED_BANNER).setDisplayName("§4§lBereich Löschen");
        ItemBuilder back = new ItemBuilder(Material.ARROW).setDisplayName("§8§lZurück");

        for (int i = 0; i < 27; i++) {
            if (sections.getItem(i) == null) {
                sections.setItem(i, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setDisplayName(" ").build());
            }
        }

        for (int i = 27; i < 45; i++) {
            if (sections.getItem(i) == null) {
                sections.setItem(i, new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setDisplayName(" ").build());
            }
        }

        sections.setItem(39, addSection.build());
        sections.setItem(41, removeSection.build());
        sections.setItem(36, back.build());

        player.playSound(player.getLocation(), Sound.BLOCK_CHEST_OPEN, 5f, 5f);
        player.openInventory(sections);
    }

}
