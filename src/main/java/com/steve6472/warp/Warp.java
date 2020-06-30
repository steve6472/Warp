package com.steve6472.warp;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.steve6472.warp.commands.CustomCommand;
import com.steve6472.warp.commands.misc.GetUUIDCommand;
import com.steve6472.warp.commands.misc.MSPTCommand;
import com.steve6472.warp.commands.misc.PrintUUIDCacheCommand;
import com.steve6472.warp.commands.warp.*;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_15_R1.CommandListenerWrapper;
import net.minecraft.server.v1_15_R1.MinecraftServer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.craftbukkit.v1_15_R1.CraftServer;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.bukkit.ChatColor.*;

public class Warp extends JavaPlugin implements Listener
{
	public List<WarpEntry> warps;
	public UUIDCache uuidCache;
	public List<CustomCommand> commands;

	public static String getWarpSign()
	{
		return GOLD + "[" + DARK_GREEN + "WARP" + GOLD + "] " + RESET + AQUA;
	}

	public void onEnable()
	{
		saveDefaultConfig();
		warps = new ArrayList<>();

		uuidCache = new UUIDCache();
		uuidCache.load(getConfig());

		commands = new ArrayList<>();

		loadWarps();

		/* Warp Commands */
		new WarpCommand(this);
		new SetWarpCommand(this);
		new DelWarpCommand(this);
		new EditWarpCommand(this);
		new SilentWarpCommand(this);
		new HomeCommand(this);
		new SpawnCommand(this);

		/* Misc Commands */
		new GetUUIDCommand(this);
		new PrintUUIDCacheCommand(this);
		new MSPTCommand(this);

		WarpEventListener warpEventListener = new WarpEventListener(this);

		Bukkit.getPluginManager().registerEvents(warpEventListener, this);
	}

	public CommandDispatcher<CommandListenerWrapper> getVanillaDispatcher()
	{
		return getVanillaServer().vanillaCommandDispatcher.a();
	}

	public MinecraftServer getVanillaServer()
	{
		return ((CraftServer) Bukkit.getServer()).getServer();
	}

	private void loadWarps()
	{
		ConfigurationSection warpConfig = getConfig().getConfigurationSection("warps");
		if (warpConfig != null)
		{
			warpConfig.getKeys(false);

			List<String> warpNames = new ArrayList<>(warpConfig.getKeys(false));

			for (String s : warpNames)
			{
				warps.add(new WarpEntry(getConfig(), s));
			}
		}
	}

	private void sendMessage(CommandContext<CommandListenerWrapper> context, Object... text)
	{
		StringBuilder sb = new StringBuilder();
		for (Object o : text)
		{
			sb.append(o);
		}
		context.getSource().getBukkitSender().sendMessage(sb.toString());
	}

	public void printAllWarps(CommandContext<CommandListenerWrapper> context)
	{
		if (warps.isEmpty())
		{
			sendMessage(context, getWarpSign(), DARK_RED, "There are no warps created");
			return;
		}

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 8; i++)
		{
			sb.append(segment());
		}

		StringBuilder sb2 = new StringBuilder();
		for (int i = 0; i < 8; i++)
		{
			sb2.append(segment2());
		}

		List<String> creators = new ArrayList<>();

