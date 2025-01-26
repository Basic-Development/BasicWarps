package com.generalsarcasam.basicwarps.interfaces;

import com.generalsarcasam.basicwarps.BasicWarps;
import com.generalsarcasam.basicwarps.objects.WarpCategory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;
import org.jetbrains.annotations.NotNull;

import static com.generalsarcasam.basicwarps.utils.Constants.baseMenu;

@DefaultQualifier(NonNull.class)
public final class WarpsMainMenu implements InventoryHolder {

    private final Inventory menu;

    public WarpsMainMenu() {

        int inventorySize = 54;
        Inventory gui = baseMenu(inventorySize, this);

        //Determine how many Warp Categories we have. If there's more than 36 Categories,
        // we'll want to have a Page feature.
        int numCategories = BasicWarps.categories.size();

        int numPages = (int) Math.ceil(numCategories / 36.0);
        int guiItemIndex = 9;

        if (numPages == 1) {

            for (WarpCategory category : BasicWarps.categories.values()) {
                gui.setItem(guiItemIndex, category.icon());
                guiItemIndex++;
            }

        } //else {

        //ToDo: Set Bottom Left Menu Slot to Prev Page Button
        //ToDo: Set Bottom Right Menu Slot to Next Page Button
        //ToDo: Determine Logic to Extract Categories #36 -> 71, etc...

        //}

        //Finalize the GUI initialization
        this.menu = gui;

    }

    @Override
    public @NotNull Inventory getInventory() {
        return this.menu;
    }

}
