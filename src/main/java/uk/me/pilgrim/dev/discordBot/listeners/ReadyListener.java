/**
 * This file is part of DiscordBot.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */
package uk.me.pilgrim.dev.discordBot.listeners;

import com.google.common.eventbus.Subscribe;

import sx.blah.discord.handle.impl.events.ReadyEvent;
import uk.me.pilgrim.dev.core.util.logger.TerraLogger;

/**
 * @author Benjamin Pilgrim &lt;ben@pilgrim.me.uk&gt;
 */
public class ReadyListener {
	
	@Subscribe
	public void onReadyEvent(ReadyEvent event){
		
		event.getClient().changePresence(false);
		TerraLogger.info("Connected to " + event.getClient().getGuilds().size() + " Guild.");
		event.getClient().getGuilds().forEach(guild -> {
			TerraLogger.info(" - " + guild.getName());
		});
	}
	
}
