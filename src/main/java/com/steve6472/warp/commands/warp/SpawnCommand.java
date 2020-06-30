package com.steve6472.warp.commands.warp;

import com.mojang.brigadier.CommandDispatcher;
import com.steve6472.warp.Warp;
import com.steve6472.warp.WarpEntry;
import net.minecraft.server.v1_15_R1.CommandListenerWrapper;
import org.bukkit.ChatColor;
import org.bukkit.Location;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 29.02.2020
 * Project: Warp
 *
 ***********************/
public class SpawnCommand extends AbstractWarpCommand
{
	public SpawnCommand(Warp warp)
	{
		super(warp);
	}

	@Override
	public void register(CommandDispatcher<CommandListenerWrapper> dispatcher)
	{
		dispatcher.register(literal(getName()).requires(this::isPlayer).executes(c ->
		{
			for (String warpName : getWarpNames())
			{
				if (warpName.equals("spawn"))
				{
					WarpEntry w = getWarpByName("spawn");

					getWarp().teleport(getPlayer(c), new Location(w.world, w.x, w.y, w.z, w.yaw, w.pitch));
					sendInfoMessage(c, "Welcome to spawn!");

					return 1;
				}
			}

			sendInfoMessage(c, ChatColor.RED + "Can not find warp with name 'spawn'");

			return 0;
		}));
	}

	@Override
	public String getName()
	{
		return "spawn";
	}
}
