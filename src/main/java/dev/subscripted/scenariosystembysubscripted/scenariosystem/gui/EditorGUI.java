package dev.subscripted.scenariosystembysubscripted.scenariosystem.gui;


import dev.subscripted.scenariosystembysubscripted.scenariosystem.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class EditorGUI {

    public static void openEditorGui(Player player) {
        Inventory editor = Bukkit.createInventory(player, 54, "§b§lScenario §8• §7Bearbeitungsmenü");

        ItemBuilder papierkorb = new ItemBuilder(Material.BARRIER).setDisplayName("§c§lPapierkorb").addLoreLine("§7Hier sind bis maximal 25 deiner gelöschten Bereiche gespeichert.").addLoreLine("§7Sobald du ein Bereich löschst kommen diese in den Papierkorb.");
        ItemBuilder tauscher = new ItemBuilder(Material.PINK_GLAZED_TERRACOTTA).setDisplayName("§d§lTauscher");
        ItemBuilder ausbledungstool = new ItemBuilder(Material.SPYGLASS).setDisplayName("§e§lAusblendungstool");
        ItemBuilder piece = new ItemBuilder(Material.KNOWLEDGE_BOOK).setDisplayName("§b§lAbschnitte").addLoreLine("§7Hier findest du deine verschiedene Abschnitte die du selber").addLoreLine("§7erstellen und selber konfigurieren kannst.");

        ItemBuilder glassPane = new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setDisplayName(" ");



        editor.setItem(10, papierkorb.build());


        editor.setItem(12, tauscher.build());


        editor.setItem(14, ausbledungstool.build());


        editor.setItem(16, piece.build());


        for (int i = 0; i < editor.getSize(); i++) {
            if (editor.getItem(i) == null) {
                editor.setItem(i, glassPane.build());
            }
        }

        player.playSound(player.getLocation(), Sound.BLOCK_BREWING_STAND_BREW, 10f, 10f);
        player.openInventory(editor);
    }
}
