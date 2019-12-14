package warp.commands.misc.modifyitem.sub;

import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.server.v1_14_R1.CommandListenerWrapper;
import net.minecraft.server.v1_14_R1.ItemStack;
import warp.commands.CommandException;
import warp.commands.exception.ItemDoesNotSupportDamageException;
import warp.commands.misc.modifyitem.EnumBaseType;
import warp.commands.misc.modifyitem.ModifyItemCommand;
import warp.commands.misc.modifyitem.SubCommand;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 04.08.2019
 * Project: SJP
 *
 ***********************/
public class DamageSubCommand extends ModifyItemCommand implements SubCommand
{
	public DamageSubCommand(EnumBaseType baseType)
	{
		super(baseType);
	}

	@Override
	public ArgumentBuilder<CommandListenerWrapper, ?> create(ArgumentBuilder<CommandListenerWrapper, ?> builder)
	{
		return literal("damage")
			.then(
				literal("get")
					.then(
						literal("damage")
							.executes(c -> {

								ItemStack itemStack = getItemStack(c);

								if (itemStack.h() == 0)
									throw new CommandException(new ItemDoesNotSupportDamageException());

								sendMessage(c, "Damage of ", itemStack.B(), " is " + itemStack.getDamage());

								return itemStack.getDamage();
							})
					)
					.then(
						literal("max")
							.executes(c -> {

								ItemStack itemStack = getItemStack(c);

								if (itemStack.h() == 0)
									throw new CommandException(new ItemDoesNotSupportDamageException());

								sendMessage(c, "Maximum damage of ", itemStack.B(), " is " + itemStack.h());

								return itemStack.h();
							})
					)
					.then(
						literal("durability")
							.executes(c -> {

								ItemStack itemStack = getItemStack(c);

								if (itemStack.h() == 0)
									throw new CommandException(new ItemDoesNotSupportDamageException());

								sendMessage(c, "Durability of ", itemStack.B(), " is " + (itemStack.h() - itemStack.getDamage()));

								return itemStack.h() - itemStack.getDamage();
							})
					)
			)
			.then(
				literal("set")
					.then(
						argument("amount", integer(0))
							.executes(c -> {

								ItemStack itemStack = getItemStack(c);

								if (itemStack.h() == 0)
									throw new CommandException(new ItemDoesNotSupportDamageException());

								setDamage(c, itemStack, getInteger(c, "amount"));

								sendMessage(c, "Damage of ", itemStack.B(), " is now " + itemStack.getDamage());

								return 1;
							})
					)
			)
			.then(
				literal("add")
					.executes(c -> {

						ItemStack itemStack = getItemStack(c);

						if (itemStack.h() == 0)
							throw new CommandException(new ItemDoesNotSupportDamageException());

						setDamage(c, itemStack, itemStack.getDamage() + 1);

						sendMessage(c, "Damage of ", itemStack.B(), " is now " + itemStack.getDamage());

						return 1;
					})
					.then(
						argument("amount", integer(0))
							.executes(c -> {

								ItemStack itemStack = getItemStack(c);

								if (itemStack.h() == 0)
									throw new CommandException(new ItemDoesNotSupportDamageException());

								setDamage(c, itemStack, itemStack.getDamage() + getInteger(c, "amount"));

								sendMessage(c, "Damage of ", itemStack.B(), " is now " + itemStack.getDamage());

								return 1;
							})
					)
			)
			.then(
				literal("remove")
					.executes(c -> {

						ItemStack itemStack = getItemStack(c);

						if (itemStack.h() == 0)
							throw new CommandException(new ItemDoesNotSupportDamageException());

						setDamage(c, itemStack, itemStack.getDamage() - 1);

						sendMessage(c, "Damage of ", itemStack.B(), " is now " + itemStack.getDamage());

						return 1;
					})
					.then(
						argument("amount", integer(0))
							.executes(c -> {

								ItemStack itemStack = getItemStack(c);

								if (itemStack.h() == 0)
									throw new CommandException(new ItemDoesNotSupportDamageException());

								setDamage(c, itemStack, itemStack.getDamage() - getInteger(c, "amount"));

								sendMessage(c, "Damage of ", itemStack.B(), " is now " + itemStack.getDamage());

								return 1;
							})
					)
			);
	}
}
