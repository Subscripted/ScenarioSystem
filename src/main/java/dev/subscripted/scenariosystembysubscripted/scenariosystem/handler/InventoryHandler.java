package dev.subscripted.scenariosystembysubscripted.scenariosystem.handler;

import dev.subscripted.scenariosystembysubscripted.scenariosystem.gui.EditorGUI;
import dev.subscripted.scenariosystembysubscripted.scenariosystem.gui.SectionDelGUI;
import dev.subscripted.scenariosystembysubscripted.scenariosystem.gui.SectionEditorGUI;
import dev.subscripted.scenariosystembysubscripted.scenariosystem.gui.SectionGUI;
import dev.subscripted.scenariosystembysubscripted.scenariosystem.utils.ConfigHandler;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.*;

public class InventoryHandler implements CommandExecutor, Listener, TabCompleter {

    private final String PERMISSION = "skyblock.scenario.*";
    private final String CONSOLE_FORMAT = "§cNix für Console";
    private final String PERMISSION_MESSAGE = "§cDir fehlen die Berechtigungen um diesen Command auszuführen §7[" + PERMISSION + "§7]";
    private final String EDITOR_GUI_TITLE = "§b§lScenario §8• §7Bearbeitungsmenü";
    private final String SECTION_GUI_TITLE = "§b§lScenario §8• §7Bereiche";
    private final String SECTION_DEL_GUI = "§b§lScenario §8• §7Bereich löschen";
    private final int TIMER_DURATION = 15;

    private static final FileConfiguration guiConfig = ConfigHandler.getConfig();

    private final Plugin plugin;
    private final Map<UUID, BukkitRunnable> timers = new HashMap<>();
    private final List<Player> creatingGui = new ArrayList<>();
    private final List<String> sectionNames = new ArrayList<>();

    public InventoryHandler(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(CONSOLE_FORMAT);
            return true;
        }

        Player player = (Player) sender;
        if (!player.hasPermission(PERMISSION)) {
            player.sendMessage(PERMISSION_MESSAGE);
            return true;
        }

        if (args.length == 0 || !args[0].equalsIgnoreCase("editor")) {
            player.sendMessage("§c§lBenutze bitte /scene editor");
            return true;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            try {
                ConfigHandler.reloadConfig();
                player.sendMessage("§a§lDie Konfigurationsdatei wurde neu geladen!");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return true;
        }

        EditorGUI.openEditorGui(player);
        return true;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) {
            return;
        }

        Player player = (Player) event.getWhoClicked();
        String inventoryTitle = event.getView().getTitle();
        ConfigurationSection sections = ConfigHandler.getConfig().getConfigurationSection("sections");

