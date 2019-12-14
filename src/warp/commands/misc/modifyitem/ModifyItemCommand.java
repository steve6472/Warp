package warp.commands.misc.modifyitem;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.server.v1_14_R1.*;
import org.bukkit.ChatColor;
import warp.Warp;
import warp.commands.CommandException;
import warp.commands.CustomCommand;
import warp.commands.misc.modifyitem.sub.*;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 03.08.2019
 * Project: SJP
 *
 ***********************/
public class ModifyItemCommand extends CustomCommand
{
	private EnumBaseType baseType;

	public ModifyItemCommand(Warp warp)
	{
		super(warp);
	}

	public ModifyItemCommand(EnumBaseType baseType)
	{
		super(null);
		this.baseType = baseType;
	}

	public EnumBaseType getBaseType()
	{
		return baseType;
	}

	private ArgumentBuilder<CommandListenerWrapper, ?> entityLiteral(Function<ArgumentBuilder<CommandListenerWrapper, ?>, ArgumentBuilder<CommandListenerWrapper, ?>> func)
	{
		return literal("entity").then(func.apply(argument("entity", singleEntity())));
	}

	private ArgumentBuilder<CommandListenerWrapper, ?> blockLiteral(Function<ArgumentBuilder<CommandListenerWrapper, ?>, ArgumentBuilder<CommandListenerWrapper, ?>> func)
	{
		return literal("block").then(func.apply(argument("block", blockPos())));
	}

	private ArgumentBuilder<CommandListenerWrapper, ?> slotLiteral(Function<ArgumentBuilder<CommandListenerWrapper, ?>, ArgumentBuilder<CommandListenerWrapper, ?>> func)
	{
		return literal("slot").then(func.apply(argument("name", itemSlot())));
	}

	private ArgumentBuilder<CommandListenerWrapper, ?> itemLiteral(Function<ArgumentBuilder<CommandListenerWrapper, ?>, ArgumentBuilder<CommandListenerWrapper, ?>> func)
	{
		return literal("item").then(func.apply(argument("item", nbtPath())));
	}