		sendMessage(context, sb.toString(), BLUE, " Warps ", sb2.toString());
		sendMessage(context, AQUA, "Global:");
		for (WarpEntry e : warps)
		{
			if (!creators.contains(e.creator))
			{
				creators.add(e.creator);
			}
			if (e.isGlobal)
			{
				TextComponent nl = new TextComponent("\n");

				TextComponent creator = new TextComponent();
				creator.setText("Creator: ");
				creator.setColor(net.md_5.bungee.api.ChatColor.GOLD);

				TextComponent player = new TextComponent();
				if (e.creator == null)
				{
					player.setText("not found");
					player.setColor(net.md_5.bungee.api.ChatColor.RED);
				} else
				{
					player.setText(uuidCache.getPlayerName(e.creator));
					player.setColor(net.md_5.bungee.api.ChatColor.WHITE);
				}

				TextComponent clickToWarp = new TextComponent();
				clickToWarp.setText("Click to warp");
				clickToWarp.setUnderlined(true);
				clickToWarp.setColor(net.md_5.bungee.api.ChatColor.AQUA);

				TextComponent text = new TextComponent();
				text.setText("  " + e.name);
				text.setColor(net.md_5.bungee.api.ChatColor.WHITE);


				text.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent[]{creator, player, nl, clickToWarp}));
				text.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/silentwarp " + e.name));
				context.getSource().getBukkitSender().spigot().sendMessage(text);
			}
		}

		boolean hasNull = false;

		for (String creator : creators)
		{
			if (creator == null)
			{
				hasNull = true;
				continue;
			}
			sendMessage(context, "");
			sendMessage(context, AQUA, uuidCache.getPlayerName(creator), ":");
			for (WarpEntry e : warps)
			{
				if (creator.equals(e.creator) && !e.isGlobal)
				{
					TextComponent clickToWarp = new TextComponent();
					clickToWarp.setText("Click to warp");
					clickToWarp.setUnderlined(true);
					clickToWarp.setColor(net.md_5.bungee.api.ChatColor.AQUA);

					TextComponent text = new TextComponent();
					text.setText("  " + e.name);
					text.setColor(net.md_5.bungee.api.ChatColor.WHITE);

					text.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent[]{clickToWarp}));
					text.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/silentwarp " + e.name));
					context.getSource().getBukkitSender().spigot().sendMessage(text);
				}
			}
		}

		if (hasNull)
		{
			sendMessage(context, "");
			sendMessage(context, AQUA, "Creator not found", ":");
			for (WarpEntry e : warps)
			{
				if (e.creator == null && !e.isGlobal)
				{
					TextComponent clickToWarp = new TextComponent();
					clickToWarp.setText("Click to warp");
					clickToWarp.setUnderlined(true);
					clickToWarp.setColor(net.md_5.bungee.api.ChatColor.AQUA);

					TextComponent text = new TextComponent();
					text.setText("  " + e.name);
					text.setColor(net.md_5.bungee.api.ChatColor.WHITE);

					text.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent[]{clickToWarp}));
					text.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/silentwarp " + e.name));
					context.getSource().getBukkitSender().spigot().sendMessage(text);
				}
			}
		}
	}

	private String segment()
	{
		return "" + BLUE + "-" + AQUA + "-";
	}

	private String segment2()
	{
		return "" + AQUA + "-" + BLUE + "-";
	}

	public void createWarp(Player p, String warpname)
	{
		boolean edited = false;
		WarpEntry edit = null;

		for (WarpEntry e : warps)
		{
			if (e.name.equals(warpname))
			{
				edited = true;
				edit = e;
				break;
			}
		}

		if (!edited)
		{
			WarpEntry w = new WarpEntry(p, warpname);
			w.save(getConfig());
			saveConfig();

			warps.add(w);
			p.sendMessage(getWarpSign() + "'" + WHITE + warpname + AQUA + "' warp set!");
		} else
		{
			warps.remove(edit);

			WarpEntry w = new WarpEntry(p, warpname);
			w.save(getConfig());
			saveConfig();

			warps.add(w);
			p.sendMessage(getWarpSign() + "'" + WHITE + warpname + AQUA + "' warp edited!");
		}
	}

	public void saveWarps()
	{
		for (WarpEntry e : warps)
		{
			e.save(getConfig());
		}
		saveConfig();
	}

	public void sayCreator(CommandContext<CommandListenerWrapper> context, String uuid)
	{
		sayCreator(context.getSource().getBukkitSender(), uuid);
	}

	private void sayCreator(CommandSender sender, String uuid)
	{
		Player player = Bukkit.getPlayer(UUID.fromString(uuid));
		if (player == null)
		{
			sender.sendMessage(RED + "Creator is offline but his UUID is " + YELLOW + uuid);
		} else
		{
			sender.sendMessage(RED + "Creator: " + WHITE + player.getDisplayName());
		}
	}

	public void teleport(Player player, Location l)
	{
		List<MetadataValue> mount = player.getMetadata("mount");
		if (!mount.isEmpty())
		{
			try
			{
				org.bukkit.entity.Entity m = (org.bukkit.entity.Entity) mount.get(mount.size() - 1).value();
				if (m == null)
				{
					player.sendMessage(DARK_RED + "God Fucking Dammit It's null! Error: 3");
				} else
				{
					player.setFallDistance(0);
					player.setVelocity(new Vector(0, 0, 0));
					m.eject();
					player.teleport(l);
					m.teleport(l.add(1.0D, 0.0D, 0.0D));
				}
			} catch (Exception e)
			{
				player.teleport(l);
			}
		} else
		{
			player.teleport(l);
		}
	}
}
