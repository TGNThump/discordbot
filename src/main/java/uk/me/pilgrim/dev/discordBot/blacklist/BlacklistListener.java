/**
 * This file is part of DiscordBot.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */
package uk.me.pilgrim.dev.discordBot.blacklist;

import java.util.List;

import javax.inject.Inject;

import com.google.common.collect.Lists;
import com.google.common.eventbus.Subscribe;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;
import uk.me.pilgrim.dev.core.Core;
import uk.me.pilgrim.dev.core.util.text.Text;
import uk.me.pilgrim.dev.discordBot.config.Lang;
import uk.me.pilgrim.dev.discordBot.events.MessageBlacklistedEvent;
import uk.me.pilgrim.dev.discordBot.models.Guild;

/**
 * @author Benjamin Pilgrim &lt;ben@pilgrim.me.uk&gt;
 */
public class BlacklistListener {
	
	@Inject
	Lang lang;
	
	@Inject
	IDiscordClient client;
	
	@Inject
	Guild.Registry guildRegistry;

	
	@Subscribe
	public void onBlacklistMessageEvent(MessageBlacklistedEvent event){
		try {	
			String words = "`" + Text.implodeCommaAnd(event.getWords(), "`, `","` and `") + "`";
			String response = String.format(lang.blacklist_message_deleted, event.getChannel().getName(), event.getWords().size() > 1 ? "s" : "", words, event.getMessage().getContent());
			
			EmbedObject embed = new EmbedBuilder()
					.withColor(137, 204, 240)
					.withDescription(response)
					.build();
			
			event.getMessage().delete();
			event.getAuthor().getOrCreatePMChannel().sendMessage("", embed, true);
		} catch (MissingPermissionsException | RateLimitException | DiscordException e) {
			e.printStackTrace();
		}
	}
	
	@Subscribe
	public void onMessageReceivedEvent(MessageReceivedEvent event){
		try{
			IMessage message = event.getMessage();
			IUser author = message.getAuthor();
			IChannel channel = message.getChannel();
			String text = event.getMessage().getContent();
	
			if (author.isBot()) return;
			if (channel.isPrivate()) return;
			if (text.startsWith("<@" + client.getOurUser().getID() + ">") || text.startsWith("<@!" + client.getOurUser().getID() + ">") || channel.isPrivate()) return;
			
			List<String> caughtWords = Lists.newArrayList();
			
			Guild guild = guildRegistry.get(channel.getGuild());
			
			guild.getBlacklist().forEach((word) -> {
				String lower = text.toLowerCase();
				if (lower.startsWith(word + " ") || lower.contains(" " + word + " ") || lower.endsWith(" " + word) || lower.equals(word)){
					caughtWords.add(word);
				}
			});
			
			if (!caughtWords.isEmpty()){
				Core.Events.fire(new MessageBlacklistedEvent(message, caughtWords));
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
