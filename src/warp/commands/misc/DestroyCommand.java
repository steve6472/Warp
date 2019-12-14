package warp.commands.misc;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.v1_14_R1.*;
import warp.Warp;
import warp.commands.CustomCommand;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 31.07.2019
 * Project: SJP
 *
 ***********************/
public class DestroyCommand extends CustomCommand
{
	public DestroyCommand(Warp warp)
	{
		super(warp);
	}

	@Override
	public void register(CommandDispatcher<CommandListenerWrapper> dispatcher)
	{
		dispatcher.register(
			literal("destroy")
				.requires(c -> c.hasPermission(3))
					.then(argument("entity", multipleEntities())
						.executes(c -> {

							int i = 0;

							for (Entity e : getEntities(c, "entity"))
							{
								if (!(e instanceof EntityPlayer))
								{
									i++;
									e.getBukkitEntity().remove();
								}
							}

							sendInfoMessage(c, "Destroyed " + i + " entities");

							return i;
						})
					)
		);
	}

	@Override
	public String getName()
	{
		return "destroy";
	}

	@Override
	public int getPermissionLevel()
	{
		return 3;
	}
}
