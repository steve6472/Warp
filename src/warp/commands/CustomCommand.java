package warp.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.*;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.server.v1_14_R1.*;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import warp.Warp;
import warp.commands.exception.NotPlayerException;

import java.util.Collection;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 31.07.2019
 * Project: SJP
 *
 ***********************/
public abstract class CustomCommand
{
	private Warp warp;

	public CustomCommand(Warp warp)
	{
		if (warp == null)
			return;

		if (getName() == null)
		{
			throw new NullPointerException("Command name not specified and will not be registered");
		}

		warp.commands.add(this);
		this.warp = warp;
		warp.getVanillaDispatcher().getRoot().removeCommand(getName());
		register(warp.getVanillaDispatcher());
	}

	public abstract void register(CommandDispatcher<CommandListenerWrapper> dispatcher);

	public abstract String getName();

	public abstract int getPermissionLevel();

	protected Warp getWarp()
	{
		return warp;
	}

	protected LiteralArgumentBuilder<CommandListenerWrapper> literal(String s)
	{
		return LiteralArgumentBuilder.literal(s);
	}

	protected <T> RequiredArgumentBuilder<CommandListenerWrapper, T> argument(String name, ArgumentType<T> type)
	{
		return RequiredArgumentBuilder.argument(name, type);
	}

	protected CompletableFuture<Suggestions> suggest(Iterable<String> source, SuggestionsBuilder builder)
	{
		String s = builder.getRemaining().toLowerCase(Locale.ROOT);

		for (String s1 : source)
		{
			if (s1.toLowerCase(Locale.ROOT).startsWith(s))
			{
				builder.suggest(s1);
			}
		}

		return builder.buildFuture();
	}

	/* Integer Argument */

	protected IntegerArgumentType integer()
	{
		return IntegerArgumentType.integer();
	}

	protected IntegerArgumentType integer(int min)
	{
		return IntegerArgumentType.integer(min);
	}

	protected IntegerArgumentType integer(int min, int max)
	{
		return IntegerArgumentType.integer(min, max);
	}

	protected int getInteger(CommandContext<CommandListenerWrapper> context, String name)
	{
		return IntegerArgumentType.getInteger(context, name);
	}

	/* Long Argument */

	protected LongArgumentType longArg()
	{
		return LongArgumentType.longArg();
	}

	protected LongArgumentType longArg(long min)
	{
		return LongArgumentType.longArg(min);
	}

	protected LongArgumentType longArg(long min, long max)
	{
		return LongArgumentType.longArg(min, max);
	}

	protected long getLong(CommandContext<CommandListenerWrapper> context, String name)
	{
		return LongArgumentType.getLong(context, name);
	}

	/* String Argument */

	protected StringArgumentType string()
	{
		return StringArgumentType.string();
	}

	protected String getString(CommandContext<CommandListenerWrapper> context, String name)
	{
		return StringArgumentType.getString(context, name);
	}

	/* Bool Argument */

	protected BoolArgumentType bool()
	{
		return BoolArgumentType.bool();
	}

	protected boolean getBool(CommandContext<CommandListenerWrapper> context, String name)
	{
		return BoolArgumentType.getBool(context, name);
	}

	/* Entity Argument */

	protected ArgumentEntity multipleEntities()
	{
		return ArgumentEntity.multipleEntities();
	}

	protected ArgumentEntity singleEntity()
	{
		return ArgumentEntity.a();
	}

	protected ArgumentEntity singlePlayer()
	{
		return ArgumentEntity.c();
	}

	protected ArgumentEntity multiplePlayers()
	{
		return ArgumentEntity.d();
	}

	protected Collection<? extends Entity> getEntities(CommandContext<CommandListenerWrapper> context, String name) throws CommandSyntaxException
	{
		return ArgumentEntity.b(context, name);
	}

	protected Entity getSingleEntity(CommandContext<CommandListenerWrapper> context, String name) throws CommandSyntaxException
	{
		return ArgumentEntity.a(context, name);
	}

	protected EntityPlayer getSinglePlayer(CommandContext<CommandListenerWrapper> context, String name) throws CommandSyntaxException
	{
		return ArgumentEntity.e(context, name);
	}

	protected Collection<EntityPlayer> getPlayers(CommandContext<CommandListenerWrapper> context, String name) throws CommandSyntaxException
	{
		return ArgumentEntity.f(context, name);
	}

