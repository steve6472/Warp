package warp.commands.misc;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.v1_14_R1.CommandListenerWrapper;
import net.minecraft.server.v1_14_R1.Entity;
import warp.Warp;
import warp.commands.CustomCommand;

import java.util.Collection;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 04.08.2019
 * Project: SJP
 *
 ***********************/
public class NoClipCommand extends CustomCommand
{
	public NoClipCommand(Warp warp)
	{
		super(warp);
	}

	@Override
	public void register(CommandDispatcher<CommandListenerWrapper> dispatcher)
	{
		dispatcher.register(
			literal(getName())
				.then(
					argument("targets", multipleEntities())
						.then(
							argument("noclip", bool())
								.executes(c -> {

									Collection<? extends Entity> targets = getEntities(c, "targets");
									for (Entity e : targets)
									{
										e.noclip = getBool(c, "noclip");
									}

									return 1;
								})
						)
				)
		);
	}

	@Override
	public String getName()
	{
		return "noclip";
	}

	@Override
	public int getPermissionLevel()
	{
		return 1;
	}
}
