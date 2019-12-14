package warp.commands.misc;

import com.mojang.brigadier.CommandDispatcher;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_14_R1.CommandListenerWrapper;
import net.minecraft.server.v1_14_R1.EntityPlayer;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer;
import warp.Warp;
import warp.commands.CustomCommand;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 08.08.2019
 * Project: SJP
 *
 ***********************/
public class MSPTDisplayCommand extends CustomCommand
{
	public MSPTDisplayCommand(Warp warp)
	{
		super(warp);
	}

	@Override
	public void register(CommandDispatcher<CommandListenerWrapper> dispatcher)
	{
		dispatcher.register(
			literal(getName())
				.executes(c -> {

					if (c.getSource().getEntity() instanceof EntityPlayer)
					{
						CraftPlayer player = (CraftPlayer) c.getSource().getEntity().getBukkitEntity();
						TextComponent start = new TextComponent("MS Per Tick: ");

						TextComponent ms = new TextComponent("" + getWarp().getVanillaServer().aR());
						ms.setColor(ChatColor.GOLD);
						ms.setUnderlined(true);

						start.addExtra(ms);

						player.spigot().sendMessage(ChatMessageType.ACTION_BAR, start);
					}

					return 1;
				})
		);
	}

	@Override
	public String getName()
	{
		return "displaymspt";
	}

	@Override
	public int getPermissionLevel()
	{
		return 1;
	}
}
