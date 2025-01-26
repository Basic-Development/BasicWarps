package com.generalsarcasam.basicwarps.commands;

import com.generalsarcasam.basicwarps.BasicWarps;
import com.generalsarcasam.basicwarps.cloud.arguments.WarpArgument;
import com.generalsarcasam.basicwarps.cloud.arguments.WarpCategoryArgument;
import com.generalsarcasam.basicwarps.interfaces.WarpsMainMenu;
import com.generalsarcasam.basicwarps.objects.Warp;
import com.generalsarcasam.basicwarps.objects.WarpCategory;
import com.generalsarcasam.basicwarps.utils.Constants;
import com.generalsarcasam.basicwarps.utils.Messages;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;
import org.incendo.cloud.Command;
import org.incendo.cloud.CommandManager;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.key.CloudKey;
import org.incendo.cloud.paper.util.sender.PlayerSource;
import org.incendo.cloud.paper.util.sender.Source;
import org.incendo.cloud.parser.standard.StringParser;
import org.incendo.cloud.processors.cache.SimpleCache;
import org.incendo.cloud.processors.confirmation.ConfirmationContext;
import org.incendo.cloud.processors.confirmation.ConfirmationManager;

import java.time.Duration;
import java.util.*;

import static org.incendo.cloud.processors.confirmation.ConfirmationManager.confirmationManager;


@DefaultQualifier(NonNull.class)
public final class WarpsCommand {

    private static final CloudKey<Player> PLAYER_KEY = CloudKey.of("player", Player.class);
    private static final CloudKey<Warp> WARP_KEY = CloudKey.of("warp", Warp.class);
    private static final CloudKey<WarpCategory> CATEGORY_KEY = CloudKey.of("category", WarpCategory.class);

    @Nullable
    public static List<UUID> warpingPlayers;
    private final ConfirmationManager<PlayerSource> confirmationManager = confirmationManager(builder ->
            builder.cache(SimpleCache.<Object, ConfirmationContext<PlayerSource>>of().keyExtractingView(source -> {
                        return source.source().getUniqueId();
                    }))
                    .noPendingCommandNotifier(sender ->
                            sender.source().sendMessage(Messages.noPendingConfirmation()))
                    .confirmationRequiredNotifier((sender, ctx) ->
                            sender.source().sendMessage(Messages.confirmationRequired(ctx)))
                    .expiration(Duration.ofSeconds(10))
    );

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

    public void register(final CommandManager<Source> commandManager) {

        Command.Builder<PlayerSource> baseCommand = commandManager
                .commandBuilder("basicwarps", "warps", "warp", "bw")
                .senderType(PlayerSource.class)
                .permission("warps.command");

        //Command: /warps
        commandManager.command(baseCommand
                .permission("warps.command.gui")
                .handler(this::handleOpenWarpsGUI));

        //Command: /warps <warp>
        commandManager.command(baseCommand
                .permission("warps.command.warp")
                .required("warp", new WarpArgument())
                .handler(this::handleWarp));

        //Command: /warps create warp <category> <name>
        commandManager.command(baseCommand
                .literal("create")
                .literal("warp")
                .permission("warps.command.create.warp")
                .required("category", new WarpCategoryArgument())
                .required("name", StringParser.stringParser())
                .handler(this::handleCreateWarp)
        );

        //Command: /warps create category <name>
        commandManager.command(baseCommand
                .literal("create")
                .literal("category")
                .required("name", StringParser.stringParser())
                .permission("warps.command.create.category")
                .handler(this::handleCreateCategory)
        );

        //Command: /warps location <warp>
        commandManager.command(baseCommand
                .literal("location")
                .permission("warps.command.location")
                .required("warp", new WarpArgument())
                .handler(this::handleUpdateLocation)
        );

        //Command: /warps warpicon <warp|category>
        commandManager.command(baseCommand
                .literal("warpicon")
                .permission("warps.command.icon")
                .required("warp", new WarpArgument())
                .handler(this::handleUpdateWarpIcon)
        );

        //Command: /warps categoryicon <warp|category>
        commandManager.command(baseCommand
                .literal("categoryicon")
                .permission("warps.command.icon")
                .required("category", new WarpCategoryArgument())
                .handler(this::handleUpdateCategoryIcon)
        );

        //Command: /warps recategorize <warp> <newCategory>
        commandManager.command(baseCommand
                .literal("recategorize")
                .permission("warps.command.category")
                .required("warp", new WarpArgument())
                .required("category", new WarpCategoryArgument())
                .handler(this::handleChangeWarpCategory)
        );

        //Command: /warps deletewarp <warp>
        //  **requires confirmation**
        commandManager.command(baseCommand
                .literal("deletewarp")
                .meta(ConfirmationManager.META_CONFIRMATION_REQUIRED, true)
                .permission("warps.command.delete")
                .required("warp", new WarpArgument())
                .handler(this::handleDeleteWarp)
        );

        //Command: /warps deletecategory <category>
        //  **requires confirmation**
        // Additionally requires the category to be empty.
        commandManager.command(baseCommand
                .literal("deletecategory")
                .meta(ConfirmationManager.META_CONFIRMATION_REQUIRED, true)
                .permission("warps.command.delete")
                .required("category", new WarpCategoryArgument())
                .handler(this::handleDeleteCategory)
        );

        //Command: /warps confirm
        commandManager.command(baseCommand
                .literal("confirm")
                .permission("warps.command.confirm")
                .handler(this.confirmationManager.createExecutionHandler())
        );

    }