        assert sections != null;
        for (String key : sections.getKeys(false)) {
            String sectionName = sections.getString(key);


            if (inventoryTitle.equals(EDITOR_GUI_TITLE) || inventoryTitle.equals(SECTION_GUI_TITLE) || inventoryTitle.contains(key) || inventoryTitle.equals(SECTION_DEL_GUI)) {
                event.setCancelled(true);

                if (inventoryTitle.equals(EDITOR_GUI_TITLE) && event.getCurrentItem().getType() == Material.KNOWLEDGE_BOOK && !event.getClickedInventory().equals(player.getInventory())) {
                    SectionGUI.openSectionMenu(player);
                    return;
                }

                if (inventoryTitle.equals(SECTION_GUI_TITLE) && event.getCurrentItem().getType() == Material.ARROW && event.getCurrentItem().getItemMeta().getDisplayName().equals("§8§lZurück") && !event.getClickedInventory().equals(player.getInventory())) {
                    EditorGUI.openEditorGui(player);
                    return;
                }

                if (inventoryTitle.equals(SECTION_GUI_TITLE) && event.getCurrentItem().getType() == Material.GREEN_BANNER && event.getCurrentItem().getItemMeta().getDisplayName().equals("§a§lBereich Erstellen")) {
                    creatingGui.add(player);
                    player.playSound(player.getLocation(), Sound.ENTITY_SHULKER_BULLET_HIT, 1f, 2f);
                    player.closeInventory();
                    player.sendMessage("Du hast 15 Sekunden Zeit! §7Bitte gib einen Namen für den Bereich ein:");

                    BukkitRunnable timer = new BukkitRunnable() {
                        @Override
                        public void run() {
                            creatingGui.remove(player);
                            player.sendMessage("§cDu hast zu lange gebraucht, um einen Bereich zu erstellen.");
                        }
                    };

                    timer.runTaskLater(plugin, TIMER_DURATION * 20);
                    timers.put(player.getUniqueId(), timer);

                    return;
                }

                if (inventoryTitle.equals(SECTION_GUI_TITLE) && event.getCurrentItem().getType() == Material.RED_BANNER && event.getCurrentItem().getItemMeta().getDisplayName().equals("§4§lBereich Löschen") && !event.getInventory().equals(player.getInventory())) {
                    SectionDelGUI.openDelGUI(player);
                    return;
                }

                if (inventoryTitle.equals(SECTION_DEL_GUI) && event.getCurrentItem().getType() == Material.ARROW && event.getCurrentItem().getItemMeta().getDisplayName().equals("§8§lZurück") && !event.getClickedInventory().equals(player.getInventory())) {
                    SectionGUI.openSectionMenu(player);
                    return;
                }
                if (inventoryTitle.equals(SECTION_DEL_GUI) && event.getCurrentItem().getType() == Material.LODESTONE && !event.getClickedInventory().equals(player.getInventory())) {
                    event.setCancelled(false);
                    return;
                }

                if (event.getCurrentItem().getType().equals(Material.LODESTONE) && inventoryTitle.equals(SECTION_GUI_TITLE) && !event.getClickedInventory().equals(player.getInventory())) {
                    if (sections != null && sections.getKeys(false).contains(event.getCurrentItem().getItemMeta().getDisplayName())) {
                        SectionEditorGUI.openNamedGUI(player, event.getCurrentItem().getItemMeta().getDisplayName());
                        return;
                    }
                }
                if (inventoryTitle.equals(SECTION_DEL_GUI)) {
                    if (event.getCurrentItem().getType() == Material.BARRIER) {
                        ItemStack cursorItem = event.getCursor();
                        if (cursorItem != null && cursorItem.getType() == Material.LODESTONE) {
                            String sectionNames = cursorItem.getItemMeta().getDisplayName();
                            if (sectionNames != null && sections.contains(sectionNames)) {
                                sections.set(sectionNames, null);
                                try {
                                    ConfigHandler.saveConfig();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                cursorItem.setAmount(0);
                                player.sendMessage("§aBereich erfolgreich gelöscht: " + sectionNames);
                                SectionDelGUI.updateDelGUI(player);
                                player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
                                return;
                            }
                        }
                        player.sendMessage("§cKein gültiger Bereich gefunden.");
                        return;
                    }
                }

                if (inventoryTitle.contains(key) && event.getCurrentItem().getType().equals(Material.ARROW) && event.getCurrentItem().getItemMeta().getDisplayName().equals("§8§lZurück") && !event.getClickedInventory().equals(player.getInventory())) {
                    SectionGUI.openSectionMenu(player);
                    return;
                }
            }
        }
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if (creatingGui.contains(player)) {
            BukkitRunnable timer = timers.get(player.getUniqueId());
            if (timer != null) {
                timer.cancel();
                timers.remove(player.getUniqueId());
            }

            String name = event.getMessage().replace("&", "§");
            event.setCancelled(true);
            creatingGui.remove(player);
            ConfigurationSection sections = ConfigHandler.getConfig().getConfigurationSection("sections");
            if (sections != null && sections.contains(name)) {
                player.sendMessage("§c§lEin Abschnitt mit diesem Namen existiert bereits.");
            } else {
                sectionNames.add(name);
                SectionEditorGUI.openNamedGUI(player, name);
            }
        }
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1 && sender.hasPermission(PERMISSION)) {
            completions.add("editor");
            completions.add("reload");
        }
        return completions;
    }
}
