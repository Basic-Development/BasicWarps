package com.generalsarcasam.basicwarps.commands;

import cloud.commandframework.Command;
import cloud.commandframework.CommandManager;
import cloud.commandframework.arguments.standard.StringArgument;
import cloud.commandframework.context.CommandContext;
import cloud.commandframework.extra.confirmation.CommandConfirmationManager;
import com.generalsarcasam.basicwarps.cloud.WarpArgument;
import com.generalsarcasam.basicwarps.cloud.WarpCategoryArgument;
import com.generalsarcasam.basicwarps.utils.Messages;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;

import java.util.concurrent.TimeUnit;

@DefaultQualifier(NonNull.class)
public final class WarpsCommand {

    public static final CommandConfirmationManager<CommandSender> CONFIRMATION_MANAGER = new CommandConfirmationManager<>(
            10L,
            TimeUnit.SECONDS,
            context -> context.getCommandContext().getSender().sendMessage(Messages.confirmGenericAction()),
            sender -> sender.sendMessage("You don't have any pending commands")
    );

    public void register(final CommandManager<CommandSender> commandManager) {

        Command.Builder<CommandSender> baseCommand = commandManager
                .commandBuilder("warps", "basicwarps", "bw")
                .senderType(Player.class)
                .permission("warps.command");


        //Command: /warps create warp <name> <category>
        commandManager.command(baseCommand
                .literal("create")
                .literal("warp")
                .permission("warps.command.create.warp")
                .argument(StringArgument.of("name"))
                .argument(new WarpCategoryArgument())
                .handler(this::handleCreateWarp)
        );

        //Command: /warps create category <name>
        commandManager.command(baseCommand
                .literal("create")
                .literal("category")
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
        commandManager.command(baseCommand
                .literal("icon")
                .permission("warps.command.icon")
                .argument(new WarpCategoryArgument())
                .handler(this::handleUpdateCategoryIcon)
        );

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
        commandManager.command(baseCommand
                .literal("delete")
                .meta(CommandConfirmationManager.META_CONFIRMATION_REQUIRED, true)
                .permission("warps.command.delete")
                .argument(new WarpCategoryArgument())
                .handler(this::handleDeleteCategory)
        );

        //Command: /warps confirm
        commandManager.command(baseCommand
                .literal("confirm")
                .permission("warps.command.confirm")
                .handler(CONFIRMATION_MANAGER.createConfirmationExecutionHandler())
        );

    }

    private void handleCreateWarp(final CommandContext<CommandSender> context) {

    }

    private void handleCreateCategory(final CommandContext<CommandSender> context) {

    }

    private void handleUpdateLocation(final CommandContext<CommandSender> context) {

    }

    private void handleUpdateWarpIcon(final CommandContext<CommandSender> context) {

    }

    private void handleUpdateCategoryIcon(final CommandContext<CommandSender> context) {

    }

    private void handleUpdateWarpCategory(final CommandContext<CommandSender> context) {

    }

    private void handleDeleteWarp(final CommandContext<CommandSender> context) {

    }

    private void handleDeleteCategory(final CommandContext<CommandSender> context) {

    }

}
