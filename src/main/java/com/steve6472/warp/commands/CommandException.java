package com.steve6472.warp.commands;

import com.mojang.brigadier.exceptions.CommandExceptionType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.steve6472.warp.commands.exception.CustomException;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 30.07.2019
 * Project: SJP
 *
 ***********************/
public class CommandException extends CommandSyntaxException
{
	public CommandException(CommandExceptionType type, String message)
	{
		super(type, () -> message);
	}

	public CommandException(CommandExceptionType type)
	{
		super(type, () -> "");
	}

	public CommandException(CustomException type)
	{
		super(type, type::getMessage);
	}
}
