/**
 * This file is part of DiscordBot.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */
package uk.me.pilgrim.dev.discordBot.blacklist;

import javax.inject.Inject;

import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;
import uk.me.pilgrim.dev.core.commands.CommandResult;
import uk.me.pilgrim.dev.core.commands.annotations.Command;
import uk.me.pilgrim.dev.core.util.Context;
import uk.me.pilgrim.dev.core.util.text.Text;
import uk.me.pilgrim.dev.discordBot.models.Guild;

/**
 * @author Benjamin Pilgrim &lt;ben@pilgrim.me.uk&gt;
 */
public class BlacklistCommands {
	
	@Inject
	Guild.Registry guildRegistry;
	
	@Command("blacklist")
	public CommandResult onBlacklist(Context context) throws MissingPermissionsException, RateLimitException, DiscordException{
		Guild guild = guildRegistry.get(context.get(IGuild.class));
		String message = "Blacklist: ";
		
		message += "`" + Text.implodeCommaAnd(guild.getBlacklist(), "`, `","` and `") + "`";
		
		context.get(IChannel.class).sendMessage(message);
		return CommandResult.SUCCESS;
	}
	
	@Command("blacklist add")
	public CommandResult onBlacklistAdd(Context context, String word) throws MissingPermissionsException, RateLimitException, DiscordException{
		Guild guild = guildRegistry.get(context.get(IGuild.class));
		guild.getBlacklist().add(word.toLowerCase());
		guild.save();
		context.get(IChannel.class).sendMessage("Added `" + word + "` to blacklist.");
		return CommandResult.SUCCESS;
	}
	
	@Command("blacklist remove")
	public CommandResult onBlacklistRemove(Context context, String word) throws MissingPermissionsException, RateLimitException, DiscordException{
		Guild guild = guildRegistry.get(context.get(IGuild.class));
		guild.getBlacklist().remove(word.toLowerCase());
		guild.save();
		context.get(IChannel.class).sendMessage("Removed `" + word + "` from blacklist.");
		return CommandResult.SUCCESS;
	}
	
}