package com.generalsarcasam.basicwarps;

import cloud.commandframework.CommandManager;
import cloud.commandframework.bukkit.CloudBukkitCapabilities;
import cloud.commandframework.execution.AsynchronousCommandExecutionCoordinator;
import cloud.commandframework.paper.PaperCommandManager;
import com.generalsarcasam.basicwarps.commands.WarpsCommand;
import com.generalsarcasam.basicwarps.listeners.EntityDamageEventListener;
import com.generalsarcasam.basicwarps.listeners.InventoryClickEventListener;
import com.generalsarcasam.basicwarps.listeners.PlayerMoveEventListener;
import com.generalsarcasam.basicwarps.objects.WarpCategory;
import com.generalsarcasam.basicwarps.io.DataTools;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public final class BasicWarps extends JavaPlugin {

    public static final MiniMessage MINI = MiniMessage.miniMessage();
    public static NamespacedKey warpNameKey;
    public static NamespacedKey warpCategoryKey;

    public static FileConfiguration config;
    public static Logger logger;

    public static Map<String, WarpCategory> categories;
    public static Plugin plugin;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        config = getConfig();

        plugin = this;
        logger = this.getLogger();

        //Create namespace key for Warp & Category items
        warpNameKey = NamespacedKey.fromString("basic_warps_name", this);
        warpCategoryKey = NamespacedKey.fromString("basic_warps_category", this);

        //Create objects
        categories = new HashMap<>();

        //Load the Saved Warps
        DataTools.loadWarps();

        //Register Commands
        CommandManager<CommandSender> commandManager = this.createCommandManager();
        new WarpsCommand().register(commandManager);

        //Register Listeners
        PluginManager pm = this.getServer().getPluginManager();
        pm.registerEvents(new InventoryClickEventListener(), this);
        pm.registerEvents(new PlayerMoveEventListener(), this);
        pm.registerEvents(new EntityDamageEventListener(), this);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        for (WarpCategory category : categories.values()) {
            category.save();
        }
    }

    private CommandManager<CommandSender> createCommandManager() {
        PaperCommandManager<CommandSender> commandManager;

        try {
            commandManager = PaperCommandManager.createNative(
                    this,
                    AsynchronousCommandExecutionCoordinator.<CommandSender>builder().withAsynchronousParsing().build()
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if (commandManager.hasCapability(CloudBukkitCapabilities.ASYNCHRONOUS_COMPLETION)) {
            commandManager.registerAsynchronousCompletions();
        }

        return commandManager;
    }

}