	/* Enchantment Argument */

	protected ArgumentEnchantment itemEnchantment()
	{
		return ArgumentEnchantment.a();
	}

	protected Enchantment getEnchantment(CommandContext<CommandListenerWrapper> context, String name)
	{
		return ArgumentEnchantment.a(context, name);
	}

	/* Slot Argument */

	protected ArgumentInventorySlot itemSlot()
	{
		return ArgumentInventorySlot.a();
	}

	protected int getSlot(CommandContext<CommandListenerWrapper> context, String name)
	{
		return ArgumentInventorySlot.a(context, name);
	}

	/* Block Pos Argument */

	protected ArgumentPosition blockPos()
	{
		return ArgumentPosition.a();
	}

	protected BlockPosition getLoadedBlockPos(CommandContext<CommandListenerWrapper> context, String name) throws CommandSyntaxException
	{
		return ArgumentPosition.a(context, name);
	}

	protected BlockPosition getBlockPos(CommandContext<CommandListenerWrapper> context, String name) throws CommandSyntaxException
	{
		return ArgumentPosition.b(context, name);
	}

	/* NBT Argument */
	protected ArgumentNBTKey nbtPath()
	{
		return ArgumentNBTKey.a();
	}

	protected ArgumentNBTKey.h getNBTPath(CommandContext<CommandListenerWrapper> context, String name) throws CommandSyntaxException
	{
		return ArgumentNBTKey.a(context, name);
	}





	protected boolean isPlayer(CommandListenerWrapper r)
	{
		return r.getEntity() instanceof EntityPlayer;
	}

	protected boolean isPlayer(CommandContext<CommandListenerWrapper> context)
	{
		return context.getSource().getBukkitSender() instanceof Player;
	}

	protected Player getPlayer(CommandContext<CommandListenerWrapper> context) throws CommandException
	{
		if (!isPlayer(context))
			throw new CommandException(new NotPlayerException());
		else
			return (Player) context.getSource().getBukkitSender();
	}

	protected void sendMessage(CommandContext<CommandListenerWrapper> context, IChatBaseComponent... text)
	{
		IChatBaseComponent result = new ChatMessage("");
		for (IChatBaseComponent c : text)
		{
			result.addSibling(c);
		}

		context.getSource().sendMessage(result, true);
	}

	protected void sendMessage(CommandContext<CommandListenerWrapper> context, Object... text)
	{
		IChatBaseComponent c = new ChatMessage("");
		for (Object o : text)
		{
			if (o instanceof String)
			{
				c.addSibling(new ChatMessage(o.toString()));
			} else if (o instanceof IChatBaseComponent)
			{
				c.addSibling(((IChatBaseComponent) o));
			}
		}
		if (context.getSource().getEntity() != null)
			context.getSource().sendMessage(c, true);
	}

	protected void sendInfoMessage(CommandContext<CommandListenerWrapper> context, Object... text) throws CommandSyntaxException
	{
		String contextPlayerName = getPlayer(context).getName();
		String contextPlayerUUID = getPlayer(context).getUniqueId().toString();

		String warpName = "%warp%";
		String playerName = "%player%";

		try
		{
			warpName = getString(context, "warp");
			playerName = getSinglePlayer(context, "player").getName();
		} catch (Exception ignored)
		{

		}

		StringBuilder sb = new StringBuilder(Warp.getWarpSign());

		for (Object o : text)
		{
			if (o instanceof String)
			{
				String s = (String) o;
				s = s.replace("%warp%", warpName);
				s = s.replace("%a_warp%", "'" + warpName + "'");
				s = s.replace("%c_warp%", ChatColor.GOLD + warpName + ChatColor.AQUA);
				s = s.replace("%ca_warp%", "'" + ChatColor.GOLD +  warpName  + ChatColor.AQUA + "'");

				s = s.replace("%player%", playerName);
				s = s.replace("%c_player%", contextPlayerName);
				s = s.replace("%w_player%", ChatColor.WHITE + playerName + ChatColor.AQUA);
				s = s.replace("%cw_player%", ChatColor.WHITE + contextPlayerName + ChatColor.AQUA);

				s = s.replace("%uuid%", contextPlayerUUID);
				sb.append(s);
			} else
			{
				sb.append(o);
			}
		}
		context.getSource().sendMessage(new ChatMessage(sb.toString()), true);
	}
}
