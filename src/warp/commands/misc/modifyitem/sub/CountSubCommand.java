package warp.commands.misc.modifyitem.sub;

import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.server.v1_14_R1.CommandListenerWrapper;
import net.minecraft.server.v1_14_R1.ItemStack;
import warp.commands.CommandException;
import warp.commands.misc.modifyitem.EnumBaseType;
import warp.commands.misc.modifyitem.ModifyItemCommand;
import warp.commands.misc.modifyitem.SubCommand;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 05.08.2019
 * Project: SJP
 *
 ***********************/
public class CountSubCommand extends ModifyItemCommand implements SubCommand
{
	public CountSubCommand(EnumBaseType baseType)
	{
		super(baseType);
	}

	@Override
	public ArgumentBuilder<CommandListenerWrapper, ?> create(ArgumentBuilder<CommandListenerWrapper, ?> builder)
	{
		return literal("count")
			.then(
				literal("add")
					.executes(c -> {

						ItemStack itemStack = getItemStack(c);

						return setCount(itemStack, itemStack.getCount() + 1);
					})
					.then(
						argument("count", integer(1, 64))
							.executes(c -> {

								ItemStack itemStack = getItemStack(c);

								return setCount(itemStack, itemStack.getCount() + getInteger(c, "count")); //return addded count
							})
					)
			)
			.then(
				literal("remove")
				.executes(c -> {

					ItemStack itemStack = getItemStack(c);

					return setCount(itemStack, itemStack.getCount() - 1);
				})
				.then(
					argument("count", integer(1, 64))
						.executes(c -> {

							ItemStack itemStack = getItemStack(c);

							return setCount(itemStack, itemStack.getCount() + getInteger(c, "count"));
						})
				)
			)
			.then(
				literal("set")
					.then(
						argument("count", integer(0, 64))
							.executes(c -> {

								ItemStack itemStack = getItemStack(c);

								return setCount(itemStack, getInteger(c, "count"));
							})
					)
			);
	}

	private int setCount(ItemStack itemStack, int i) throws CommandException
	{
		if (itemStack.getCount() + i > itemStack.getMaxStackSize())
			throw new CommandException(() -> "Item can not have count bigger than " + itemStack.getMaxStackSize());

		int before = itemStack.getCount();

		itemStack.setCount(itemStack.getCount() + i);
		return itemStack.getCount() - before;
	}
}
