package me.awesomemoder316.passwordlogon.listeners;

import me.awesomemoder316.passwordlogon.MessageConfig;
import me.awesomemoder316.passwordlogon.Utils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.EntityTeleportEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class RestrictActions implements Listener {

    @EventHandler
    public void stopBreak(BlockBreakEvent e) {
        if (Utils.noPasswordEntered.containsKey(e.getPlayer().getUniqueId())) {
            e.setCancelled(true);
            new MessageConfig().restrictAction(e.getPlayer());
        }
    }

    @EventHandler
    public void stopCommand(PlayerCommandPreprocessEvent e) {
        if (Utils.noPasswordEntered.containsKey(e.getPlayer().getUniqueId())) {
            if (!e.getMessage().startsWith("/pw") && !e.getMessage().startsWith("/password")) {
                e.setCancelled(true);
                new MessageConfig().restrictAction(e.getPlayer());
                Utils.plugin.getLogger().info(e.getPlayer().getName() + " tried to send a command without entering their password, and was stopped!");
            }
        }
    }

    @EventHandler
    public void stopMove(PlayerMoveEvent e) {
        if (Utils.noPasswordEntered.containsKey(e.getPlayer().getUniqueId())) {
            double distAway = e.getPlayer().getLocation().distance(Utils.teleportTo(e.getPlayer().getWorld()));
            if (distAway > 10) {
                e.getPlayer().teleport(Utils.teleportTo(e.getPlayer().getWorld()));
                new MessageConfig().restrictAction(e.getPlayer());
            }
        }
    }

    @EventHandler
    public void stopPetTeleport(EntityTeleportEvent e) {
        if (!(e.getEntity() instanceof Tameable)) return;

        Tameable entity = (Tameable) e.getEntity();

        if (entity.getOwner() == null) return;

        if (Utils.noPasswordEntered.containsKey(entity.getOwner().getUniqueId())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void stopPlace(BlockPlaceEvent e) {
        if (Utils.noPasswordEntered.containsKey(e.getPlayer().getUniqueId())) {
            e.setCancelled(true);
            new MessageConfig().restrictAction(e.getPlayer());
        }
    }

    @EventHandler
    public void stopPVP(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player) {
            if (Utils.noPasswordEntered.containsKey(e.getEntity().getUniqueId())) {
                e.setCancelled(true);
                new MessageConfig().restrictAction(e.getDamager());
            }
        }
    }

    @EventHandler
    public void stopDrop(PlayerDropItemEvent e) {
        if (Utils.noPasswordEntered.containsKey(e.getPlayer().getUniqueId())) {
            e.setCancelled(true);
            new MessageConfig().restrictAction(e.getPlayer());
        }
    }

    @EventHandler
    public void stopPickup(EntityPickupItemEvent e) {
        if (e.getEntity() instanceof Player) {
            if (Utils.noPasswordEntered.containsKey(e.getEntity().getUniqueId())) {
                e.setCancelled(true);
                new MessageConfig().restrictAction(e.getEntity());
            }
        }
    }

    @EventHandler
    public void stopDrag(InventoryClickEvent e) {
        if (Utils.noPasswordEntered.containsKey(e.getWhoClicked().getUniqueId())) {
            e.setCancelled(true);
            new MessageConfig().restrictAction(e.getWhoClicked());
        }
    }
}
