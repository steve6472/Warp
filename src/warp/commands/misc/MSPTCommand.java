package warp.commands.misc;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.v1_14_R1.CommandListenerWrapper;
import org.bukkit.ChatColor;
import warp.Warp;
import warp.commands.CustomCommand;

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
		dispatcher.register(
			literal(getName())
				.executes(c -> {

					if (c.getSource().getEntity() != null)
						sendInfoMessage(c, "MS Per Tick: ", ChatColor.GOLD + "" + getWarp().getVanillaServer().aR());

					return (int) getWarp().getVanillaServer().aR();
				})
		);
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
