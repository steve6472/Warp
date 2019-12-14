package warp.commands.warp;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.v1_14_R1.CommandListenerWrapper;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import warp.Warp;
import warp.WarpEntry;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 31.07.2019
 * Project: SJP
 *
 ***********************/
public class WarpCommand extends AbstractWarpCommand
{
	public WarpCommand(Warp warp)
	{
		super(warp);
	}

	@Override
	public void register(CommandDispatcher<CommandListenerWrapper> dispatcher)
	{
		dispatcher.register(
			literal("warp")
//				.requires(this::isPlayer)
					.then(
						argument("warp", string())
							.suggests((a, b) -> suggest(getWarpNames(), b))
								.executes(c ->
								{
									if (!warp(c, getString(c, "warp")))
										sendMessage(c, ChatColor.RED, "No warp by that name exists");
									return 1;
								})
					)
					.executes(c ->
					{
						getWarp().printAllWarps(c);
						return 1;
					}));
	}

	private boolean warp(CommandContext<CommandListenerWrapper> context, String name) throws CommandSyntaxException
	{
		CommandSender sender = context.getSource().getBukkitSender();
		if (!(sender instanceof Player))
			return false;

		for (WarpEntry w : getWarps())
		{
			if (w.name.equals(name))
			{
				getWarp().teleport((Player) sender, new Location(w.world, w.x, w.y, w.z, w.yaw, w.pitch));

				sendInfoMessage(context, "Warping to %ca_warp%");
				return true;
			}
		}

		return false;
	}

	@Override
	public String getName()
	{
		return "warp";
	}
}
