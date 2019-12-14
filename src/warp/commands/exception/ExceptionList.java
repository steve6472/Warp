package warp.commands.exception;

import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import net.minecraft.server.v1_14_R1.ChatMessage;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 04.08.2019
 * Project: SJP
 *
 ***********************/
public class ExceptionList
{
	public static DynamicCommandExceptionType enchantUnknown = new DynamicCommandExceptionType(c -> new ChatMessage("enchantment.unknown", c));
}
