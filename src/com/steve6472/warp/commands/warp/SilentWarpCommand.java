package com.steve6472.warp.commands.warp;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.steve6472.warp.Warp;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_14_R1.CommandListenerWrapper;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.steve6472.warp.WarpEntry;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 02.08.2019
 * Project: SJP
 *
 ***********************/
public class SilentWarpCommand extends AbstractWarpCommand
{
	public SilentWarpCommand(Warp warp)
	{
		super(warp);
	}

	@Override
	public void register(CommandDispatcher<CommandListenerWrapper> dispatcher)
	{
		dispatcher.register(
			literal("silentwarp")
				.then(
					argument("com/steve6472/warp", string())
						.suggests((a, b) -> suggest(getWarpNames(), b))
							.executes(c ->
							{
								if (!warp(c, getString(c, "com/steve6472/warp")))
									sendMessage(c, ChatColor.RED, "No warp by that name exists");
								return 1;
							})
				)
		);
	}

	@Override
	public String getName()
	{
		return "silentwarp";
	}

	private boolean warp(CommandContext<CommandListenerWrapper> context, String name)
	{
		CommandSender sender = context.getSource().getBukkitSender();
		if (!(sender instanceof Player))
			return false;

		for (WarpEntry w : getWarps())
		{
			if (w.name.equals(name))
			{
				getWarp().teleport((Player) sender, new Location(w.world, w.x, w.y, w.z, w.yaw, w.pitch));
				return true;
			}
		}

		return false;
	}
}
