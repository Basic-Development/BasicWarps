package com.generalsarcasam.basicwarps.utils;

import com.generalsarcasam.basicwarps.BasicWarps;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public final class Constants {

    public static final ItemStack FILLER_ITEM = fillerItem();
    public static final ItemStack CLOSE_MENU_ITEM = closeMenuItem();
    public static final ItemStack NEXT_PAGE_ITEM = nextPageItem();
    public static final ItemStack PREVIOUS_PAGE_ITEM = previousPageItem();
    public static final @Nullable ItemStack PREVIOUS_MENU_ITEM = previousMenuItem();

    private Constants() {
    }

    public static Inventory baseMenu(final int inventorySize,
                                     final InventoryHolder holder) {
        return baseMenu(inventorySize, holder, Messages.PREFIX);
    }

    public static Inventory baseMenu(final int inventorySize,
                                     final InventoryHolder holder,
                                     final Component menuPrefix) {

        Inventory gui = Bukkit.createInventory(holder, inventorySize, menuPrefix);

        //Fill the rest of the GUI with the Filler Item
        for (int i = 9; i < inventorySize; i++) {
            gui.setItem(i, fillerItem());
        }

        //Set the Top Row of the GUI to the Close Button
        for (int i = 0; i < 9; i++) {
            gui.setItem(i, closeMenuItem());
        }

        //Set the Bottom Row of the GUI to the Close Button
        for (int i = inventorySize - 1; i > inventorySize - 10; i--) {
            gui.setItem(i, closeMenuItem());
        }


        return gui;
    }

    private static ItemStack fillerItem() {

        return menuItem(Material.LIGHT_GRAY_STAINED_GLASS_PANE,
                Component.text(""),
                new ArrayList<>());
    }

    private static ItemStack nextPageItem() {
        ItemStack item = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
        ItemMeta meta = item.getItemMeta();

        //Set the Display Name to "Next Page"
        meta.displayName(
                Component.text("Next Page", NamedTextColor.GREEN)
                        .decoration(TextDecoration.BOLD, true)
                        .decoration(TextDecoration.ITALIC, false)
        );

        item.setItemMeta(meta);
        return item;
    }

    private static ItemStack previousPageItem() {
        ItemStack item = new ItemStack(Material.YELLOW_STAINED_GLASS_PANE);
        ItemMeta meta = item.getItemMeta();

        //Set the Display Name to "Previous Page"
        meta.displayName(
                Component.text("Previous Page", NamedTextColor.YELLOW)
                        .decoration(TextDecoration.BOLD, true)
                        .decoration(TextDecoration.ITALIC, false)
        );

        item.setItemMeta(meta);
        return item;
    }

    private static ItemStack previousMenuItem() {
        ItemStack item = new ItemStack(Material.ORANGE_STAINED_GLASS_PANE);
        ItemMeta meta = item.getItemMeta();

        //Set the Display Name to "Previous Page"
        meta.displayName(
                Component.text("Previous Menu", NamedTextColor.GOLD)
                        .decoration(TextDecoration.BOLD, true)
                        .decoration(TextDecoration.ITALIC, false)
        );

        item.setItemMeta(meta);
        return item;
    }

    private static ItemStack closeMenuItem() {

        return menuItem(Material.BLACK_STAINED_GLASS_PANE,
                Component.text("Close Menu", NamedTextColor.DARK_RED)
                        .decorationIfAbsent(TextDecoration.BOLD, TextDecoration.State.TRUE)
                        .decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE),
                new ArrayList<>()
        );
    }

    private static ItemStack menuItem(final Material material,
                                      final Component displayName,
                                      final List<Component> lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();

        meta.displayName(displayName);
        meta.lore(lore);

        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP);

        item.setItemMeta(meta);
        return item;
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

    public static ItemStack warpIcon(final String warpName,
                                     final String categoryName) {

        ItemStack item = new ItemStack(Material.LIGHT_BLUE_CONCRETE);
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();

        pdc.set(BasicWarps.warpNameKey, PersistentDataType.STRING, warpName);
        pdc.set(BasicWarps.warpCategoryKey, PersistentDataType.STRING, categoryName);

        meta.displayName(
                Component.text(warpName, NamedTextColor.AQUA)
                        .decoration(TextDecoration.BOLD, false)
                        .decoration(TextDecoration.ITALIC, false)
        );

        List<Component> lore = new ArrayList<>();
        lore.add(
                Component.text("Update this Warp Icon with /warps icon <warp>",
                        NamedTextColor.GRAY)
                        .decoration(TextDecoration.ITALIC, false)
        );

        meta.lore(lore);

        item.setItemMeta(meta);
        return item;
    }


}
