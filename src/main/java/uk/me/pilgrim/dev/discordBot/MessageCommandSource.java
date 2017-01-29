/**
 * This file is part of DiscordBot.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */
package uk.me.pilgrim.dev.discordBot;

import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;
import uk.me.pilgrim.dev.core.commands.sources.CommandSource;
import uk.me.pilgrim.dev.core.util.logger.TerraLogger;


/**
 * @author Benjamin Pilgrim &lt;ben@pilgrim.me.uk&gt;
 */
public class MessageCommandSource implements CommandSource {
	
	private MessageReceivedEvent event;
	
	public MessageCommandSource(MessageReceivedEvent event){
		this.event = event;
	}
	
	/* (non-Javadoc)
	 * @see uk.me.pilgrim.dev.core.commands.sources.CommandSource#hasPermission(java.lang.String)
	 */
	@Override
	public boolean hasPermission(String perm) {
		return true;
	}
	
	/* (non-Javadoc)
	 * @see uk.me.pilgrim.dev.core.commands.sources.CommandSource#sendMessage(java.lang.String)
	 */
	@Override
	public void sendMessage(String of) {
		try {
			event.getMessage().reply("```" + of + "```");
			TerraLogger.info(of);
		} catch (MissingPermissionsException | RateLimitException | DiscordException e) {
			e.printStackTrace();
		}
	}
	
}
