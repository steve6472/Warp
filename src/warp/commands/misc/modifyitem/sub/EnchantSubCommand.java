package warp.commands.misc.modifyitem.sub;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.v1_14_R1.*;
import org.bukkit.ChatColor;
import warp.commands.misc.modifyitem.EnumBaseType;
import warp.commands.misc.modifyitem.ModifyItemCommand;
import warp.commands.misc.modifyitem.SubCommand;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 03.08.2019
 * Project: SJP
 *
 ***********************/
public class EnchantSubCommand extends ModifyItemCommand implements SubCommand
{
	public EnchantSubCommand(EnumBaseType baseType)
	{
		super(baseType);
	}

	@Override
	public ArgumentBuilder<CommandListenerWrapper, ?> create(ArgumentBuilder<CommandListenerWrapper, ?> builder)
	{
		return literal("enchantment")
			.then(
				literal("set")
					.then(
						argument("enchantment", itemEnchantment())
							.executes(c -> {

								Enchantment enchantment = getEnchantment(c, "enchantment");
								ItemStack itemStack = getItemStack(c);

								setEnchantment(itemStack, enchantment, 1);

								sendLevelChangeMessage(c, itemStack, enchantment, 1);

								return 1;
							})
							.then(
								argument("level", integer(0, 32767))
									.executes(c -> {

										Enchantment enchantment = getEnchantment(c, "enchantment");
										int level = getInteger(c, "level");
										ItemStack itemStack = getItemStack(c);

										setEnchantment(itemStack, enchantment, level);

										sendLevelChangeMessage(c, itemStack, enchantment, level);

										return 1;
									})
							)
					)
			)
			.then(
				literal("add")
					.then(
						argument("enchantment", itemEnchantment())
							.executes(c -> {

								Enchantment enchantment = getEnchantment(c, "enchantment");
								ItemStack itemStack = getItemStack(c);

								int level = getEnchantmentLevel(itemStack, enchantment);
								setEnchantment(itemStack, enchantment, level + 1);

								sendLevelChangeMessage(c, itemStack, enchantment, level + 1);

								return 1;
							})
							.then(
							argument("level", integer(0, 32767))
								.executes(c -> {

									Enchantment enchantment = getEnchantment(c, "enchantment");
									ItemStack itemStack = getItemStack(c);
									int levelAdd = getInteger(c, "level");

									int level = getEnchantmentLevel(itemStack, enchantment);
									setEnchantment(itemStack, enchantment, level + levelAdd);

									sendLevelChangeMessage(c, itemStack, enchantment, level + levelAdd);

									return 1;
								})
						)
					)
			)
			.then(
				literal("clear")
					.executes(c -> {

						ItemStack itemStack = getItemStack(c);

						clearEnchantments(itemStack);

						sendMessage(c, "Cleared all enchantments from ", itemStack.B());

						return 1;
					})
					.then(
						argument("enchantment", itemEnchantment())
							.executes(c -> {

								Enchantment enchantment = getEnchantment(c, "enchantment");
								ItemStack itemStack = getItemStack(c);

								setEnchantment(itemStack, enchantment, 0);

								sendClearMessage(c, itemStack, enchantment);

								return 1;
							})
					)
			)
			.then(
				literal("remove")
					.then(
						argument("enchantment", itemEnchantment())
							.executes(c -> {

								Enchantment enchantment = getEnchantment(c, "enchantment");
								ItemStack itemStack = getItemStack(c);

								int level = getEnchantmentLevel(itemStack, enchantment);
								setEnchantment(itemStack, enchantment, level - 1);

								sendLevelChangeMessage(c, itemStack, enchantment, level - 1);

								return 1;
							})
							.then(
								argument("level", integer(0, 32767))
									.executes(c -> {

										Enchantment enchantment = getEnchantment(c, "enchantment");
										ItemStack itemStack = getItemStack(c);
										int levelRemove = getInteger(c, "level");

										int level = getEnchantmentLevel(itemStack, enchantment);
										setEnchantment(itemStack, enchantment, level - levelRemove);

										sendLevelChangeMessage(c, itemStack, enchantment, level - levelRemove);

										return 1;
									})
							)
					)
			)
			.then(
				literal("get")
					.then(argument("enchantment", itemEnchantment())
						.executes(c -> {

							ItemStack itemStack = getItemStack(c);
							Enchantment enchantment = getEnchantment(c, "enchantment");

							int lvl = getEnchantmentLevel(itemStack, enchantment);

							sendLevelMessage(c, itemStack, enchantment, lvl);

							return lvl;
						})

				)
			);
	}

	private void sendLevelMessage(CommandContext<CommandListenerWrapper> context, ItemStack itemStack, Enchantment enchantment, int level)
	{
		MinecraftKey key = IRegistry.ENCHANTMENT.getKey(enchantment);
		if (key != null)
		{
			sendMessage(context, "Level of ", "enchantment.minecraft." + key.getKey(), " on ", itemStack.B(), " is " + level);
		} else
		{
			sendMessage(context, ChatColor.RED + "ResourceLocation is 'null', this should not happen!");
		}
	}

	private void sendLevelChangeMessage(CommandContext<CommandListenerWrapper> context, ItemStack itemStack, Enchantment enchantment, int level)
	{
		MinecraftKey key = IRegistry.ENCHANTMENT.getKey(enchantment);
		if (key != null)
		{
			sendMessage(context, "Level of ", "enchantment.minecraft." + key.getKey(), " on ", itemStack.B(), " is now " + level);
		} else
		{
			sendMessage(context, ChatColor.RED + "ResourceLocation is 'null', this should not happen!");
		}
	}

	private void sendClearMessage(CommandContext<CommandListenerWrapper> context, ItemStack itemStack, Enchantment enchantment)
	{
		MinecraftKey key = IRegistry.ENCHANTMENT.getKey(enchantment);
		if (key != null)
		{
			sendMessage(context, "Cleared ", "enchantment.minecraft." + key.getKey(), " from ", itemStack.B());
		} else
		{
			sendMessage(context, ChatColor.RED + "ResourceLocation is 'null', this should not happen!");
		}
	}
}
