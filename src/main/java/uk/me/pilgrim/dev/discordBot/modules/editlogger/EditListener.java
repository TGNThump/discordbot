/**
 * This file is part of DiscordBot.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */
package uk.me.pilgrim.dev.discordBot.modules.editlogger;

import java.util.Optional;

import com.google.common.eventbus.Subscribe;

import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;
import uk.me.pilgrim.dev.discordBot.events.MessageEditedEvent;
import uk.me.pilgrim.dev.discordBot.models.Channel;

/**
 * @author Benjamin Pilgrim &lt;ben@pilgrim.me.uk&gt;
 */
public class EditListener {
	
	@Subscribe
	public void onMesssageEditEvent(MessageEditedEvent event) throws RateLimitException, DiscordException, MissingPermissionsException{
		if (event.getAuthor().getDiscordUser().isBot()) return;
		if (event.isPrivate()) return;
		Optional<Channel> log = event.getGuild().getLogChannel();
		if (!log.isPresent()) return;
		if (!event.getChannel().isLoggingEdits()) return;
	
		IMessage newM = event.getNew();
		IMessage oldM = event.getOld();
		
		String message = String.format("Original message by **%s** in **%s**: ```%s``` Edit: ```%s```", event.getAuthor().mention(), oldM.getChannel().mention(), oldM.getContent(), newM.getContent());
		
		log.get().info(message);
	}
}
