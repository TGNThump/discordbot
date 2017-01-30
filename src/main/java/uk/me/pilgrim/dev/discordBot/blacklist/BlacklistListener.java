/**
 * This file is part of DiscordBot.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */
package uk.me.pilgrim.dev.discordBot.blacklist;

import java.util.List;

import com.google.common.collect.Lists;
import com.google.common.eventbus.Subscribe;

import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;
import uk.me.pilgrim.dev.core.Core;
import uk.me.pilgrim.dev.core.util.text.Text;
import uk.me.pilgrim.dev.discordBot.config.Lang;
import uk.me.pilgrim.dev.discordBot.config.MainConfig;

/**
 * @author Benjamin Pilgrim &lt;ben@pilgrim.me.uk&gt;
 */
public class BlacklistListener {
	
	@Subscribe
	public void onMessageReceivedEvent(MessageReceivedEvent event){
		try{
			IMessage message = event.getMessage();
			IUser author = message.getAuthor();
			IChannel channel = message.getChannel();
			String text = event.getMessage().getContent();
	
			if (author.isBot()) return;
			if (channel.isPrivate()) return;
			
			List<String> caughtWords = Lists.newArrayList();
			
			Lang lang = Core.get(Lang.class);
			
			Core.get(MainConfig.class).wordBlacklist.forEach((word) -> {
				String lower = text.toLowerCase();
				if (lower.startsWith(word + " ") || lower.contains(" " + word + " ") || lower.endsWith(" " + word) || lower.equals(word)){
					caughtWords.add(word);
				}
			});
			
			if (!caughtWords.isEmpty()){
				try {
					message.delete();
					String words = "`" + Text.implodeCommaAnd(caughtWords, "`, `","` and `") + "`";
					String response = String.format(lang.blacklist_message_deleted, channel.getName(), caughtWords.size() > 1 ? "s" : "", words, message);
					
					author.getOrCreatePMChannel().sendMessage(response);
				} catch (MissingPermissionsException | RateLimitException | DiscordException e) {
					e.printStackTrace();
				}
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
