/**
 * This file is part of DiscordBot.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */
package uk.me.pilgrim.dev.discordBot.commands.source;

import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;
import uk.me.pilgrim.dev.core.commands.sources.CommandSource;
import uk.me.pilgrim.dev.core.util.logger.TerraLogger;
import uk.me.pilgrim.dev.discordBot.events.MessageEvent;


/**
 * @author Benjamin Pilgrim &lt;ben@pilgrim.me.uk&gt;
 */
public class MessageCommandSource implements CommandSource {
	
	private MessageEvent event;
	
	public MessageCommandSource(MessageEvent event){
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
			boolean block = of.endsWith("```");
			if (of.length() > 1900)
				of = of.substring(0, 1900);
			if (block) of += "```";

			EmbedObject embed = new EmbedBuilder()
				.withColor(137, 204, 240)
				.withDescription(of)
				.build();
			event.getMessage().getChannel().sendMessage("", embed, true);

			TerraLogger.info(of);
		} catch (MissingPermissionsException | RateLimitException | DiscordException e) {
			e.printStackTrace();
		}
	}
	
}
