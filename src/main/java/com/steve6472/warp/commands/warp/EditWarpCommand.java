package com.steve6472.warp.commands.warp;

import com.mojang.brigadier.CommandDispatcher;
import com.steve6472.warp.Warp;
import com.steve6472.warp.WarpEntry;
import net.minecraft.server.v1_15_R1.CommandListenerWrapper;
import net.minecraft.server.v1_15_R1.EntityPlayer;

import static org.bukkit.ChatColor.*;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 31.07.2019
 * Project: SJP
 *
 ***********************/
public class EditWarpCommand extends AbstractWarpCommand
{
	public EditWarpCommand(Warp warp)
	{
		super(warp);
	}

	@Override
	public void register(CommandDispatcher<CommandListenerWrapper> dispatcher)
	{
		dispatcher.register(literal("editwarp").requires(this::isPlayer).then(argument("warp", string()).suggests((a, b) -> suggest(getWarpNamesLimitedToOwner(a), b)).then(literal("owner").then(argument("player", singlePlayer()).executes(c ->
		{
			/* Change owner */

			WarpEntry e = getOwnersWarp(c);

			EntityPlayer player = getSinglePlayer(c, "player");

			if (getPlayer(c).getUniqueId().equals(player.getUniqueID()))
			{
				sendInfoMessage(c, "Nothing changed silly!");

				return 2;
			}

			sendInfoMessage(c, "Changed owner of %ca_warp% from %cw_player% to %w_player%");
			e.creator = player.getUniqueID().toString();

			return 1;
		})).executes(c ->
		{
			/* Print owner which is essentially useless as the player can see only his warps */

			getOwnersWarp(c);
			sendInfoMessage(c, "The owner of %ca_warp% is you, %cw_player%!");


			return 1;
		})).then(literal("global").then(argument("isGlobal", bool()).executes(c ->
		{
			/* Set global */

			WarpEntry e = getOwnersWarp(c);

			e.isGlobal = getBool(c, "isGlobal");
			getWarp().saveWarps();
			if (e.isGlobal)
			{
				sendInfoMessage(c, "%ca_warp% is now global");
			} else
			{
				sendInfoMessage(c, "%ca_warp% is no longer global");
			}

			return 1;
		})).executes(c ->
		{
			/* Check if warp is global or not */

			WarpEntry e = getOwnersWarp(c);

			sendInfoMessage(c, "%ca_warp%", isNot(e.isGlobal), "global");

			return 1;
		})).then(literal("name").then(argument("new name", string()).executes(c ->
		{

			WarpEntry e = getOwnersWarp(c);
			if (getString(c, "new name").equals(e.name))
			{
				sendInfoMessage(c, "Nothing changed silly!");
			} else
			{
				getWarp().getConfig().set("warps." + e.name, null);
				e.name = getString(c, "new name");
				e.save(getWarp().getConfig());
				getWarp().saveConfig();
				sendInfoMessage(c, "Name changed from %ca_warp% to '" + GOLD + e.name + AQUA + "'");
			}

			return 1;
		})))));
	}

	private String isNot(boolean is)
	{
		if (is)
		{
			return " " + DARK_GREEN + "is" + RESET + " " + AQUA;
		} else
		{
			return " " + DARK_RED + "is not" + RESET + " " + AQUA;
		}
	}

	@Override
	public String getName()
	{
		return "editwarp";
	}
}
