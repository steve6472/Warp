package com.steve6472.warp;

import com.steve6472.warp.commands.CustomCommand;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.server.ServerLoadEvent;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 01.08.2019
 * Project: SJP
 *
 ***********************/
public class WarpEventListener implements Listener
{
	Warp warp;

	public WarpEventListener(Warp warp)
	{
		this.warp = warp;
	}

	@EventHandler
	public void updateCache(PlayerJoinEvent e)
	{
		warp.uuidCache.update();
		warp.uuidCache.save(warp.getConfig());
		warp.saveConfig();

		updatePermissions(e.getPlayer());
	}

	@EventHandler
	public void reload(ServerLoadEvent e)
	{
		for (Player p : Bukkit.getOnlinePlayers())
		{
			updatePermissions(p);
		}
	}

	private void updatePermissions(Player player)
	{
		player.recalculatePermissions();
		for (CustomCommand comm : warp.commands)
		{
			if (comm.getPermissionLevel() == 0)
			{
				player.addAttachment(warp, "minecraft.command." + comm.getName(), true);
			} else if (player.isOp())
			{
				player.addAttachment(warp, "minecraft.command." + comm.getName(), true);
			}
		}

		for (String s : warp.registry.functions.keySet())
		{
			player.addAttachment(warp, "minecraft.command." + s, true);
		}
	}
}
