package com.generalsarcasam.basicwarps.commands;

import cloud.commandframework.Command;
import cloud.commandframework.CommandManager;
import cloud.commandframework.arguments.standard.StringArgument;
import cloud.commandframework.context.CommandContext;
import cloud.commandframework.extra.confirmation.CommandConfirmationManager;
import com.generalsarcasam.basicwarps.BasicWarps;
import com.generalsarcasam.basicwarps.cloud.WarpArgument;
import com.generalsarcasam.basicwarps.cloud.WarpCategoryArgument;
import com.generalsarcasam.basicwarps.objects.Warp;
import com.generalsarcasam.basicwarps.objects.WarpCategory;
import com.generalsarcasam.basicwarps.utils.Constants;
import com.generalsarcasam.basicwarps.utils.Messages;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;

import java.util.*;
import java.util.concurrent.TimeUnit;

@DefaultQualifier(NonNull.class)
public final class WarpsCommand {

    public static final CommandConfirmationManager<CommandSender> CONFIRMATION_MANAGER = new CommandConfirmationManager<>(
            10L,
            TimeUnit.SECONDS,
            context -> context.getCommandContext().getSender().sendMessage(Messages.confirmGenericAction()),
            sender -> sender.sendMessage("You don't have any pending commands")
    );

    @Nullable
    public static List<UUID> warpingPlayers;

    public WarpsCommand() {
        if (warpingPlayers == null) {
            warpingPlayers = new ArrayList<>();
        }
    }

    public static List<UUID> getWarpingPlayers() {
        return Objects.requireNonNullElse(warpingPlayers, new ArrayList<>());
    }

    public static void setWarpingPlayers(final List<UUID> warpingPlayers) {
        WarpsCommand.warpingPlayers = warpingPlayers;
    }

    public void register(final CommandManager<CommandSender> commandManager) {

        Command.Builder<CommandSender> baseCommand = commandManager
                .commandBuilder("basicwarps", "warps", "bw")
                .senderType(Player.class)
                .permission("warps.command");

        //Command: /warp <warp>
        commandManager.command(baseCommand
                .permission("warps.command.warp")
                .argument(new WarpArgument())
                .handler(this::handleWarp));

        //Command: /warps create warp <category> <name>
        commandManager.command(baseCommand
                .literal("create")
                .literal("warp")
                .permission("warps.command.create.warp")
                .argument(new WarpCategoryArgument())
                .argument(StringArgument.of("name"))
                .handler(this::handleCreateWarp)
        );

        //Command: /warps create category <name>
        commandManager.command(baseCommand
                .literal("create")
                .literal("category")
                .argument(StringArgument.of("name"))
                .permission("warps.command.create.category")
                .handler(this::handleCreateCategory)
        );

        //Command: /warps location <warp>
        commandManager.command(baseCommand
                .literal("location")
                .permission("warps.command.location")
                .argument(new WarpArgument())
                .handler(this::handleUpdateLocation)
        );

        //Command: /warps icon <warp|category>
        commandManager.command(baseCommand
                .literal("icon")
                .permission("warps.command.icon")
                .argument(new WarpArgument())
                .handler(this::handleUpdateWarpIcon)
        );

        //Command: /warps icon <warp|category>
        //commandManager.command(baseCommand
        //        .literal("icon")
        //        .permission("warps.command.icon")
        //        .argument(new WarpCategoryArgument())
        //        .handler(this::handleUpdateCategoryIcon)
        //);

        //Command: /warps category <warp> <category>
        commandManager.command(baseCommand
                .literal("category")
                .permission("warps.command.category")
                .argument(new WarpArgument())
                .argument(new WarpCategoryArgument())
                .handler(this::handleUpdateWarpCategory)
        );

        //Command: /warps delete <warp>
        //  **requires confirmation**
        commandManager.command(baseCommand
                .literal("delete")
                .meta(CommandConfirmationManager.META_CONFIRMATION_REQUIRED, true)
                .permission("warps.command.delete")
                .argument(new WarpArgument())
                .handler(this::handleDeleteWarp)
        );

        //Command: /warps delete <category>
        //  **requires confirmation**
        // Additionally requires the category to be empty.
        //commandManager.command(baseCommand
        //        .literal("delete")
        //        .meta(CommandConfirmationManager.META_CONFIRMATION_REQUIRED, true)
        //        .permission("warps.command.delete")
        //        .argument(new WarpCategoryArgument())
        //        .handler(this::handleDeleteCategory)
        //);

        //Command: /warps confirm
        commandManager.command(baseCommand
                .literal("confirm")
                .permission("warps.command.confirm")
                .handler(CONFIRMATION_MANAGER.createConfirmationExecutionHandler())
        );

    }

