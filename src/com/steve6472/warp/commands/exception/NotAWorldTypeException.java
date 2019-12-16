package com.steve6472.warp.commands.exception;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 03.08.2019
 * Project: SJP
 *
 ***********************/
public class NotAWorldTypeException implements CustomException
{
	@Override
	public String getMessage()
	{
		return "That is not a valid world type";
	}
}
