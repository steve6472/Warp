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
public class NameSubCommand extends ModifyItemCommand implements SubCommand
{
	public NameSubCommand(EnumBaseType baseType)
	{
		super(baseType);
	}

	@Override
	public ArgumentBuilder<CommandListenerWrapper, ?> create(ArgumentBuilder<CommandListenerWrapper, ?> builder)
	{
		return literal("name")
			.then(
				literal("set")
					.then(
						argument("item name", ArgumentChatComponent.a())
							.executes(c -> {

								ItemStack itemStack = getItemStack(c);

								IChatBaseComponent text = (ArgumentChatComponent.a(c, "item name"));
								IChatBaseComponent edited = ChatComponentUtils.filterForDisplay(c.getSource(), text, c.getSource().getEntity(), 0);

								String json = IChatBaseComponent.ChatSerializer.a(edited);

								setName(itemStack, json);

								sendMessage(c, "Name changed to ", itemStack.B());

								return 1;
							})
					)
			)
			.then(
				literal("clear")
					.executes(c -> {

						ItemStack itemStack = getItemStack(c);
						setName(itemStack, null);

						sendMessage(c, "Name has been cleared");

						return 1;
					})
			);
	}

	private void setName(ItemStack item, String name) throws CommandException
	{
		if (item.isEmpty())
			throw new CommandException(new NoItemException());

		if (name == null)
		{
			if (item.getTag() == null) return;
			if (!item.getTag().hasKeyOfType("display", 10)) return;
			if (!item.getTag().getCompound("display").hasKey("Name")) return;
			item.getTag().getCompound("display").remove("Name");
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

		item.getTag().getCompound("display").set("Name", new NBTTagString(name));
	}
}
