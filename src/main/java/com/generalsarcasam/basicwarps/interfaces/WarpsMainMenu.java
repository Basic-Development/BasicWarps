package com.generalsarcasam.basicwarps.interfaces;

import com.generalsarcasam.basicwarps.BasicWarps;
import com.generalsarcasam.basicwarps.objects.WarpCategory;
import com.generalsarcasam.basicwarps.utils.Constants;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static com.generalsarcasam.basicwarps.utils.Constants.baseMenu;

@DefaultQualifier(NonNull.class)
public final class WarpsMainMenu implements InventoryHolder {

    public final int pageNumber;
    private final Inventory menu;

    public WarpsMainMenu(final int pageNumber,
                         final Player viewer) {
        this.pageNumber = pageNumber;

        int inventorySize = 54;
        Inventory gui = baseMenu(inventorySize, this);

        List<WarpCategory> categoryList = new ArrayList<>();
        for (WarpCategory category : BasicWarps.categories.values()) {
            if (!viewer.hasPermission(category.permission())) {
                // The Player doesn't have Permission to use Warps in this Category
                continue;
            }

            categoryList.add(category);

        }

        //Determine which Categories we're interested in.
        boolean nextPage = true;

        int startingIndex = (36 * (pageNumber - 1));
        int endingIndex = startingIndex + 36;
        if (endingIndex > categoryList.size()) {
            // There are more slots left than Categories
            endingIndex = categoryList.size();
            nextPage = false;
        }

        int guiIndex = 9;
        for (int i = startingIndex; i < endingIndex; i++) {

            WarpCategory category = categoryList.get(i);

            ItemStack menuItem = category.icon();

            gui.setItem(guiIndex, menuItem);

            guiIndex++;
        }

        // Add buttons for GUI Navigation
        if (pageNumber == 1) {
            //Put next page button only, if there are enough items to warrant another page.
            if (nextPage) {
                gui.setItem(inventorySize - 1, Constants.NEXT_PAGE_ITEM);
            }
        } else {
            // Determine how many Categories there are, and if there aren't enough to warrant a next page,
            // then only have a previous page button.
            if (nextPage) {
                gui.setItem(inventorySize - 1, Constants.NEXT_PAGE_ITEM);
            }
            gui.setItem(inventorySize - 9, Constants.PREVIOUS_PAGE_ITEM);
        }

        //Finalize the GUI initialization
        this.menu = gui;

    }

    @Override
    public @NotNull Inventory getInventory() {
        return this.menu;
    }

}
