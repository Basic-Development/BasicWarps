package com.generalsarcasam.basicwarps;

import cloud.commandframework.CommandManager;
import cloud.commandframework.bukkit.CloudBukkitCapabilities;
import cloud.commandframework.execution.AsynchronousCommandExecutionCoordinator;
import cloud.commandframework.paper.PaperCommandManager;
import com.generalsarcasam.basicwarps.commands.WarpsCommand;
import com.generalsarcasam.basicwarps.listeners.InventoryClickEventListener;
import com.generalsarcasam.basicwarps.objects.WarpCategory;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public final class BasicWarps extends JavaPlugin {

    public static final MiniMessage MINI = MiniMessage.miniMessage();
    public static NamespacedKey warpNameKey;
    public static NamespacedKey warpCategoryKey;

    public static Map<String, WarpCategory> categories;

    @Override
    public void onEnable() {
        //Create namespace key for Warp & Category items
        warpNameKey = NamespacedKey.fromString("basic_warps_name", this);
        warpCategoryKey = NamespacedKey.fromString("basic_warps_category", this);

        //Create objects
        categories = new HashMap<>();

        //Register Commands
        CommandManager<CommandSender> commandManager = this.createCommandManager();
        new WarpsCommand().register(commandManager);

        //Register Listeners
        PluginManager pm = this.getServer().getPluginManager();
        pm.registerEvents(new InventoryClickEventListener(), this);
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
