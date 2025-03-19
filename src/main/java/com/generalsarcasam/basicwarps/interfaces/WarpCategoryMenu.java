package com.generalsarcasam.basicwarps.interfaces;

import com.generalsarcasam.basicwarps.objects.Warp;
import com.generalsarcasam.basicwarps.objects.WarpCategory;
import com.generalsarcasam.basicwarps.utils.Constants;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.apache.commons.lang3.StringUtils;
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
public final class WarpCategoryMenu implements InventoryHolder {

    private final Inventory menu;
    public WarpCategory category;
    public int pageNumber;

    public WarpCategoryMenu(final WarpCategory category,
                            final int pageNumber) {
        this.category = category;
        this.pageNumber = pageNumber;
        int inventorySize = 54;

        Component displayName = Component.text(StringUtils.capitalize(category.key()),
                        NamedTextColor.DARK_PURPLE)
                .decorationIfAbsent(TextDecoration.BOLD, TextDecoration.State.TRUE)
                .decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE);

        Inventory gui = baseMenu(inventorySize, this, displayName);

        List<Warp> warpList = new ArrayList<>(category.warps().values());

        //Determine which Warps we're interested in.
        boolean nextPage = true;

        int startingIndex = (36 * (pageNumber - 1));
        int endingIndex = startingIndex + 36;
        if (endingIndex > warpList.size()) {
            // There are more slots left than Warps in the Category
            endingIndex = warpList.size();
            nextPage = false;
        }

        int guiIndex = 9;
        for (int i = startingIndex; i < endingIndex; i++) {

            Warp warp = warpList.get(i);

            ItemStack menuItem = warp.warpIcon();

            gui.setItem(guiIndex, menuItem);

            guiIndex++;
        }

        // Add buttons for GUI Navigation
        if (pageNumber == 1) {
            if (nextPage) {
                gui.setItem(inventorySize - 1, Constants.NEXT_PAGE_ITEM);
            }
        } else {
            // Determine how many Warps there are, and if there aren't enough to warrant a next page,
            // then only have a previous page button.
            if (nextPage) {
                gui.setItem(inventorySize - 1, Constants.NEXT_PAGE_ITEM);
            }
            gui.setItem(inventorySize - 9, Constants.PREVIOUS_PAGE_ITEM);
        }

        //Finalize the GUI initialization
        this.menu = gui;

    }

    public WarpCategory category() {
        return this.category;
    }

    @Override
    public @NotNull Inventory getInventory() {
        return this.menu;
    }
}
