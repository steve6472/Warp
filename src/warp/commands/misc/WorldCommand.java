package warp.commands.misc;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.server.v1_14_R1.CommandListenerWrapper;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.entity.Player;
import warp.Warp;
import warp.commands.CommandException;
import warp.commands.CustomCommand;
import warp.commands.exception.NotAWorldTypeException;

import java.util.Locale;
import java.util.concurrent.CompletableFuture;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 03.08.2019
 * Project: SJP
 *
 ***********************/
public class WorldCommand extends CustomCommand
{
	public WorldCommand(Warp warp)
	{
		super(warp);
	}

	/*

		WorldCreator worldCreator = new WorldCreator("survival");
		worldCreator.seed(4);
		worldCreator.type(WorldType.NORMAL);

		Bukkit.createWorld(worldCreator);*/

	@Override
	public void register(CommandDispatcher<CommandListenerWrapper> dispatcher)
	{
		dispatcher.register(
			literal("world")
				.then(
					literal("create")
						.then(
							argument("name", string())
								.then(
									argument("type", string())
										.suggests((a, b) -> suggest(WorldType.values(), b))
											.then(
												argument("seed", longArg())
													.executes(c -> {

														WorldType worldType = WorldType.getByName(getString(c, "type"));

														if (worldType == null)
														{
															throw new CommandException(new NotAWorldTypeException());
														}

														WorldCreator worldCreator = new WorldCreator(getString(c, "name"));
														worldCreator.seed(getLong(c, "seed"));
														worldCreator.type(worldType);

														Bukkit.createWorld(worldCreator);

														sendInfoMessage(c, "Created new world '" + getString(c, "name") + "' !");

														return 1;
													})
											)
								)
						)
				)
				.then(
					literal("warp")
						.then(
							argument("name", string())
								.executes(c -> {

									Player player = getPlayer(c);
									player.teleport(new Location(Bukkit.getWorld(getString(c, "name")), 0, 256, 0));

									return 1;
								})
						)
				)
		);
	}

	private CompletableFuture<Suggestions> suggest(WorldType[] source, SuggestionsBuilder builder) {
		String s = builder.getRemaining().toLowerCase(Locale.ROOT);

		for(WorldType s1 : source) {
			if (s1.getName().toLowerCase(Locale.ROOT).startsWith(s)) {
				builder.suggest(s1.getName());
			}
		}

		return builder.buildFuture();
	}

	@Override
	public String getName()
	{
		return "world";
	}

	@Override
	public int getPermissionLevel()
	{
		return 3;
	}
}
