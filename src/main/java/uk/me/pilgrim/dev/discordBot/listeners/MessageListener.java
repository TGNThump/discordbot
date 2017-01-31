/**
 * This file is part of DiscordBot.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */
package uk.me.pilgrim.dev.discordBot.listeners;

import com.google.common.eventbus.Subscribe;

import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import uk.me.pilgrim.dev.core.Core;
import uk.me.pilgrim.dev.core.commands.exceptions.CommandException;
import uk.me.pilgrim.dev.core.util.logger.TerraLogger;
import uk.me.pilgrim.dev.core.util.text.ConsoleColor;
import uk.me.pilgrim.dev.discordBot.events.MessageEvent;

/**
 * @author Benjamin Pilgrim &lt;ben@pilgrim.me.uk&gt;
 */
public class MessageListener {
	
	@Subscribe
	public void onMessageReceivedEvent(MessageReceivedEvent event) throws CommandException{
		Core.Events.fire(new MessageEvent(event.getMessage()));
		
		IMessage message = event.getMessage();
		IUser author = message.getAuthor();
		IGuild guild = message.getGuild();
		IChannel channel = message.getChannel();
		String text = event.getMessage().getContent();

		if (author.isBot()) return;
		
		for (IUser user : message.getMentions()){
			text = text.replace("<@!" + user.getID() + ">", ConsoleColor.YELLOW  + "@" + user.getDisplayName(message.getGuild()) + "<r>");
			text = text.replace("<@" + user.getID() + ">", ConsoleColor.YELLOW  + "@" + user.getDisplayName(message.getGuild()) + "<r>");
		}
		
		if (channel.isPrivate()){
			TerraLogger.clear("<l><PM><"+ channel.getName() +"><r> " + text.replaceAll("%", "%%"));
		} else {
			TerraLogger.clear("<l><"+ guild.getName() +"><#"+ channel.getName() +"><" + author.getNicknameForGuild(guild).orElse(author.getName()) + "><r> " + text.replaceAll("%", "%%"));
		}
	}
}
