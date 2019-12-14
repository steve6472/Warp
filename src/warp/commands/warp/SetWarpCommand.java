package warp.commands.warp;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.v1_14_R1.CommandListenerWrapper;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import warp.Warp;
import warp.WarpEntry;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 31.07.2019
 * Project: SJP
 *
 ***********************/
public class SetWarpCommand extends AbstractWarpCommand
{
	public SetWarpCommand(Warp warp)
	{
		super(warp);
	}

	@Override
	public void register(CommandDispatcher<CommandListenerWrapper> dispatcher)
	{
		dispatcher.register(
			literal("setwarp")
				.requires(this::isPlayer)
					.then(
						argument("warp", string())
							.executes(c -> {

								setwarp(c, getString(c, "warp"));

								return 1;
							})
					)
		);
	}

	private void setwarp(CommandContext<CommandListenerWrapper> context, String warpname) throws CommandSyntaxException
	{
		Player player = getPlayer(context);

		String playerId = player.getUniqueId().toString();

		for (WarpEntry e : getWarps())
		{
			if (e.name.equals(warpname))
			{
				if (e.creator.equals(playerId))
				{
					getWarp().createWarp(player, warpname);
				} else
				{
					sendInfoMessage(context, ChatColor.RED, "You can not edit other players warp");
					getWarp().sayCreator(context, e.creator);
				}
				return;
			}
		}
		getWarp().createWarp(player, warpname);
	}

	@Override
	public String getName()
	{
		return "setwarp";
	}
}
