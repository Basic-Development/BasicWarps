package com.generalsarcasam.basicwarps.utils;

import com.generalsarcasam.basicwarps.BasicWarps;
import com.generalsarcasam.basicwarps.objects.Warp;
import com.generalsarcasam.basicwarps.objects.WarpCategory;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;
import org.incendo.cloud.component.CommandComponent;
import org.incendo.cloud.paper.util.sender.PlayerSource;
import org.incendo.cloud.processors.confirmation.ConfirmationContext;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static net.kyori.adventure.text.format.TextDecoration.BOLD;
import static net.kyori.adventure.text.format.TextDecoration.ITALIC;
import static net.kyori.adventure.text.format.TextDecoration.State.FALSE;

@DefaultQualifier(NonNull.class)
public final class Messages {

    public static final Component PREFIX = BasicWarps.MINI.deserialize(
            Objects.requireNonNullElse(BasicWarps.config.getString("plugin-prefix"),
                    "<dark_gray>[</dark_gray><dark_aqua><bold>Warps</bold></dark_aqua>"
                            + "<dark_gray>]</dark_gray> ")
    ).decorationIfAbsent(BOLD, FALSE).decorationIfAbsent(ITALIC, FALSE);

    private static final Component ADMIN_PREFIX = BasicWarps.MINI.deserialize(
            Objects.requireNonNullElse(BasicWarps.config.getString("admin-prefix"),
                    "<dark_gray>[</dark_gray><dark_aqua><bold>WarpsAdmin</bold></dark_aqua>"
                            + "<dark_gray>]</dark_gray> ")
    ).decorationIfAbsent(BOLD, FALSE).decorationIfAbsent(ITALIC, FALSE);

    private static final TextColor MESSAGE_COLOR = TextColor.color(0x50ffc0);
    private static final TextColor DARKER_COLOR = TextColor.color(0x349170);
    private static final TextColor ERROR_COLOR = TextColor.color(0xff3d3d);

    private Messages() {
    }

    private static Component format(final String text,
                                    final TextColor color) {
        return PREFIX.append(Component.text(text, color)
                .decorationIfAbsent(BOLD, FALSE)
                .decorationIfAbsent(ITALIC, FALSE));
    }

    private static Component formatAdmin(final String text,
                                         final TextColor color) {
        return ADMIN_PREFIX.append(Component.text(text, color)
                .decorationIfAbsent(BOLD, FALSE)
                .decorationIfAbsent(ITALIC, FALSE));
    }

    public static Component createdWarpCategory(final WarpCategory category) {
        return formatAdmin("Created a new Warp Category: " + category.key(),
                MESSAGE_COLOR);
    }

    public static Component createdWarp(final Warp warp) {
        return formatAdmin("Created a new Warp: " + warp.key() + " in Category " + warp.category().key(),
                MESSAGE_COLOR);
    }

    public static Component confirmGenericAction() {
        return formatAdmin("This action requires confirmation! To confirm, type /warps confirm", ERROR_COLOR);
    }

    public static Component updatedWarpLocation(final Warp warp) {
        return formatAdmin("Updated the location for warp " + warp.key() + ".", MESSAGE_COLOR);
    }

    public static Component updatedWarpIcon(final Warp warp) {
        return formatAdmin("Updated the icon for warp " + warp.key() + ".", MESSAGE_COLOR);
    }

    public static Component updatedCategoryIcon(final WarpCategory category) {
        return formatAdmin("Updated the icon for category " + category.key() + ".", MESSAGE_COLOR);
    }

    public static Component updatedWarpCategory(final Warp warp,
                                                final WarpCategory newCategory) {
        return formatAdmin("Updated the category for warp " + warp.key()
                + " to " + newCategory.key(), MESSAGE_COLOR);
    }

    public static Component deletedWarp(final Warp warp) {
        return formatAdmin("Deleted the warp " + warp.key()
                + " from category " + warp.category().key(), MESSAGE_COLOR);
    }

    public static Component categoryNotEmpty(final WarpCategory category) {
        return formatAdmin("You can't delete " + category.key() + ", as it still has warps in it! "
                + "Delete the warps or move them to other categories before deleting the category!", ERROR_COLOR);
    }

    public static Component deletedCategory(final WarpCategory category) {
        return formatAdmin("Deleted the category " + category.key(), MESSAGE_COLOR);
    }

    public static Component teleportCancelled(final String cancelReason) {
        return format("Your warp was cancelled! " + cancelReason, ERROR_COLOR);
    }

    public static Component teleported(final Warp warp) {
        return format("Warped to " + warp.key(), MESSAGE_COLOR);
    }

    public static @NotNull Component teleportInitiated(final Warp warp) {
        return format("Warping to " + warp.key() + "! Please wait for "
                + BasicWarps.teleportDelay + " seconds, and don't move!", DARKER_COLOR);
    }

    public static Component noPermissionToWarp() {
        return format("You don't have permission to perform this command!", ERROR_COLOR);
    }

    public static Component categoryAlreadyExists(final String categoryName) {
        return format("A category already exists with the name " + categoryName, ERROR_COLOR);
    }

    public static Component warpAlreadyExists(final String warpName) {
        return format("A warp already exists with the name " + warpName, ERROR_COLOR);
    }

    public static Component noPendingConfirmation() {
        return format("No pending actions to confirm.", DARKER_COLOR);
    }

    public static Component confirmationRequired(final ConfirmationContext<PlayerSource> context) {

        CommandComponent<PlayerSource> subcommand = context.commandContext().command().components().get(1);

        switch (subcommand.name()) {

            case ("deletewarp") -> {
                return format("Deleting a warp requires confirmation! To confirm, type /basicwarps confirm",
                        DARKER_COLOR);
            }
            case ("deletecategory") -> {
                return format("Deleting a Warp Category requires confirmation! To confirm, type /basicwarps confirm",
                        DARKER_COLOR);
            }
            default -> {
                return confirmGenericAction();
            }
        }
    }

    public static Component invalidWarpCategory(final String categoryName) {
        return format("Something odd happened. Failed to get a Category with the name "
                + categoryName + "! Please notify an admin.", ERROR_COLOR);
    }

    public static Component invalidWarp(final String warpName) {
        return format("Something odd happened. Failed to get a Warp with the name "
                + warpName + "! Please notify an admin.", ERROR_COLOR);
    }
}
