package warp.commands.exception;

import com.mojang.brigadier.exceptions.CommandExceptionType;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 31.07.2019
 * Project: SJP
 *
 ***********************/
public interface CustomException extends CommandExceptionType
{
	String getMessage();
}
