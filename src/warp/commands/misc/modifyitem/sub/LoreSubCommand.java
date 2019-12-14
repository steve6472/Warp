package warp.commands.misc.modifyitem.sub;

import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.server.v1_14_R1.*;
import warp.commands.CommandException;
import warp.commands.exception.NoItemException;
import warp.commands.misc.modifyitem.EnumBaseType;
import warp.commands.misc.modifyitem.ModifyItemCommand;
import warp.commands.misc.modifyitem.SubCommand;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 04.08.2019
 * Project: SJP
 *
 ***********************/
public class LoreSubCommand extends ModifyItemCommand implements SubCommand
{
	public LoreSubCommand(EnumBaseType baseType)
	{
		super(baseType);
	}

	@Override
	public ArgumentBuilder<CommandListenerWrapper, ?> create(ArgumentBuilder<CommandListenerWrapper, ?> builder)
	{
		return literal("lore")
			.then(
				literal("set")
					.then(
						argument("line", integer(0))
							.then(
								argument("lore", ArgumentChatComponent.a())
									.executes(c -> {

										ItemStack itemStack = getItemStack(c);

										IChatBaseComponent text = (ArgumentChatComponent.a(c, "lore"));
										IChatBaseComponent edited = ChatComponentUtils.filterForDisplay(c.getSource(), text, c.getSource().getEntity(), 0);

										String json = IChatBaseComponent.ChatSerializer.a(edited);

										setLore(itemStack, json, getInteger(c, "line"));

										sendMessage(c, "Lore changed to '", edited, "'");

										return 1;
									})
							)
					)
			)
			.then(
				literal("remove")
					.then(
						argument("line", integer(0))
							.executes(c -> {

								ItemStack itemStack = getItemStack(c);

								setLore(itemStack, null, getInteger(c, "line"));

								return 1;
							})
					)
			)
			.then(
				literal("add")
					.then(
						argument("line", integer(0))
							.then(
								argument("lore", ArgumentChatComponent.a())
									.executes(c -> {

										ItemStack itemStack = getItemStack(c);

										IChatBaseComponent text = (ArgumentChatComponent.a(c, "lore"));
										IChatBaseComponent edited = ChatComponentUtils.filterForDisplay(c.getSource(), text, c.getSource().getEntity(), 0);

										String json = IChatBaseComponent.ChatSerializer.a(edited);

										addLore(itemStack, json, getInteger(c, "line"));

										sendMessage(c, "Lore changed to '", edited, "'");


										return 1;
									})
							)
					)
			);
	}

	private void addLore(ItemStack item, String lore, int line) throws CommandException
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
			while (list.size() < line)
				list.add(new NBTTagString("{\"text\":\"\"}"));
			list.add(new NBTTagString(lore));
		} else
		{
			list.add(line, new NBTTagString(lore));
		}
	}

	private void setLore(ItemStack item, String lore, int line) throws CommandException
	{
		if (item.isEmpty())
			throw new CommandException(new NoItemException());

		if (lore == null)
		{
			if (item.getTag() == null) return;
			if (!item.getTag().hasKeyOfType("display", 10)) return;
			if (!item.getTag().getCompound("display").hasKey("Lore")) return;

			NBTTagList list = item.getTag().getCompound("display").getList("Lore", 8);
			list.remove(line);
			if (list.isEmpty())
				item.getTag().getCompound("display").remove("Lore");
			item.getTag().getCompound("display");
			if (!item.getTag().getCompound("display").isEmpty()) return;
			item.getTag().remove("display");
			if (!item.getTag().isEmpty()) return;
			item.setTag(null);
			return;
		}

		item.getOrCreateTag();
		assert item.getTag() != null;
		if (!item.getTag().hasKeyOfType("display", 10))
			item.getTag().set("display", new NBTTagCompound());
		if (!item.getTag().getCompound("display").hasKeyOfType("Lore", 9))
			item.getTag().getCompound("display").set("Lore", new NBTTagList());

		NBTTagList list = item.getTag().getCompound("display").getList("Lore", 8);
		if (list.size() <= line)
		{
			while (list.size() < line)
				list.add(new NBTTagString("{\"text\":\"\"}"));
			list.add(new NBTTagString(lore));
		} else
		{
			list.set(line, new NBTTagString(lore));
		}
	}
}
