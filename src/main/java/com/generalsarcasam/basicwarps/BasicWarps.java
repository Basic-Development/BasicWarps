package com.generalsarcasam.basicwarps;

import com.generalsarcasam.basicwarps.commands.WarpsCommand;
import com.generalsarcasam.basicwarps.io.DataTools;
import com.generalsarcasam.basicwarps.listeners.EntityDamageEventListener;
import com.generalsarcasam.basicwarps.listeners.InventoryClickEventListener;
import com.generalsarcasam.basicwarps.listeners.PlayerMoveEventListener;
import com.generalsarcasam.basicwarps.objects.WarpCategory;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.incendo.cloud.CommandManager;
import org.incendo.cloud.execution.ExecutionCoordinator;
import org.incendo.cloud.paper.PaperCommandManager;
import org.incendo.cloud.paper.util.sender.PaperSimpleSenderMapper;
import org.incendo.cloud.paper.util.sender.Source;

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

    public static int teleportDelay;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        config = getConfig();

        plugin = this;
        logger = this.getLogger();

        teleportDelay = BasicWarps.config.getInt("teleport-delay");
        if (teleportDelay == 0) {
            logger.warning("Overwrote Teleport Delay of 0 to Default Value (5 Seconds). To bypass the warps"
                    + "delay, assign the permission warps.timer.bypass to the player or permission group you"
                    + "want to allow to skip the timer!");
            teleportDelay = 5;
        }

        PlayerMoveEventListener.playerLocationMap = new HashMap<>();

        //Create namespace key for Warp & Category items
        warpNameKey = NamespacedKey.fromString("basic_warps_name", this);
        warpCategoryKey = NamespacedKey.fromString("basic_warps_category", this);

        //Create objects
        categories = new HashMap<>();

        //Load the Saved Warps
        DataTools.loadWarps();

        //Register Commands
        CommandManager<Source> manager = PaperCommandManager.builder(PaperSimpleSenderMapper.simpleSenderMapper())
                .executionCoordinator(ExecutionCoordinator.asyncCoordinator())
                .buildOnEnable(this);

        new WarpsCommand().register(manager);

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

}
