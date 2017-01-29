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
import uk.me.pilgrim.dev.core.commands.sources.CommandSource;
import uk.me.pilgrim.dev.core.util.Context;
import uk.me.pilgrim.dev.core.util.logger.TerraLogger;
import uk.me.pilgrim.dev.core.util.text.ConsoleColor;
import uk.me.pilgrim.dev.discordBot.MessageCommandSource;

/**
 * @author Benjamin Pilgrim &lt;ben@pilgrim.me.uk&gt;
 */
public class MessageListener {
	
	MethodCommandService commandService;
	
	public MessageListener(MethodCommandService commandService){
		this.commandService = commandService;
	}
	
	@Subscribe
	public void onMessageReceivedEvent(MessageReceivedEvent event){
		IDiscordClient client = event.getClient();
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
		
		TerraLogger.clear("<l><"+ guild.getName() +"><#"+ channel.getName() +"><" + author.getNicknameForGuild(guild).orElse(author.getName()) + "><r> " + text.replaceAll("%", "%%"));

		text = event.getMessage().getContent();
		
		if (text.startsWith("<@" + client.getOurUser().getID() + ">") || text.startsWith("<@!" + client.getOurUser().getID() + ">")){
			text = text.substring(text.indexOf(">")+1);
			while(text.startsWith(" ")) text = text.substring(1);
			
			Context context = new Context();
			context.put(CommandSource.class, new MessageCommandSource(event));
			context.put(IDiscordClient.class, client);
			context.put(IUser.class, author);
			context.put(IGuild.class, guild);
			context.put(IChannel.class, channel);
			context.put(IMessage.class, message);
			
			try {
				commandService.processCommand(context, text);
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
					
					message.reply("```" + reply + "```");
				} catch (MissingPermissionsException | RateLimitException | DiscordException e1) {
					e1.printStackTrace();
				}
				e.printStackTrace();
			}
		}		
	}
	
}
