package com.generalsarcasam.basicwarps.listeners;

import com.generalsarcasam.basicwarps.commands.WarpsCommand;
import com.generalsarcasam.basicwarps.utils.Messages;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public final class PlayerMoveEventListener implements Listener {

    public static Map<UUID, Location> playerLocationMap;

    public PlayerMoveEventListener() {
        if (playerLocationMap == null) {
            playerLocationMap = new HashMap<>();
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerMoveEvent(final PlayerMoveEvent event) {

        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        if (!playerLocationMap.containsKey(uuid)) {
            return;
        }

        Location startingLocation = playerLocationMap.get(uuid);
        double startingX = startingLocation.getX();
        double startingY = startingLocation.getY();
        double startingZ = startingLocation.getZ();

        Location endLocation = event.getTo();
        double x = endLocation.getX();
        double y = endLocation.getY();
        double z = endLocation.getZ();

        //Ensure player is within 1 block of where they issued the command from
        // this ensures they don't move, but don't get yelled at for wiggling.
        if (startingX <= x + 1 && startingX >= x - 1) {
            if (startingY <= y + 1 && startingY >= y - 1) {
                if (startingZ <= z + 1 && startingZ >= z - 1) {
                    //We don't care, they haven't moved too far.
                    return;
                }
            }
        }

        List<UUID> warpingPlayerList = WarpsCommand.getWarpingPlayers();
        warpingPlayerList.remove(uuid);
        WarpsCommand.setWarpingPlayers(warpingPlayerList);

        player.sendMessage(Messages.teleportCancelled("You moved while waiting!"));

        playerLocationMap.remove(uuid);

    }

}
