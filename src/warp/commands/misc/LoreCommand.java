package warp.commands.misc;

import com.mojang.brigadier.CommandDispatcher;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_14_R1.*;
import warp.Warp;
import warp.commands.CommandException;
import warp.commands.CustomCommand;
import warp.commands.exception.NoItemException;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 31.07.2019
 * Project: SJP
 *
 ***********************/
public class LoreCommand extends CustomCommand
{
	public LoreCommand(Warp warp)
	{
		super(warp);
	}

	@Override
	public void register(CommandDispatcher<CommandListenerWrapper> dispatcher)
	{
		dispatcher.register(
			literal("lore")
				.then(
					literal("basic")
						.then(
							argument("line", integer(0, 32))
								.then(
									argument("text", ArgumentChat.a())
										.executes(c -> {

											if (c.getSource().getEntity() instanceof EntityPlayer)
											{
												EntityPlayer player = (EntityPlayer) c.getSource().getEntity();
												ItemStack item = player.getItemInMainHand();

												String edited = editText(ArgumentChat.a(c, "text").getString());

												setLore(item,
														"{\"text\":\"" + edited + "\"}",
														getInteger(c, "line"));

												player.getBukkitEntity().sendMessage("Lore at line " + getInteger(c, "line") + " changed to");
												player.getBukkitEntity().sendMessage(edited);
											}

											return 1;
										})
								)
						)
				)
				.then(
					literal("advanced")
						.then(
							argument("line", integer(0, 32))
								.then(
									argument("text", ArgumentChatComponent.a())
										.executes(c -> {

											if (c.getSource().getEntity() instanceof EntityPlayer)
											{
												EntityPlayer player = (EntityPlayer) c.getSource().getEntity();
												ItemStack item = player.getItemInMainHand();

												IChatBaseComponent base = new ChatComponentText("");
												base.setChatModifier(new ChatModifier().setColor(EnumChatFormat.WHITE).setItalic(false));

												IChatBaseComponent text = (ArgumentChatComponent.a(c, "text"));
												IChatBaseComponent edited = ChatComponentUtils.filterForDisplay(c.getSource(), text, player, 0);

												IChatBaseComponent result = base.addSibling(edited);

												String json = IChatBaseComponent.ChatSerializer.a(result);

												sendMessage(c, "Lore at line " + getInteger(c, "line") + " changed to");

												sendMessage(c, edited);

												setLore(item, json, getInteger(c, "line"));
											}

											return 1;
										})
								)
						)
				)
				.then(
					literal("edit")
						.then(
							argument("line", integer(0, 32))
								.then(
									literal("basic")
										.executes(c -> {

											if (c.getSource().getEntity() instanceof EntityPlayer)
											{
												EntityPlayer player = (EntityPlayer) c.getSource().getEntity();
												ItemStack item = player.getItemInMainHand();

												String text = getBasicLore(item, getInteger(c, "line"));
												text = text.substring(9, text.length() - 2);
												text = reverseEditText(text);

												TextComponent tc = new TextComponent();
												tc.setColor(net.md_5.bungee.api.ChatColor.GOLD);
												tc.setText(text);
												tc.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/lore basic " + getInteger(c, "line") + " " + text));
												sendMessage(c, tc);
											}

											return 1;
										})
								)
								.then(
									literal("advanced")
										.executes(c -> {

											if (c.getSource().getEntity() instanceof EntityPlayer)
											{
												EntityPlayer player = (EntityPlayer) c.getSource().getEntity();
												ItemStack item = player.getItemInMainHand();

												String text = getBasicLore(item, getInteger(c, "line"));
												text = text.substring(40, text.length() - 11);

												TextComponent tc = new TextComponent();
												tc.setColor(net.md_5.bungee.api.ChatColor.GOLD);
												tc.setText(text);
												tc.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/lore advanced " + getInteger(c, "line") + " " + text));
												sendMessage(c, tc);
											}


											return 1;
										})
								)
						)
				)
		);
	}

	private String editText(String text)
	{
		return text.replace("\\&", "[AND_SYMBOL]").replace("&", "§").replace("[AND_SYMBOL]", "&");
	}

	private String reverseEditText(String text)
	{
		return text.replace("&", "[AND_SYMBOL]").replace("§", "&").replace("[AND_SYMBOL]", "\\&");
	}

	private void setLore(ItemStack item, String lore, int line) throws CommandException
	{
		if (item.isEmpty())
			throw new CommandException(new NoItemException());

		item.getOrCreateTag();
		assert item.getTag() != null;
		if (!item.getTag().hasKeyOfType("display", 10))
			item.getTag().set("display", new NBTTagCompound());
		if (!item.getTag().getCompound("display").hasKeyOfType("Lore", 9))
			item.getTag().getCompound("display").set("Lore", new NBTTagList());

		NBTTagList list = item.getTag().getCompound("display").getList("Lore", 8);
		if (list.size() <= line)
		{
			list.add(new NBTTagString(lore));
		} else
		{
			list.set(line, new NBTTagString(lore));
		}
	}

	private String getBasicLore(ItemStack item, int line) throws CommandException
	{
		if (item.isEmpty())
			throw new CommandException(new NoItemException());

		item.getOrCreateTag();
		assert item.getTag() != null;
		if (!item.getTag().hasKeyOfType("display", 10))
			item.getTag().set("display", new NBTTagCompound());
		if (!item.getTag().getCompound("display").hasKeyOfType("Lore", 9))
			item.getTag().getCompound("display").set("Lore", new NBTTagList());

		NBTTagList list = item.getTag().getCompound("display").getList("Lore", 8);
		return list.getString(line);
	}

	@Override
	public String getName()
	{
		return "lore";
	}

	@Override
	public int getPermissionLevel()
	{
		return 0;
	}
}
