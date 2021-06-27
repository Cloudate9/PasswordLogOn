package me.awesomemoder316.passwordlogon.listeners;

import me.awesomemoder316.passwordlogon.Utils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class RestrictActions implements Listener {
    @EventHandler
    public void stopMove(PlayerMoveEvent e) {
        if (Utils.noPasswordEntered.containsKey(e.getPlayer().getUniqueId())) {
            double distAway = e.getPlayer().getLocation().distance(Utils.teleportTo(e.getPlayer().getWorld()));
            if (distAway > 10) {
                e.getPlayer().teleport(Utils.teleportTo(e.getPlayer().getWorld()));
                e.getPlayer().sendMessage(ChatColor.RED + "To move further away, enter your password in chat!");
            }
        }
    }

    @EventHandler
    public void stopBreak(BlockBreakEvent e) {
        if (Utils.noPasswordEntered.containsKey(e.getPlayer().getUniqueId())) {
            e.setCancelled(true);
            e.getPlayer().sendMessage(ChatColor.RED + "To break blocks, enter your password in chat!");
        }
    }

    @EventHandler
    public void stopPlace(BlockPlaceEvent e) {
        if (Utils.noPasswordEntered.containsKey(e.getPlayer().getUniqueId())) {
            e.setCancelled(true);
            e.getPlayer().sendMessage(ChatColor.RED + "To place blocks, enter your password in chat!");
        }
    }

    @EventHandler
    public void stopPVP(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player) {
            if (Utils.noPasswordEntered.containsKey(e.getEntity().getUniqueId())) {
                e.setCancelled(true);
                e.getDamager().sendMessage(ChatColor.RED + "You can't fight other players until you enter your Password!");
            }
        }
    }

    @EventHandler
    public void stopDrop(PlayerDropItemEvent e) {
        if (Utils.noPasswordEntered.containsKey(e.getPlayer().getUniqueId())) {
            e.setCancelled(true);
            e.getPlayer().sendMessage(ChatColor.RED + "Enter your password to drop items!");
        }
    }

    @EventHandler
    public void stopPickup(EntityPickupItemEvent e) {
        if (e.getEntity() instanceof Player) {
            if (Utils.noPasswordEntered.containsKey(e.getEntity().getUniqueId())) {
                e.setCancelled(true);
                e.getEntity().sendMessage(ChatColor.RED + "Enter your password to pickup items!");
            }
        }
    }

    @EventHandler
    public void stopDrag(InventoryClickEvent e) {
        if (Utils.noPasswordEntered.containsKey(e.getWhoClicked().getUniqueId())) {
            e.setCancelled(true);
            e.getWhoClicked().sendMessage(ChatColor.RED + "You need to enter your password to do that!");
        }
    }
}
