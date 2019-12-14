package warp.commands.warp;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.v1_14_R1.CommandListenerWrapper;
import warp.Warp;
import warp.WarpEntry;
import warp.commands.CommandException;
import warp.commands.CustomCommand;
import warp.commands.exception.NotCreatorException;

import java.util.ArrayList;
import java.util.List;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 31.07.2019
 * Project: SJP
 *
 ***********************/
public abstract class AbstractWarpCommand extends CustomCommand
{
	@Override
	public int getPermissionLevel()
	{
		return 0;
	}

	public AbstractWarpCommand(Warp warp)
	{
		super(warp);
	}

	public List<WarpEntry> getWarps()
	{
		return getWarp().warps;
	}

	public List<String> getWarpNames()
	{
		if (getWarps().size() == 0)
		{
			return new ArrayList<>();
		} else
		{
			List<String> names = new ArrayList<>();
			for (WarpEntry e : getWarps())
			{
				names.add(e.name);
			}
			return names;
		}
	}

	public List<String> getWarpNamesLimitedToOwner(CommandContext<CommandListenerWrapper> c) throws CommandException
	{
		if (getWarps().size() == 0)
		{
			return new ArrayList<>();
		} else
		{
			List<String> warpnames = new ArrayList<>();
			String playerUUID = getPlayer(c).getUniqueId().toString();

			for (WarpEntry e : getWarps())
			{
				if (playerUUID.equals(e.creator))
				{
					warpnames.add(e.name);
				}
			}

			return warpnames;
		}
	}

	protected boolean checkCreator(CommandContext<CommandListenerWrapper> c, WarpEntry e) throws CommandException
	{
		String playerUUID = getPlayer(c).getUniqueId().toString();
		return playerUUID.equals(e.creator);
	}

	protected void tryCreator(CommandContext<CommandListenerWrapper> c, WarpEntry e) throws CommandException
	{
		if (!checkCreator(c, e)) throw new CommandException(new NotCreatorException());
	}

	protected WarpEntry getOwnersWarp(CommandContext<CommandListenerWrapper> c) throws CommandException
	{
		WarpEntry e = getWarpByName(StringArgumentType.getString(c, "warp"));
		tryCreator(c, e);
		return e;
	}

	public WarpEntry getWarpByName(String name)
	{
		for (WarpEntry e : getWarps())
		{
			if (e.name.equals(name))
				return e;
		}

		return null;
	}
}
