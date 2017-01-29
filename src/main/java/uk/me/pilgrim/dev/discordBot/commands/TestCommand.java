/**
 * This file is part of DiscordBot.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */
package uk.me.pilgrim.dev.discordBot.commands;

import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IUser;
import uk.me.pilgrim.dev.core.commands.CommandResult;
import uk.me.pilgrim.dev.core.commands.annotations.Command;
import uk.me.pilgrim.dev.core.commands.annotations.Desc;
import uk.me.pilgrim.dev.core.commands.sources.CommandSource;
import uk.me.pilgrim.dev.core.util.Context;

/**
 * @author Benjamin Pilgrim &lt;ben@pilgrim.me.uk&gt;
 */
public class TestCommand {
	
	@Command("test")
	@Desc("Test Command.")
	public CommandResult onTest(Context context, IUser user){		
		context.get(CommandSource.class).sendMessage(user.getName());
		return CommandResult.SUCCESS;
	}
	
	@Command("test")
	@Desc("Test Command.")
	public CommandResult onTest2(Context context, IChannel channel){
		context.get(CommandSource.class).sendMessage(channel.getName());
		return CommandResult.SUCCESS;
	}
	
}
