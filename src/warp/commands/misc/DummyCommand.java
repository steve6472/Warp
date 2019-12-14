package warp.commands.misc;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.server.v1_14_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;
import warp.Warp;
import warp.commands.CommandException;
import warp.commands.CustomCommand;
import warp.commands.exception.TooManyFunctionsException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 01.08.2019
 * Project: SJP
 *
 ***********************/
public class DummyCommand extends CustomCommand
{
	public DummyCommand(Warp warp)
	{
		super(warp);
	}

	private static final SuggestionProvider<CommandListenerWrapper> FUNCTION_SUGGESTER = (a, b) -> {
		CustomFunctionData functionmanager = a.getSource().getServer().getFunctionData();
		return ICompletionProvider.a(functionmanager.c().keySet(), b);
	};

	@Override
	public void register(CommandDispatcher<CommandListenerWrapper> dispatcher)
	{
		dispatcher.register(
			literal("dummy")
				.requires(r -> r.hasPermission(3))
					.then(
						literal("create")
							.then(
								argument("custom name", string())
									.then(
										argument("function", ArgumentTag.a())
											.suggests(FUNCTION_SUGGESTER)
												.executes(c -> {

													Collection<CustomFunction> functions = ArgumentTag.a(c, "function");

													for (CustomFunction f : functions)
													{
														if (functions.size() > 1)
														{
															throw new CommandException(new TooManyFunctionsException());
														}

														String customName = getString(c, "custom name");

														getWarp().registry.register(customName, f.a().toString());

														sendInfoMessage(c, "Registrated new Custom Command '", ChatColor.WHITE, customName, ChatColor.AQUA, "' ",
																"that will run function '", ChatColor.WHITE, f.a().toString(), ChatColor.AQUA, "'");
														sendInfoMessage(c, "Use /reload to activate all created commands");
													}

													return 1;
												})
									)
							)
					)
				.then(
					literal("remove")
						.then(
							argument("custom command", string())
								.suggests((a, b) -> suggest(getCustomCommands(), b))
									.executes(c -> {

										getWarp().registry.remove(getString(c, "custom command"));

										sendInfoMessage(c, "Custom Command '",
												ChatColor.WHITE, getString(c, "custom command"), ChatColor.AQUA, "' has been removed");
										sendInfoMessage(c, "Use /reload to remove deleted commands from clients");

										return 1;
									})
						)
				)
			.then(
				literal("list")
					.executes(c -> {

						for (String s : getCustomCommands())
						{
							sendInfoMessage(c, s, " : " + getWarp().registry.functions.get(s));
						}

						return 1;
					})
			)
			.then(
				literal("test")
					.executes(c -> {

						for (Player p : Bukkit.getOnlinePlayers())
						{
							for (PermissionAttachmentInfo info : p.getEffectivePermissions())
							{
								sendInfoMessage(c, p.getName() + ": " + info.getPermission());
							}
						}

						return 1;
					})
			)
		);
	}

	private List<String> getCustomCommands()
	{
		return new ArrayList<>(getWarp().registry.functions.keySet());
	}

	@Override
	public String getName()
	{
		return "dummy";
	}

	@Override
	public int getPermissionLevel()
	{
		return 3;
	}
}
