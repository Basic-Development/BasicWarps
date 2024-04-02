package com.generalsarcasam.basicwarps.objects;

import com.generalsarcasam.basicwarps.BasicWarps;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;

import java.util.Map;

@DefaultQualifier(NonNull.class)
public final class WarpCategory {

    String key;
    Map<String, Warp> warps;
    ItemStack icon;

    public WarpCategory(final String key,
                        final Map<String, Warp> warps,
                        final ItemStack icon) {
        this.key = key;
        this.warps = warps;
        this.icon = icon;

        BasicWarps.categories.put(this.key, this);
    }

    public void key(final String key) {
        this.key = key;
        this.save();
    }

    public void warps(final Map<String, Warp> warpList) {
        this.warps = warpList;
        this.save();
    }

    public void icon(final ItemStack icon) {
        this.icon = icon;
        this.save();
    }

    public String key() {
        return this.key;
    }

    public Map<String, Warp> warps() {
        return this.warps;
    }

    public ItemStack icon() {
        return this.icon;
    }

    //todo: save categories to a file
    public void save() {
    }
}
