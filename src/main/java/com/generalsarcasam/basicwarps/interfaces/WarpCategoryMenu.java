package com.generalsarcasam.basicwarps.interfaces;

import com.generalsarcasam.basicwarps.objects.Warp;
import com.generalsarcasam.basicwarps.objects.WarpCategory;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;
import org.jetbrains.annotations.NotNull;

import static com.generalsarcasam.basicwarps.utils.Constants.baseMenu;

@DefaultQualifier(NonNull.class)
public final class WarpCategoryMenu implements InventoryHolder {

    private final Inventory menu;
    WarpCategory category;

    public WarpCategoryMenu(final WarpCategory category,
                            final int pageNumber) {
        this.category = category;
        int inventorySize = 54;

        Component displayName = Component.text(StringUtils.capitalize(category.key()),
                        NamedTextColor.DARK_PURPLE)
                .decorationIfAbsent(TextDecoration.BOLD, TextDecoration.State.TRUE)
                .decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE);

        Inventory gui = baseMenu(inventorySize, this, displayName);

        //Determine how many Warps we have. If there's more than 36 Warps,
        // we'll want to have a Page feature.
        int numWarps = this.category.warps().size();

        int numPages = (int) Math.ceil(numWarps / 36.0);
        int guiItemIndex = 9;

        if (numPages == 1) {

            for (Warp warp : category.warps().values()) {
                gui.setItem(guiItemIndex, warp.warpIcon());
                guiItemIndex++;
            }

        } //else {

        //ToDo: Set Bottom Left Menu Slot to Prev Page Button
        //ToDo: Set Bottom Right Menu Slot to Next Page Button
        //ToDo: Determine Logic to Extract Warps #36 -> 71, etc...

        //}

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
