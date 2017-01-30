/**
 * This file is part of DiscordBot.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */
package uk.me.pilgrim.dev.discordBot.listeners;

import com.google.common.eventbus.Subscribe;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;
import uk.me.pilgrim.dev.core.commands.MethodCommandService;
import uk.me.pilgrim.dev.core.commands.exceptions.CommandException;
import uk.me.pilgrim.dev.core.commands.sources.CommandSource;
import uk.me.pilgrim.dev.core.util.Context;
import uk.me.pilgrim.dev.discordBot.commands.source.MessageCommandSource;

/**
 * @author Benjamin Pilgrim &lt;ben@pilgrim.me.uk&gt;
 */
public class CommandListener {
	
	MethodCommandService commandService;
	
	public CommandListener(MethodCommandService commandService){
		this.commandService = commandService;
	}
	
	@Subscribe
	public void onMessageReceivedEvent(MessageReceivedEvent event) throws CommandException{
		try{
			IDiscordClient client = event.getClient();
			IMessage message = event.getMessage();
			IUser author = message.getAuthor();
			IGuild guild = message.getGuild();
			IChannel channel = message.getChannel();
			String text = event.getMessage().getContent();
	
			if (author.isBot()) return;
	
			text = event.getMessage().getContent();
			
			if (text.startsWith("<@" + client.getOurUser().getID() + ">") || text.startsWith("<@!" + client.getOurUser().getID() + ">") || channel.isPrivate()){
				if (!channel.isPrivate()) text = text.substring(text.indexOf(">")+1);
				while(text.startsWith(" ")) text = text.substring(1);
					
				Context context = new Context();
				context.put(CommandSource.class, new MessageCommandSource(event));
				context.put(IDiscordClient.class, client);
				context.put(IUser.class, author);
				if (!channel.isPrivate()) context.put(IGuild.class, guild);
				context.put(IChannel.class, channel);
				context.put(IMessage.class, message);
				
				commandService.processCommand(context, text);
			}
		} catch (Exception e) {
			try {
				String reply = "";
				StackTraceElement[] stackTrace = e.getStackTrace();
				reply += e;
				reply += "\n";
				for (StackTraceElement s : stackTrace){
					reply += " at " + s;
					
					reply += "\n";
				}
				
				reply = reply.substring(0, 1900);
				
				event.getMessage().reply("```" + reply + "```");
			} catch (MissingPermissionsException | RateLimitException | DiscordException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
	}
	
}
