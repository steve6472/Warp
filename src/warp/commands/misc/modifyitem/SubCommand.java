package warp.commands.misc.modifyitem;

import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.server.v1_14_R1.CommandListenerWrapper;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 03.08.2019
 * Project: SJP
 *
 ***********************/
public interface SubCommand
{
	ArgumentBuilder<CommandListenerWrapper, ?> create(ArgumentBuilder<CommandListenerWrapper, ?> builder);
}
