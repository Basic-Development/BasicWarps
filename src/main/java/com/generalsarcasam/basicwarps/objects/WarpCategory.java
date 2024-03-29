package com.generalsarcasam.basicwarps.objects;

import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;

import java.util.Map;

@DefaultQualifier(NonNull.class)
public final class WarpCategory {

    String key;
    Map<String, Warp> warps;
    ItemStack categoryIcon;

    public WarpCategory(final String key,
                        final Map<String, Warp> warps,
                        final ItemStack categoryIcon) {
        this.key = key;
        this.warps = warps;
        this.categoryIcon = categoryIcon;
    }

    public void key(final String key) {
        this.key = key;
    }

    public void warps(final Map<String, Warp> warpList) {
        this.warps = warpList;
    }

    public void categoryIcon(final ItemStack categoryIcon) {
        this.categoryIcon = categoryIcon;
    }

    public String key() {
        return this.key;
    }

    public Map<String, Warp> warps() {
        return this.warps;
    }

    public ItemStack categoryIcon() {
        return this.categoryIcon;
    }

    //todo: save categories to a file
    public void save() {
    }
}