    private void handleWarp(final CommandContext<CommandSender> context) {

        CommandSender sender = context.getSender();
        Player player = (Player) sender;
        UUID uuid = player.getUniqueId();

        Warp warp = context.get("warp");

        String permission = "warps.teleport." + warp.key();
        if (!player.hasPermission(permission)) {
            player.sendMessage(Messages.noPermissionToWarp());
            return;
        }

        Location initialLocation = player.getLocation();
        double x = initialLocation.getX();
        double y = initialLocation.getY();
        double z = initialLocation.getZ();

        boolean hasTimerBypass = player.hasPermission("warps.timer.bypass");

        if (hasTimerBypass) {
            //Get back on main thread to teleport player instantly
            BukkitRunnable runnable = new BukkitRunnable() {
                @Override
                public void run() {
                    player.teleport(warp.location());
                    player.sendMessage(Messages.teleported(warp));
                }
            };
            runnable.runTask(BasicWarps.plugin);
            return;
        }

        List<UUID> warpingPlayerList = WarpsCommand.getWarpingPlayers();
        warpingPlayerList.add(uuid);
        WarpsCommand.setWarpingPlayers(warpingPlayerList);

        //Get back on main thread to teleport player after 5s delay
        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                List<UUID> warpList = WarpsCommand.getWarpingPlayers();

                if (warpList.contains(uuid)) {
                    //They haven't had their warp cancelled.
                    player.teleport(warp.location());
                    player.sendMessage(Messages.teleported(warp));
                }

                //They warped, so remove them from the waiting-to-warp list
                warpList.remove(uuid);
                WarpsCommand.setWarpingPlayers(warpList);

            }
        };

        runnable.runTaskLater(BasicWarps.plugin, 5 * 20);
    }

    private void handleOpenWarpsGUI(final CommandContext<CommandSender> context) {
    }

    private void handleCreateWarp(final CommandContext<CommandSender> context) {

        CommandSender sender = context.getSender();
        Player player = (Player) sender;

        String warpName = context.get("name");
        ItemStack defaultItem = Constants.warpIcon(warpName);

        WarpCategory category = context.get("category");

        Location location = player.getLocation();

        Warp warp = new Warp(warpName, category, defaultItem, location);

        player.sendMessage(Messages.createdWarp(warp));

    }

    private void handleCreateCategory(final CommandContext<CommandSender> context) {

        CommandSender sender = context.getSender();
        Player player = (Player) sender;

        String categoryName = context.get("name");
        ItemStack defaultItem = Constants.categoryIcon(categoryName);

        WarpCategory category = new WarpCategory(categoryName, new HashMap<>(), defaultItem);

        player.sendMessage(Messages.createdWarpCategory(category));

    }

    private void handleUpdateLocation(final CommandContext<CommandSender> context) {
        CommandSender sender = context.getSender();
        Player player = (Player) sender;

        Warp warp = context.get("warp");

        warp.location(player.getLocation());

        player.sendMessage(Messages.updatedWarpLocation(warp));

    }

    private void handleUpdateWarpIcon(final CommandContext<CommandSender> context) {
        CommandSender sender = context.getSender();
        Player player = (Player) sender;

        Warp warp = context.get("warp");

        ItemStack item = player.getInventory().getItemInMainHand();

        warp.warpIcon(item);

        player.sendMessage(Messages.updatedWarpIcon(warp));

    }

    private void handleUpdateCategoryIcon(final CommandContext<CommandSender> context) {
        CommandSender sender = context.getSender();
        Player player = (Player) sender;

        WarpCategory category = context.get("category");

        ItemStack item = player.getInventory().getItemInMainHand();

        category.icon(item);

        player.sendMessage(Messages.updatedCategoryIcon(category));

    }

    private void handleUpdateWarpCategory(final CommandContext<CommandSender> context) {
        CommandSender sender = context.getSender();
        Player player = (Player) sender;

        Warp warp = context.get("warp");

        WarpCategory newCategory = context.get("category");

        warp.category(newCategory);

        player.sendMessage(Messages.updatedWarpCategory(warp, newCategory));

    }

    private void handleDeleteWarp(final CommandContext<CommandSender> context) {
        CommandSender sender = context.getSender();
        Player player = (Player) sender;

        Warp warp = context.get("warp");

        WarpCategory category = warp.category();
        Map<String, Warp> warps = category.warps();
        warps.remove(warp.key());
        category.warps(warps);

        player.sendMessage(Messages.deletedWarp(warp));

    }

    private void handleDeleteCategory(final CommandContext<CommandSender> context) {
        CommandSender sender = context.getSender();
        Player player = (Player) sender;

        WarpCategory category = context.get("category");
        Map<String, Warp> warps = category.warps();

        if (!warps.isEmpty()) {
            player.sendMessage(Messages.categoryNotEmpty(category));
            return;
        }

        BasicWarps.categories.remove(category.key());
        //todo: remove the file

        player.sendMessage(Messages.deletedCategory(category));

    }

}
