package com.generalsarcasam.basicwarps.interfaces.handlers;

import com.generalsarcasam.basicwarps.BasicWarps;
import com.generalsarcasam.basicwarps.interfaces.WarpCategoryMenu;
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
public final class WarpMainMenuClickHandler {

    private WarpMainMenuClickHandler() {
    }

    public static void handleClick(final Player player,
                                   final ItemStack itemClicked) {

        if (itemClicked.equals(fillerItem())) {
            return;
        }

        if (itemClicked.equals(closeMenuItem())) {
            player.closeInventory(InventoryCloseEvent.Reason.PLUGIN);
            return;
        }

        //ToDo: Handle Next Page and Previous Page Items

        //The only other items in the GUI are Warp Category Icons
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

        //Open the Warp Category Menu for this Category (starting at Page 0)
        player.closeInventory(InventoryCloseEvent.Reason.OPEN_NEW);
        player.openInventory(new WarpCategoryMenu(category, 0).getInventory());

    }

}
