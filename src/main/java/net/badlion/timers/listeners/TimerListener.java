package net.badlion.timers.listeners;

import net.badlion.timers.TimerPlugin;
import net.badlion.timers.impl.NmsManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class TimerListener implements Listener {

	private final TimerPlugin plugin;

	public TimerListener(TimerPlugin plugin) {
		this.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onJoin(PlayerJoinEvent event) {
		NmsManager.sendPluginMessage(event.getPlayer(), TimerPlugin.CHANNEL_NAME, "REGISTER|{}".getBytes(TimerPlugin.UTF_8_CHARSET));
		NmsManager.sendPluginMessage(event.getPlayer(), TimerPlugin.CHANNEL_NAME, "CHANGE_WORLD|{}".getBytes(TimerPlugin.UTF_8_CHARSET));
	}

	@EventHandler
	public void onDisconnect(PlayerQuitEvent event) {
		this.plugin.getTimerApi().clearTimers(event.getPlayer());
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onTeleport(PlayerTeleportEvent event) {
		if (!event.getFrom().getWorld().equals(event.getTo().getWorld())) {
			NmsManager.sendPluginMessage(event.getPlayer(), TimerPlugin.CHANNEL_NAME, "CHANGE_WORLD|{}".getBytes(TimerPlugin.UTF_8_CHARSET));
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onRespawn(PlayerRespawnEvent event) {
		NmsManager.sendPluginMessage(event.getPlayer(), TimerPlugin.CHANNEL_NAME, "CHANGE_WORLD|{}".getBytes(TimerPlugin.UTF_8_CHARSET));
	}
}
