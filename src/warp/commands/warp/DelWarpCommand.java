package warp.commands.warp;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.v1_14_R1.CommandListenerWrapper;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import warp.Warp;
import warp.WarpEntry;

import java.util.Iterator;

import static org.bukkit.ChatColor.*;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 31.07.2019
 * Project: SJP
 *
 ***********************/
public class DelWarpCommand extends AbstractWarpCommand
{
	public DelWarpCommand(Warp warp)
	{
		super(warp);
	}

	@Override
	public void register(CommandDispatcher<CommandListenerWrapper> dispatcher)
	{
		dispatcher.register(
			literal("delwarp")
				.requires(this::isPlayer)
					.then(
						argument("warp", string())
							.suggests((a, b) -> suggest(getWarpNamesLimitedToOwner(a), b))
								.executes(c ->
								{
									delwarp(c, getString(c, "warp"));
									return 1;
								})
					)
		);
	}

	private void delwarp(CommandContext<CommandListenerWrapper> context, String name) throws CommandSyntaxException
	{
		CommandSender sender = context.getSource().getBukkitSender();

		for (Iterator<WarpEntry> iter = getWarps().iterator(); iter.hasNext();)
		{
			WarpEntry e = iter.next();
			if (e.name.equals(name))
			{
				Player player = getPlayer(context);
				String playerId = player.getUniqueId().toString();

				if (e.creator.equals(playerId))
				{
					getWarp().getConfig().set("warps." + name, null);
					getWarp().saveConfig();
					sendInfoMessage(context, "Deleted warp '" + GOLD + name + AQUA + "'");

					iter.remove();
					return;
				} else
				{
					sendInfoMessage(context, RED, "You can not delete other players warp");
					getWarp().sayCreator(context, e.creator);
					return;
				}
			}
		}
		sendInfoMessage(context, RED, "No warp by that name exists");
	}

	@Override
	public String getName()
	{
		return "delwarp";
	}
}
