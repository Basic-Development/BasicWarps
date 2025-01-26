package com.generalsarcasam.basicwarps.interfaces.handlers;

import com.generalsarcasam.basicwarps.BasicWarps;
import com.generalsarcasam.basicwarps.objects.Warp;
import com.generalsarcasam.basicwarps.objects.WarpCategory;
import com.generalsarcasam.basicwarps.utils.Messages;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;
import org.jetbrains.annotations.Nullable;

import static com.generalsarcasam.basicwarps.utils.Constants.closeMenuItem;
import static com.generalsarcasam.basicwarps.utils.Constants.fillerItem;

@DefaultQualifier(NonNull.class)
public final class WarpCategoryMenuClickHandler {

    private WarpCategoryMenuClickHandler() {
    }

    public static void handleClick(final Player player,
                                   final ItemStack itemClicked) {

        if (itemClicked.equals(fillerItem())) {
            return;
        }

        if (itemClicked.equals(closeMenuItem())) {
            player.closeInventory(InventoryCloseEvent.Reason.PLUGIN);
        }

        //ToDo: Handle Next Page and Previous Page Items

        //The only other items in the GUI are Warp Icons
        ItemMeta meta = itemClicked.getItemMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();

        //Get the Name of the Warp Category
        String categoryName = pdc.getOrDefault(BasicWarps.warpCategoryKey, PersistentDataType.STRING, "none");
        if (categoryName.equalsIgnoreCase("none")) {
            player.closeInventory(InventoryCloseEvent.Reason.PLUGIN);
            player.sendMessage(Messages.invalidWarpCategory(categoryName));
            return;
        }

        //Get the Warp Category
        @Nullable WarpCategory category = null;
        for (WarpCategory cat : BasicWarps.categories.values()) {
            if (cat.key().equalsIgnoreCase(categoryName)) {
                category = cat;
                break;
            }
        }
        if (category == null) {
            player.closeInventory(InventoryCloseEvent.Reason.PLUGIN);
            player.sendMessage(Messages.invalidWarpCategory(categoryName));
            return;
        }

        String warpName = pdc.getOrDefault(BasicWarps.warpNameKey, PersistentDataType.STRING, "none");
        if (!category.warps().containsKey(warpName)) {
            player.closeInventory(InventoryCloseEvent.Reason.PLUGIN);
            player.sendMessage(Messages.invalidWarp(warpName));
        }

        Warp warp = category.warps().get(warpName);

        //Process Command to Warp Player to Warp Location
        player.closeInventory(InventoryCloseEvent.Reason.PLUGIN);
        BasicWarps.plugin.getServer().dispatchCommand(
                player, "basicwarps:warps  " + warp.key()
        );
        player.sendMessage(Messages.teleportInitiated(warp));

    }

}
