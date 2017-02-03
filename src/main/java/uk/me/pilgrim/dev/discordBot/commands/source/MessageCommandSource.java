/**
 * This file is part of DiscordBot.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */
package uk.me.pilgrim.dev.discordBot.commands.source;

import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;
import uk.me.pilgrim.dev.core.commands.sources.CommandSource;
import uk.me.pilgrim.dev.core.util.Context;
import uk.me.pilgrim.dev.discordBot.models.Channel;
import uk.me.pilgrim.dev.discordBot.models.User;


/**
 * @author Benjamin Pilgrim &lt;ben@pilgrim.me.uk&gt;
 */
public class MessageCommandSource implements CommandSource {
	
	private Context context;
	
	public MessageCommandSource(Context context){
		this.context = context;
	}
	
	/* (non-Javadoc)
	 * @see uk.me.pilgrim.dev.core.commands.sources.CommandSource#hasPermission(java.lang.String)
	 */
	@Override
	public boolean hasPermission(String perm) {
		return context.get(User.class).hasPermission(perm);
	}
	
	/* (non-Javadoc)
	 * @see uk.me.pilgrim.dev.core.commands.sources.CommandSource#sendMessage(java.lang.String)
	 */
	@Override
	public void sendMessage(String content) {
		try {
			context.get(Channel.class).info(content);
		} catch (RateLimitException | DiscordException | MissingPermissionsException e) {
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see uk.me.pilgrim.dev.core.commands.sources.CommandSource#info(java.lang.String)
	 */
	@Override
	public void info(String content) {
		try {
			context.get(Channel.class).info(content);
		} catch (RateLimitException | DiscordException | MissingPermissionsException e) {
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see uk.me.pilgrim.dev.core.commands.sources.CommandSource#error(java.lang.String)
	 */
	@Override
	public void error(String content) {
		try {
			context.get(Channel.class).error(content);
		} catch (RateLimitException | DiscordException | MissingPermissionsException e) {
			e.printStackTrace();
		}
	}
	
}
