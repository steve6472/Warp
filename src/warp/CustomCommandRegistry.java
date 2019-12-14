package warp;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.server.v1_14_R1.CommandListenerWrapper;
import net.minecraft.server.v1_14_R1.CustomFunction;
import net.minecraft.server.v1_14_R1.MinecraftKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import warp.commands.CommandException;
import warp.commands.exception.NoCustomCommandException;

import java.util.HashMap;
import java.util.Optional;
import java.util.Set;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 01.08.2019
 * Project: SJP
 *
 ***********************/
public class CustomCommandRegistry
{
	/**
	 * Custom Name : Path
	 */
	public HashMap<String, String> functions;

	private Warp warp;

	public CustomCommandRegistry(Warp warp)
	{
		functions = new HashMap<>();
		this.warp = warp;
	}

	public void load(FileConfiguration config)
	{
		ConfigurationSection cs = config.getConfigurationSection("customCommands");
		if (cs != null)
		{
			Set<String> customNames = cs.getKeys(false);
			for (String s : customNames)
			{
				functions.put(s, config.getString("customCommands." + s));
			}
		}

		for (String k : functions.keySet())
		{
			register(k, functions.get(k));
		}
	}

	public void save(FileConfiguration config)
	{
		for (String k : functions.keySet())
		{
			config.set("customCommands." + k, functions.get(k));
		}
		warp.saveConfig();
	}

	public void register(String name, String path)
	{
//		Bukkit.broadcastMessage("Registrated new command " + name + " at " + path);
		functions.put(name, path);
		reload();
		save(warp.getConfig());
	}

	public void remove(String name) throws CommandException
	{
		if (!functions.containsKey(name))
			throw new CommandException(new NoCustomCommandException());

		warp.getVanillaDispatcher().getRoot().removeCommand(name);
		functions.remove(name);
		warp.getConfig().set("customCommands." + name, null);
		save(warp.getConfig());
	}

	public void reload()
	{
		for (String k : functions.keySet())
		{
//			Bukkit.broadcastMessage("Removing command " + k + " at " + functions.get(k));
			warp.getVanillaDispatcher().getRoot().removeCommand(k);
//			Bukkit.broadcastMessage("Registrated command " + k + " at " + functions.get(k));
			constructCommand(k, functions.get(k));
		}
		/* This sends only vanilla (and my custom) commands but doesn't make the new one work */
//		for (EntityPlayer p : warp.getVanillaServer().getPlayerList().players)
//		{
//			warp.getVanillaServer().vanillaCommandDispatcher.a(p);
//		}
	}

	private void constructCommand(String name, String path)
	{
		warp.getVanillaDispatcher().register(
			literal(name)
				.requires(r -> r.hasPermission(0))
					.executes(c -> {
						MinecraftKey resource = MinecraftKey.a(path);
						Optional<CustomFunction> f = warp.getVanillaServer().getFunctionData().a(resource);
						return f.map(customFunction -> executeFunctions(c.getSource(), customFunction)).orElse(0);
					})
		);
	}

	private static int executeFunctions(CommandListenerWrapper source, CustomFunction function)
	{
		return source.getServer().getFunctionData().a(function, source.a().a(4));
	}

	private LiteralArgumentBuilder<CommandListenerWrapper> literal(String s)
	{
		return LiteralArgumentBuilder.literal(s);
	}
}
