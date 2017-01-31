/**
 * This file is part of DiscordBot.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */
package uk.me.pilgrim.dev.discordBot.blacklist;

import javax.inject.Inject;

import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;
import uk.me.pilgrim.dev.core.commands.CommandResult;
import uk.me.pilgrim.dev.core.commands.annotations.Command;
import uk.me.pilgrim.dev.core.util.Context;
import uk.me.pilgrim.dev.core.util.text.Text;
import uk.me.pilgrim.dev.discordBot.config.MainConfig;

/**
 * @author Benjamin Pilgrim &lt;ben@pilgrim.me.uk&gt;
 */
public class BlacklistCommands {
	
	@Inject
	private MainConfig config;
	
	@Command("blacklist")
	public CommandResult onBlacklist(Context context) throws MissingPermissionsException, RateLimitException, DiscordException{
		String message = "Blacklist: ";
		
		message += "`" + Text.implodeCommaAnd(config.wordBlacklist, "`, `","` and `") + "`";
		
		context.get(IChannel.class).sendMessage(message);
		return CommandResult.SUCCESS;
	}
	
	@Command("blacklist add")
	public CommandResult onBlacklistAdd(Context context, String word) throws MissingPermissionsException, RateLimitException, DiscordException{
		config.wordBlacklist.add(word.toLowerCase());
		config.save();
		context.get(IChannel.class).sendMessage("Added `" + word + "` to blacklist.");
		return CommandResult.SUCCESS;
	}
	
	@Command("blacklist remove")
	public CommandResult onBlacklistRemove(Context context, String word) throws MissingPermissionsException, RateLimitException, DiscordException{
		config.wordBlacklist.remove(word.toLowerCase());
		config.save();
		context.get(IChannel.class).sendMessage("Removed `" + word + "` from blacklist.");
		return CommandResult.SUCCESS;
	}
	
}
