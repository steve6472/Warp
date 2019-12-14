package warp.commands.misc;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.v1_14_R1.*;
import warp.Warp;
import warp.commands.CustomCommand;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 31.07.2019
 * Project: SJP
 *
 ***********************/
public class ForceEnchantCommand extends CustomCommand
{
	public ForceEnchantCommand(Warp warp)
	{
		super(warp);
	}

	@Override
	public void register(CommandDispatcher<CommandListenerWrapper> dispatcher)
	{
		dispatcher.register(
			literal("forceenchant")
				.requires(r -> r.hasPermission(2))
					.then(
						argument("targets", multipleEntities())
							.then(
								argument("enchantment", itemEnchantment())
									.then(
										argument("level", integer(Short.MIN_VALUE, Short.MAX_VALUE))
											.executes(c -> {

												for (Entity e : getEntities(c, "targets"))
												{
													if (e instanceof EntityLiving)
													{
														EntityLiving el = (EntityLiving) e;
														ItemStack is = el.getItemInMainHand();
														if (!is.isEmpty())
														{
															addEnchantment(is, getEnchantment(c, "enchantment"), getInteger(c, "level"));
														}
													}
												}

												return 1;
											})
									)
							)
					)
		);
	}

	private void addEnchantment(ItemStack is, Enchantment enchantment, int i)
	{
		is.getOrCreateTag();
		assert is.getTag() != null;
		if (!is.getTag().hasKeyOfType("Enchantments", 9)) {
			is.getTag().set("Enchantments", new NBTTagList());
		}

		NBTTagList nbttaglist = is.getTag().getList("Enchantments", 10);
		NBTTagCompound nbttagcompound = new NBTTagCompound();
		nbttagcompound.setString("id", String.valueOf(IRegistry.ENCHANTMENT.getKey(enchantment)));
		nbttagcompound.setShort("lvl", (short) i);
		nbttaglist.add(nbttagcompound);
	}

	@Override
	public String getName()
	{
		return "forceenchant";
	}

	@Override
	public int getPermissionLevel()
	{
		return 2;
	}
}
