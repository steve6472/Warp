package warp.commands.exception;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 30.07.2019
 * Project: SJP
 *
 ***********************/
public class NotPlayerException implements CustomException
{
	@Override
	public String getMessage()
	{
		return "Not a player";
	}
}
