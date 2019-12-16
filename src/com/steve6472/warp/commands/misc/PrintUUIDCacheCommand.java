package com.steve6472.warp.commands.misc;

import com.mojang.brigadier.CommandDispatcher;
import com.steve6472.warp.Warp;
import com.steve6472.warp.commands.CustomCommand;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_14_R1.CommandListenerWrapper;

import java.util.HashMap;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 01.08.2019
 * Project: SJP
 *
 ***********************/
public class PrintUUIDCacheCommand extends CustomCommand
{
	public PrintUUIDCacheCommand(Warp warp)
	{
		super(warp);
	}

	@Override
	public void register(CommandDispatcher<CommandListenerWrapper> dispatcher)
	{
		dispatcher.register(
			literal("printuuidcache")
				.requires(c -> c.hasPermission(2))
					.executes(c -> {

						HashMap<String, String> map = getWarp().uuidCache.getUuidNameMap();
						sendInfoMessage(c, "Printing all Cached UUIDs");
						for (String k : map.keySet())
						{
							TextComponent click = new TextComponent("");
							click.setColor(ChatColor.GOLD);
							click.setText(k);
							click.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, k));

							TextComponent name = new TextComponent(map.get(k) + ": ");
							name.addExtra(click);
							c.getSource().getBukkitSender().spigot().sendMessage(name);
						}

						return 1;
					})

		);
	}

	@Override
	public String getName()
	{
		return "printuuidcache";
	}

	@Override
	public int getPermissionLevel()
	{
		return 2;
	}
}
