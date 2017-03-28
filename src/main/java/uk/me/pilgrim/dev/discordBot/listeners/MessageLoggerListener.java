/**
 * This file is part of DiscordBot.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */
package uk.me.pilgrim.dev.discordBot.listeners;

import com.google.common.eventbus.Subscribe;

import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import uk.me.pilgrim.dev.core.commands.exceptions.CommandException;
import uk.me.pilgrim.dev.core.util.logger.TerraLogger;
import uk.me.pilgrim.dev.core.util.text.ConsoleColor;
import uk.me.pilgrim.dev.discordBot.events.MessageReceivedEvent;

/**
 * @author Benjamin Pilgrim &lt;ben@pilgrim.me.uk&gt;
 */
public class MessageLoggerListener {
	
	@Subscribe
	public void onMessageReceivedEvent(MessageReceivedEvent event) throws CommandException{
		IMessage message = event.getMessage();
		IUser author = event.getAuthor().getDiscordUser();
		IChannel channel = event.getChannel().getDiscordChannel();
		String text = event.getContent();

		if (author.isBot()) return;
		
		for (IUser user : message.getMentions()){
			if (user == null) continue;
			String displayName = message.getChannel().isPrivate() ? user.getDisplayName(message.getGuild()) : user.getName();
			
			text = text.replace("<@!" + user.getID() + ">", ConsoleColor.YELLOW  + "@" + displayName + "<r>");
			text = text.replace("<@" + user.getID() + ">", ConsoleColor.YELLOW  + "@" + displayName + "<r>");
		}
		
		if (channel.isPrivate()){
			TerraLogger.info("<<l>PM<r>><<l>"+ channel.getName() +"<r>> " + text.replaceAll("%", "%%"));
		} else {
			IGuild guild = event.getGuild().getDiscordGuild();	
			TerraLogger.info("<<l>"+ guild.getName() +"<r>><<l>#"+ channel.getName() +"<r>><<l>" + author.getDisplayName(guild) + "<r>> " + text.replaceAll("%", "%%"));
		}
	}
}
