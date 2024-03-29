package com.generalsarcasam.basicwarps.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;

@DefaultQualifier(NonNull.class)
public final class InventoryClickEventListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onInventoryClickEvent(final InventoryClickEvent event) {

    }

}