	@Override
	public void register(CommandDispatcher<CommandListenerWrapper> dispatcher)
	{
		LiteralArgumentBuilder<CommandListenerWrapper> builder = literal(getName()).requires(r -> r.hasPermission(2));

		List<Class<? extends SubCommand>> subCommands = new ArrayList<>();
		subCommands.add(EnchantSubCommand.class);
		subCommands.add(DamageSubCommand.class);
		subCommands.add(NameSubCommand.class);
		subCommands.add(LoreSubCommand.class);
		subCommands.add(CountSubCommand.class);

		for (Class<? extends SubCommand> sub : subCommands)
		{
			builder.then(
				blockLiteral(f0 -> f0.then(
					slotLiteral(f1 ->
					{
						try
						{
							return f1.then(sub.getDeclaredConstructor(EnumBaseType.class).newInstance(EnumBaseType.BLOCK_SLOT).create(f1));
						} catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e)
						{
							e.printStackTrace();
						}
						return null;
					}
					))));/*
			builder.then(
				blockLiteral(f0 -> f0.then(
					itemLiteral(f1 ->
						{
							try
							{
								return f1.then(sub.getDeclaredConstructor(EnumBaseType.class).newInstance(EnumBaseType.BLOCK_ITEM).create(f1));
							} catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e)
							{
								e.printStackTrace();
							}
							return null;
						}
					))));*/
			builder.then(
				entityLiteral(f0 -> f0.then(
					slotLiteral(f1 ->
						{
							try
							{
								return f1.then(sub.getDeclaredConstructor(EnumBaseType.class).newInstance(EnumBaseType.ENTITY_SLOT).create(f1));
							} catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e)
							{
								e.printStackTrace();
							}
							return null;
						}
					))));/*
			builder.then(
				entityLiteral(f0 -> f0.then(
					itemLiteral(f1 ->
						{
							try
							{
								return f1.then(sub.getDeclaredConstructor(EnumBaseType.class).newInstance(EnumBaseType.ENTITY_ITEM).create(f1));
							} catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e)
							{
								e.printStackTrace();
							}
							return null;
						}
					))));*/
		}

		dispatcher.register(builder);

		for (String s : dispatcher.getAllUsage(dispatcher.getRoot().getChild("modifyitem"), null, false))
		{
			System.out.println("modifyitem " + s);
		}
	}

	protected void setDamage(CommandContext<CommandListenerWrapper> context, ItemStack itemStack, int amount) throws CommandSyntaxException
	{
		itemStack.setDamage(amount);
		if (itemStack.getDamage() <= 0)
		{
			replaceItemInInventory(context, itemStack);
		}
	}

	protected ItemStack getItemStack(CommandContext<CommandListenerWrapper> context) throws CommandSyntaxException
	{
		switch (baseType)
		{
			case BLOCK_SLOT:
				return getItemFromBlock(context, getBlockPos(context, "block"), getSlot(context, "name"));
			case ENTITY_SLOT:
				return getItemFromEntity(getSingleEntity(context, "entity"), getSlot(context, "name"));
			default:
				throw new CommandException(() -> "" + ChatColor.RED + baseType + " is not supported yet!");
		}
	}

	protected ItemStack getItemFromBlock(CommandContext<CommandListenerWrapper> context, BlockPosition position, int inventorySlot) throws CommandSyntaxException
	{
		TileEntity te = context.getSource().getWorld().getTileEntity(position);
		if (te == null)
		{
			throw new SimpleCommandExceptionType(new ChatMessage("commands.data.block.invalid")).create();
		}

		if (!(te instanceof TileEntityContainer))
		{
			throw new CommandException(() -> "The target block does not contain inventory");
		}

		TileEntityContainer te1 = (TileEntityContainer) te;
		return te1.getItem(inventorySlot);
	}

	protected ItemStack getItemFromEntity(Entity entity, int inventorySlot) throws CommandException
	{
		if (entity instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer) entity;
			if (inventorySlot >= 98 && inventorySlot <= 103)
			{
				switch (inventorySlot)
				{
					case 98 : return player.inventory.getItemInHand();
					case 99 : return player.inventory.extraSlots.get(0);
					case 100 : return player.inventory.getArmorContents().get(0);
					case 101 : return player.inventory.getArmorContents().get(1);
					case 102 : return player.inventory.getArmorContents().get(2);
					case 103 : return player.inventory.getArmorContents().get(3);
				}
			} else
			{
				return player.inventory.items.get(inventorySlot);
			}
		} else
		{
			if (entity instanceof EntityLiving)
			{
				EntityLiving living = (EntityLiving) entity;
				switch (inventorySlot)
					{
					case 98 : return living.getItemInMainHand();
					case 99 : return living.getItemInOffHand();
					case 100 : return living.getEquipment(EnumItemSlot.FEET);
					case 101 : return living.getEquipment(EnumItemSlot.LEGS);
					case 102 : return living.getEquipment(EnumItemSlot.CHEST);
					case 103 : return living.getEquipment(EnumItemSlot.HEAD);
				}
			} else
			{
				throw new CommandException(() -> "Entity not supported! (" + entity.getClass().getName() + ")");
			}
		}

		return ItemStack.a;
	}

	protected void replaceItemInInventory(CommandContext<CommandListenerWrapper> context, ItemStack itemStack) throws CommandSyntaxException
	{
		switch (baseType)
		{
			case BLOCK_SLOT:
				replaceItemInInventory(context, getBlockPos(context, "block"), getSlot(context, "name"), itemStack);
				return;
			case ENTITY_SLOT:
				replaceItemInInventory(getSingleEntity(context, "entity"), getSlot(context, "name"), itemStack);
				return;
			default:
				throw new CommandException(() -> "" + ChatColor.RED + baseType + " is not supported yet!");
		}
	}

	protected boolean replaceItemInInventory(Entity entity, int inventorySlot, ItemStack itemStack)
	{
		return entity.a_(inventorySlot, itemStack);
	}

	protected void replaceItemInInventory(CommandContext<CommandListenerWrapper> context, BlockPosition position, int inventorySlot, ItemStack itemStack) throws CommandSyntaxException
	{
		TileEntity te = context.getSource().getWorld().getTileEntity(position);
		if (te == null)
		{
			throw new SimpleCommandExceptionType(new ChatMessage("commands.data.block.invalid")).create();
		}

		if (!(te instanceof TileEntityContainer))
		{
			throw new CommandException(() -> "The target block does not contain inventory");
		}

		TileEntityContainer te1 = (TileEntityContainer) te;
		te1.setItem(inventorySlot, itemStack);
	}

	protected void clearEnchantments(ItemStack is)
	{
		if (is.getTag() == null) return;
		if (!is.getTag().hasKeyOfType("Enchantments", 9)) return;

		is.getTag().remove("Enchantments");
		if (is.getTag().isEmpty())
			is.setTag(null);
	}

	protected short getEnchantmentLevel(ItemStack is, Enchantment enchantment)
	{
		if (is.getTag() == null) return 0;
		if (!is.getTag().hasKeyOfType("Enchantments", 9)) return 0;

		NBTTagList nbttaglist = is.getTag().getList("Enchantments", 10);
		for (NBTBase nbtBase : nbttaglist)
		{
			NBTTagCompound tag = (NBTTagCompound) nbtBase;
			if (tag.getString("id").equals(String.valueOf(IRegistry.ENCHANTMENT.getKey(enchantment))))
			{
				return tag.getShort("lvl");
			}
		}

		return 0;
	}

	protected void setEnchantment(ItemStack is, Enchantment enchantment, int i)
	{
		if (i <= 0)
		{
			if (is.getTag() == null) return;
			if (!is.getTag().hasKeyOfType("Enchantments", 9)) return;

			NBTTagList nbttaglist = is.getTag().getList("Enchantments", 10);
			for (Iterator<NBTBase> iter = nbttaglist.iterator(); iter.hasNext(); )
			{
				NBTTagCompound tag = (NBTTagCompound) iter.next();
				if (tag.getString("id").equals(String.valueOf(IRegistry.ENCHANTMENT.getKey(enchantment))))
				{
					iter.remove();
				}
			}
			if (nbttaglist.size() == 0)
			{
				is.getTag().remove("Enchantments");
			}

			if (is.getTag().isEmpty())
				is.setTag(null);
		} else
		{
			is.getOrCreateTag();
			assert is.getTag() != null;

			boolean isNew = false;

			if (!is.getTag().hasKeyOfType("Enchantments", 9))
			{
				isNew = true;
				is.getTag().set("Enchantments", new NBTTagList());
			}

			NBTTagList nbttaglist = is.getTag().getList("Enchantments", 10);

			if (!isNew)
			{
				for (NBTBase nbtBase : nbttaglist)
				{
					NBTTagCompound tag = (NBTTagCompound) nbtBase;
					if (tag.getString("id").equals(String.valueOf(IRegistry.ENCHANTMENT.getKey(enchantment))))
					{
						tag.setShort("lvl", (short) i);
						return;
					}
				}
			}

			NBTTagCompound nbttagcompound = new NBTTagCompound();
			nbttagcompound.setString("id", String.valueOf(IRegistry.ENCHANTMENT.getKey(enchantment)));
			nbttagcompound.setShort("lvl", (short) i);
			nbttaglist.add(nbttagcompound);
		}
	}

	@Override
	public String getName()
	{
		return "modifyitem";
	}

	@Override
	public int getPermissionLevel()
	{
		return 2;
	}
}
