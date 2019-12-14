package warp.commands.exception;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 01.08.2019
 * Project: SJP
 *
 ***********************/
public class TooManyFunctionsException implements CustomException
{
	@Override
	public String getMessage()
	{
		return "You have somehow specified too many functions. This should not be possible!";
	}
}
