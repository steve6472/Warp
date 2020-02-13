package com.steve6472.warp.commands.misc;

import com.mojang.brigadier.CommandDispatcher;
import com.steve6472.warp.Warp;
import com.steve6472.warp.commands.CustomCommand;
import net.minecraft.server.v1_15_R1.CommandListenerWrapper;
import org.bukkit.ChatColor;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 08.08.2019
 * Project: SJP
 *
 ***********************/
public class MSPTCommand extends CustomCommand
{
	public MSPTCommand(Warp warp)
	{
		super(warp);
	}

	@Override
	public void register(CommandDispatcher<CommandListenerWrapper> dispatcher)
	{
		dispatcher.register(literal(getName()).executes(c ->
		{

			if (c.getSource().getEntity() != null)
				sendInfoMessage(c, "MS Per Tick: ", ChatColor.GOLD + "" + getWarp().getVanillaServer().aU());

			return (int) getWarp().getVanillaServer().aU();
		}));
	}

	@Override
	public String getName()
	{
		return "getmspt";
	}

	@Override
	public int getPermissionLevel()
	{
		return 0;
	}
}