    private void handleWarp(final CommandContext<PlayerSource> context) {

        Player player = context.sender().source();
        UUID uuid = player.getUniqueId();

        Warp warp = context.get(WARP_KEY);

        String permission = "warps.teleport." + warp.key();
        if (!player.hasPermission(permission)) {
            player.sendMessage(Messages.noPermissionToWarp());
            return;
        }

        Location initialLocation = player.getLocation();
        double x = initialLocation.getX();
        double y = initialLocation.getY();
        double z = initialLocation.getZ();

        boolean playerHasBypassPermission = player.hasPermission("warps.timer.bypass");

        if (playerHasBypassPermission) {
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

        //ToDo: Sort out caching of players and their locations while they're warping to prevent them from
        // moving before they warp

        runnable.runTaskLater(BasicWarps.plugin, 5 * 20);
    }

    private void handleOpenWarpsGUI(final CommandContext<PlayerSource> context) {
        Player player = context.sender().source();

        //Get back on the main thread to open an inventory for the player
        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                player.openInventory(new WarpsMainMenu().getInventory());
            }
        };
        runnable.runTask(BasicWarps.plugin);

    }

    private void handleCreateWarp(final CommandContext<PlayerSource> context) {

        Player player = context.sender().source();
        String warpName = context.get("name");
        WarpCategory category = context.get("category");

        ItemStack defaultItem = Constants.warpIcon(warpName, category.key());

        //Check ALL categories for a warp with the same key
        for (WarpCategory c : BasicWarps.categories.values()) {

            for (Warp warp : c.warps().values()) {
                if (warp.key().equalsIgnoreCase(warpName)) {
                    //Reject any duplicate warp keys
                    player.sendMessage(Messages.warpAlreadyExists(warpName));
                    return;
                }
            }
        }

        Location location = player.getLocation();

        Warp warp = new Warp(warpName, category, defaultItem, location);

        player.sendMessage(Messages.createdWarp(warp));

    }

    private void handleCreateCategory(final CommandContext<PlayerSource> context) {

        Player player = context.sender().source();

        String categoryName = context.get("name");

        ItemStack defaultItem = Constants.categoryIcon(categoryName);

        for (WarpCategory category : BasicWarps.categories.values()) {
            if (category.key().equalsIgnoreCase(categoryName)) {
                player.sendMessage(Messages.categoryAlreadyExists(categoryName));
                return;
            }
        }

        WarpCategory category = new WarpCategory(categoryName, new HashMap<>(), defaultItem);

        player.sendMessage(Messages.createdWarpCategory(category));

    }

    private void handleUpdateLocation(final CommandContext<PlayerSource> context) {
        Player player = context.sender().source();

        Warp warp = context.get(WARP_KEY);

        warp.location(player.getLocation());

        player.sendMessage(Messages.updatedWarpLocation(warp));

    }

    private void handleUpdateWarpIcon(final CommandContext<PlayerSource> context) {
        Player player = context.sender().source();

        Warp warp = context.get(WARP_KEY);

        ItemStack item = player.getInventory().getItemInMainHand();

        warp.warpIcon(item);

        player.sendMessage(Messages.updatedWarpIcon(warp));

    }

    private void handleUpdateCategoryIcon(final CommandContext<PlayerSource> context) {
        Player player = context.sender().source();

        WarpCategory category = context.get(CATEGORY_KEY);

        ItemStack item = player.getInventory().getItemInMainHand();

        category.icon(item);

        player.sendMessage(Messages.updatedCategoryIcon(category));

    }

    private void handleChangeWarpCategory(final CommandContext<PlayerSource> context) {
        Player player = context.sender().source();

        Warp warp = context.get(WARP_KEY);

        WarpCategory newCategory = context.get(CATEGORY_KEY);

        warp.category(newCategory);

        player.sendMessage(Messages.updatedWarpCategory(warp, newCategory));

    }

    private void handleDeleteWarp(final CommandContext<PlayerSource> context) {
        Player player = context.sender().source();

        Warp warp = context.get(WARP_KEY);

        WarpCategory category = warp.category();
        Map<String, Warp> warps = category.warps();
        warps.remove(warp.key());
        category.warps(warps);

        player.sendMessage(Messages.deletedWarp(warp));

    }

    private void handleDeleteCategory(final CommandContext<PlayerSource> context) {
        Player player = context.sender().source();

        WarpCategory category = context.get(CATEGORY_KEY);
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
