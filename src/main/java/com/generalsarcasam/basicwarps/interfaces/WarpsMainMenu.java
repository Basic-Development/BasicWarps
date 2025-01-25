package com.generalsarcasam.basicwarps.interfaces;

import com.generalsarcasam.basicwarps.utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;
import org.jetbrains.annotations.NotNull;

import static com.generalsarcasam.basicwarps.utils.Constants.fillerItem;

@DefaultQualifier(NonNull.class)
public final class WarpsMainMenu implements InventoryHolder {

    private final Inventory menu;

    public WarpsMainMenu() {

        int inventorySize = 54;

        Inventory gui = Bukkit.createInventory(this, inventorySize, Messages.PREFIX);

        //Fill the GUI with the Filler Item
        for (int i = 0; i < inventorySize; i++) {
            gui.setItem(i, fillerItem());
        }

        //ToDo: Add Warp Category Icons to the Menu

        //Finalize the GUI initialization
        this.menu = gui;

    }

    @Override
    public @NotNull Inventory getInventory() {
        return this.menu;
    }
}
