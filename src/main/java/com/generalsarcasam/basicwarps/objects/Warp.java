package com.generalsarcasam.basicwarps.objects;

import com.generalsarcasam.basicwarps.BasicWarps;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;

import java.util.Map;

@DefaultQualifier(NonNull.class)
public final class Warp {

    private String key;
    private WarpCategory category;
    private ItemStack warpIcon;
    private Location location;
    private long priority;

    public Warp(final String key,
                final WarpCategory category,
                final ItemStack warpIcon,
                final Location location,
                final long priority) {
        this.key = key.toLowerCase();
        this.category = category;
        this.warpIcon = warpIcon;
        this.location = location;
        this.priority = priority;

        Map<String, Warp> warps = category.warps();
        warps.put(this.key, this);
        category.warps(warps);

        this.save();
    }

    public void key(final String key) {
        this.key = key.toLowerCase();
        this.save();
    }

    public long priority() {
        return this.priority;
    }

    public void priority(final long priority) {
        this.priority = priority;
        this.save();
    }

    public String permission() {

        return "warps.category." + this.category.key() + "." + this.key;

    }

    public void category(final WarpCategory category) {

        //Remove this warp from the previous parent category
        WarpCategory oldCategory = this.category;
        Map<String, Warp> oldWarps = oldCategory.warps();
        oldWarps.remove(this.key);
        oldCategory.warps(oldWarps);

        //Update the category variable
        this.category = category;

        //Update the new category to add this warp
        Map<String, Warp> warps = category.warps();
        warps.put(this.key.toLowerCase(), this);
        category.warps(warps);
        this.save();
    }

    public void warpIcon(final ItemStack warpIcon) {
        //Update the Icon, but ensure it's Tagged with the Warp Information
        ItemStack tagged = warpIcon.clone();
        ItemMeta meta = tagged.getItemMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();

        pdc.set(BasicWarps.warpCategoryKey, PersistentDataType.STRING, this.category.key());
        pdc.set(BasicWarps.warpNameKey, PersistentDataType.STRING, this.key());

        tagged.setItemMeta(meta);


        this.warpIcon = tagged;
        this.save();
    }

    public void location(final Location location) {
        this.location = location;
        this.save();
    }

    public String key() {
        return this.key.toLowerCase();
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
