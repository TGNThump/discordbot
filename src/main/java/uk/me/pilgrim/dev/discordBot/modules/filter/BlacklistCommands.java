/**
 * This file is part of DiscordBot.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */
package uk.me.pilgrim.dev.discordBot.modules.filter;

import javax.inject.Inject;

import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;
import uk.me.pilgrim.dev.core.commands.CommandResult;
import uk.me.pilgrim.dev.core.commands.annotations.Command;
import uk.me.pilgrim.dev.core.commands.annotations.Perm;
import uk.me.pilgrim.dev.core.util.Context;
import uk.me.pilgrim.dev.core.util.text.Text;
import uk.me.pilgrim.dev.discordBot.models.Channel;
import uk.me.pilgrim.dev.discordBot.models.Guild;
import uk.me.pilgrim.dev.discordBot.models.User;

/**
 * @author Benjamin Pilgrim &lt;ben@pilgrim.me.uk&gt;
 */
public class BlacklistCommands {
	
	@Inject
	Guild.Registry guildRegistry;
	
	@Command("blacklist")
	public CommandResult onBlacklist(Context context) throws MissingPermissionsException, RateLimitException, DiscordException{
		Guild guild = context.get(Guild.class);
		String message = "Blacklist: ";
		
		message += "`" + Text.implodeCommaAnd(guild.getBlacklist(), "`, `","` and `") + "`";
		
		if (guild.getBlacklist().isEmpty()){
			message = "The blacklist is currently empty.";
		}
		
		context.get(User.class).getPMChannel().info(message);
		context.get(Channel.class).info("The blacklist has been private messaged to you.");
		return CommandResult.SUCCESS;
	}
	
	@Command("blacklist add")
	@Perm("MANAGE_MESSAGES")
	public CommandResult onBlacklistAdd(Context context, String word) throws MissingPermissionsException, RateLimitException, DiscordException{
		Guild guild = context.get(Guild.class);
		guild.getBlacklist().add(word.toLowerCase());
		guild.save();
		context.get(Channel.class).info("Added `" + word + "` to blacklist.");
		return CommandResult.SUCCESS;
	}
	
	@Command("blacklist remove")
	@Perm("MANAGE_MESSAGES")
	public CommandResult onBlacklistRemove(Context context, String word) throws MissingPermissionsException, RateLimitException, DiscordException{
		Guild guild = context.get(Guild.class);
		guild.getBlacklist().remove(word.toLowerCase());
		guild.save();
		context.get(Channel.class).info("Removed `" + word + "` from blacklist.");
		return CommandResult.SUCCESS;
	}
	
}