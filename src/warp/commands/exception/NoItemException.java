package warp.commands.exception;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 31.07.2019
 * Project: SJP
 *
 ***********************/
public class NoItemException implements CustomException
{
	@Override
	public String getMessage()
	{
		return "You must be holding an item to apply Lore";
	}
}
