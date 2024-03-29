package com.generalsarcasam.basicwarps.objects;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;

@DefaultQualifier(NonNull.class)
public final class Warp {

    private String key;
    private WarpCategory category;
    private ItemStack warpIcon;
    private Location location;

    public Warp(final String key,
                final WarpCategory category,
                final ItemStack warpIcon,
                final Location location) {
        this.key = key;
        this.category = category;
        this.warpIcon = warpIcon;
        this.location = location;
    }

    public void key(final String key) {
        this.key = key;
    }

    public void category(final WarpCategory category) {
        this.category = category;
    }

    public void warpIcon(final ItemStack warpIcon) {
        this.warpIcon = warpIcon;
    }

    public void location(final Location location) {
        this.location = location;
    }

    public String key() {
        return this.key;
    }

    public WarpCategory category() {
        return this.category;
    }

    public ItemStack warpIcon() {
        return this.warpIcon;
    }

    public Location location() {
        return this.location;
    }

    //invokes the WarpCategory#save method for the category that this warp belongs to
    public void save() {
        this.category().save();
    }
}
