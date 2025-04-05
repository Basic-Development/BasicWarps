package com.generalsarcasam.basicwarps.interfaces.handlers;

import com.generalsarcasam.basicwarps.BasicWarps;
import com.generalsarcasam.basicwarps.interfaces.WarpCategoryMenu;
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

import static com.generalsarcasam.basicwarps.utils.Constants.CLOSE_MENU_ITEM;
import static com.generalsarcasam.basicwarps.utils.Constants.FILLER_ITEM;
import static com.generalsarcasam.basicwarps.utils.Constants.NEXT_PAGE_ITEM;
import static com.generalsarcasam.basicwarps.utils.Constants.PREVIOUS_PAGE_ITEM;

@DefaultQualifier(NonNull.class)
public final class WarpCategoryMenuClickHandler {

    private WarpCategoryMenuClickHandler() {
    }

    public static void handleClick(final WarpCategory category,
                                   final Player player,
                                   final ItemStack itemClicked,
                                   final int pageNumber) {

        if (itemClicked.equals(FILLER_ITEM)) {
            return;
        }

        if (itemClicked.equals(CLOSE_MENU_ITEM)) {
            player.closeInventory(InventoryCloseEvent.Reason.PLUGIN);
            return;
        }

        if (itemClicked.equals(NEXT_PAGE_ITEM)) {
            player.closeInventory(InventoryCloseEvent.Reason.OPEN_NEW);
            player.openInventory(
                    new WarpCategoryMenu(category, pageNumber + 1, player)
                            .getInventory()
            );
            return;
        }

        if (itemClicked.equals(PREVIOUS_PAGE_ITEM)) {
            player.closeInventory(InventoryCloseEvent.Reason.OPEN_NEW);
            player.openInventory(
                    new WarpCategoryMenu(category, pageNumber - 1, player)
                            .getInventory()
            );
            return;
        }

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

    }

}
