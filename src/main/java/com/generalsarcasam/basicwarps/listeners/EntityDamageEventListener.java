package com.generalsarcasam.basicwarps.listeners;

import com.generalsarcasam.basicwarps.commands.WarpsCommand;
import com.generalsarcasam.basicwarps.utils.Messages;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.List;
import java.util.UUID;

public final class EntityDamageEventListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntityDamageEvent(final EntityDamageEvent event) {

        if (event.isCancelled() || event.getDamage() == 0) {
            return;
        }

        Entity entity = event.getEntity();
        if (!(entity instanceof Player player)) {
            return;
        }

        UUID uuid = player.getUniqueId();

        List<UUID> warpingPlayers = WarpsCommand.getWarpingPlayers();
        if (warpingPlayers.contains(uuid)) {
            //The player was waiting to warp while they took damage.
            player.sendMessage(Messages.teleportCancelled("You took damage while waiting!"));

            warpingPlayers.remove(uuid);
            WarpsCommand.setWarpingPlayers(warpingPlayers);

        }

    }

}
