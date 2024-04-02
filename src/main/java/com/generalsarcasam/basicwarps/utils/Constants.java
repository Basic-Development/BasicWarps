package com.generalsarcasam.basicwarps.utils;

import com.generalsarcasam.basicwarps.BasicWarps;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public final class Constants {

    private Constants() {
    }

    public static ItemStack categoryIcon(final String name) {

        ItemStack item = new ItemStack(Material.CYAN_CONCRETE);
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();

        pdc.set(BasicWarps.warpCategoryKey, PersistentDataType.STRING, name);

        meta.displayName(
                Component.text(name, NamedTextColor.DARK_AQUA)
                        .decoration(TextDecoration.BOLD, true)
                        .decoration(TextDecoration.ITALIC, false)
        );

        List<Component> lore = new ArrayList<>();
        lore.add(
                Component.text("Update this Category Icon with /warps icon <category>", NamedTextColor.GRAY)
                        .decoration(TextDecoration.BOLD, false)
                        .decoration(TextDecoration.ITALIC, false)
        );

        meta.lore(lore);

        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack warpIcon(final String name) {

        ItemStack item = new ItemStack(Material.LIGHT_BLUE_CONCRETE);
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();

        pdc.set(BasicWarps.warpNameKey, PersistentDataType.STRING, name);

        meta.displayName(
                Component.text(name, NamedTextColor.AQUA)
                        .decoration(TextDecoration.BOLD, false)
                        .decoration(TextDecoration.ITALIC, false)
        );

        List<Component> lore = new ArrayList<>();
        lore.add(
                Component.text("Update this Warp Icon with /warps icon <warp>")
        );

        meta.lore(lore);

        item.setItemMeta(meta);
        return item;
    }


}
