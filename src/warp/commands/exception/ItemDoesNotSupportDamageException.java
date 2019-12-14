package warp.commands.exception;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 04.08.2019
 * Project: SJP
 *
 ***********************/
public class ItemDoesNotSupportDamageException implements CustomException
{
	@Override
	public String getMessage()
	{
		return "That item does not support damage";
	}
}
