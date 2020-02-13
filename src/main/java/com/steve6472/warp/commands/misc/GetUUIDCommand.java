package com.steve6472.warp.commands.misc;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.steve6472.warp.Warp;
import com.steve6472.warp.commands.CustomCommand;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_15_R1.CommandListenerWrapper;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 31.07.2019
 * Project: SJP
 *
 ***********************/
public class GetUUIDCommand extends CustomCommand
{
	public GetUUIDCommand(Warp warp)
	{
		super(warp);
	}

	@Override
	public void register(CommandDispatcher<CommandListenerWrapper> dispatcher)
	{
		dispatcher.register(literal("getuuid").requires(this::isPlayer).then(argument("player", singlePlayer()).executes(c ->
		{
			getUUID(c);
			return 1;
		})));
	}

	private void getUUID(CommandContext<CommandListenerWrapper> context) throws CommandSyntaxException
	{
		String UUID = getWarp().uuidCache.getUUID(getSinglePlayer(context, "player").getName());

		sendInfoMessage(context, "UUID of " + getSinglePlayer(context, "player") + " is ");

		TextComponent tc = new TextComponent();
		tc.setColor(net.md_5.bungee.api.ChatColor.GOLD);
		tc.setText(UUID);
		tc.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, UUID));
		context.getSource().getBukkitSender().spigot().sendMessage(tc);
	}

	@Override
	public String getName()
	{
		return "getuuid";
	}

	@Override
	public int getPermissionLevel()
	{
		return 0;
	}
}
