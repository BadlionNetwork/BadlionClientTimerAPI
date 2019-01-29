package net.badlion.timers.api;

import net.badlion.timers.TimerPlugin;
import net.badlion.timers.impl.NmsManager;
import net.badlion.timers.impl.TimerImpl;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class TimerApiImpl extends TimerApi {

	private final TimerPlugin plugin;
	private final AtomicInteger idGenerator;
	private final Set<TimerImpl> allTimers;

	public TimerApiImpl(TimerPlugin plugin) {
		this.plugin = plugin;
		this.idGenerator = new AtomicInteger(1);
		this.allTimers = Collections.newSetFromMap(new ConcurrentHashMap<TimerImpl, Boolean>());

		TimerApi.instance = this;
	}

	@Override
	public Timer createTimer(ItemStack item, boolean repeating, long time) {
		return this.createTimer(null, item, repeating, time);
	}

	@Override
	public Timer createTimer(String name, ItemStack item, boolean repeating, long time) {
		TimerImpl timer = new TimerImpl(this.plugin, this.idGenerator.getAndIncrement(), name, item, repeating, time);

		this.allTimers.add(timer);

		return timer;
	}

	@Override
	public void removeTimer(Timer timer) {
		// Failsafe
		if (timer instanceof TimerImpl) {
			TimerImpl timerImpl = (TimerImpl) timer;

			this.allTimers.remove(timerImpl);
		}

		timer.clearReceivers();
	}

	@Override
	public void clearTimers(Player player) {
		for (TimerImpl timer : this.allTimers) {
			timer.getReceivers().remove(player);
		}

		NmsManager.sendPluginMessage(player, TimerPlugin.CHANNEL_NAME, "REMOVE_ALL_TIMERS|{}".getBytes(TimerPlugin.UTF_8_CHARSET));
	}

	public void tickTimers() {
		for (TimerImpl timer : this.allTimers) {
			timer.tick();
		}
	}

	public void syncTimers() {
		for (TimerImpl timer : this.allTimers) {
			timer.syncTimer();
		}
	}
}
