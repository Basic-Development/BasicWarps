package com.generalsarcasam.basicwarps.listeners;

import com.generalsarcasam.basicwarps.interfaces.WarpCategoryMenu;
import com.generalsarcasam.basicwarps.interfaces.WarpsMainMenu;
import com.generalsarcasam.basicwarps.interfaces.handlers.WarpCategoryMenuClickHandler;
import com.generalsarcasam.basicwarps.interfaces.handlers.WarpMainMenuClickHandler;
import com.generalsarcasam.basicwarps.objects.WarpCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;
import org.jetbrains.annotations.Nullable;

@DefaultQualifier(NonNull.class)
public final class InventoryClickEventListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onInventoryClickEvent(final InventoryClickEvent event) {

        @Nullable Inventory inventory = event.getClickedInventory();
        if (inventory == null) {
            return;
        }
        @Nullable InventoryHolder holder = inventory.getHolder();
        if (holder == null) {
            return;
        }

        @Nullable ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null) {
            return;
        }

        //We are handling the event, and don't want the players to get any items
        event.setCancelled(true);

        switch (holder) {
            case WarpsMainMenu menu -> {

                int pageNumber = menu.pageNumber;

                Player player = (Player) event.getWhoClicked();
                WarpMainMenuClickHandler.handleClick(player, clickedItem, pageNumber);

            }
            case WarpCategoryMenu menu -> {

                int pageNumber = menu.pageNumber;
                WarpCategory category = menu.category;

                Player player = (Player) event.getWhoClicked();
                WarpCategoryMenuClickHandler.handleClick(
                        category,
                        player,
                        clickedItem,
                        pageNumber
                );
            }
            default -> {
            }
        }

    }

}
