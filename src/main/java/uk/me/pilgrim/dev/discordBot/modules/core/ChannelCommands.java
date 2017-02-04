/**
 * This file is part of DiscordBot.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */
package uk.me.pilgrim.dev.discordBot.modules.core;

import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;
import uk.me.pilgrim.dev.core.commands.CommandResult;
import uk.me.pilgrim.dev.core.commands.annotations.Command;
import uk.me.pilgrim.dev.core.commands.annotations.Perm;
import uk.me.pilgrim.dev.core.util.Context;
import uk.me.pilgrim.dev.discordBot.models.Channel;
import uk.me.pilgrim.dev.discordBot.models.Guild;

/**
 * @author Benjamin Pilgrim &lt;ben@pilgrim.me.uk&gt;
 */
public class ChannelCommands {
	
	@Command("setlogchannel")
	@Perm("admin")
	public CommandResult onSetLogChannel(Context context, Channel channel) throws RateLimitException, DiscordException, MissingPermissionsException{
		context.get(Guild.class).setLogChannel(channel);
		channel.save();
		context.get(Channel.class).info("Set log channel to " + channel.mention() + ".");
		return CommandResult.SUCCESS;
	}
}
