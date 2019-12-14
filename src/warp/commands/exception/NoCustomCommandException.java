package warp.commands.exception;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 01.08.2019
 * Project: SJP
 *
 ***********************/
public class NoCustomCommandException implements CustomException
{
	@Override
	public String getMessage()
	{
		return "No Custom Command by that name exists";
	}
}
