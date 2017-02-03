/**
 * This file is part of DiscordBot.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */
package uk.me.pilgrim.dev.discordBot.listeners;

import javax.inject.Inject;

import com.google.common.eventbus.Subscribe;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IUser;
import uk.me.pilgrim.dev.core.commands.CommandService;
import uk.me.pilgrim.dev.core.commands.sources.CommandSource;
import uk.me.pilgrim.dev.discordBot.commands.source.MessageCommandSource;
import uk.me.pilgrim.dev.discordBot.events.MessageReceivedEvent;

/**
 * @author Benjamin Pilgrim &lt;ben@pilgrim.me.uk&gt;
 */
public class CommandListener {
	
	@Inject
	IDiscordClient client;
	
	@Inject
	CommandService commandService;
	
	@Subscribe
	public void onMessageReceivedEvent(MessageReceivedEvent event) throws Throwable{
		IUser author = event.getAuthor().getDiscordUser();
		IChannel channel = event.getChannel().getDiscordChannel();
		String text = event.getContent();

		if (author.isBot()) return;
		
		if (text.startsWith("<@" + client.getOurUser().getID() + ">") || text.startsWith("<@!" + client.getOurUser().getID() + ">") || channel.isPrivate()){
			if (!channel.isPrivate()) text = text.substring(text.indexOf(">")+1);
			while(text.startsWith(" ")) text = text.substring(1);
			
			event.getContext().put(CommandSource.class, new MessageCommandSource(event.getContext()));
			
			commandService.processCommand(event.getContext(), text);
		}
	}
	
}
