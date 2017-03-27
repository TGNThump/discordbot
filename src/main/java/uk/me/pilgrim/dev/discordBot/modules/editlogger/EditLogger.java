/**
 * This file is part of DiscordBot.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */
package uk.me.pilgrim.dev.discordBot.modules.editlogger;

import com.google.common.eventbus.Subscribe;

import uk.me.pilgrim.dev.core.events.InitEvent;
import uk.me.pilgrim.dev.core.foundation.Module;

/**
 * @author Benjamin Pilgrim &lt;ben@pilgrim.me.uk&gt;
 */
public class EditLogger extends Module{
	
	@Subscribe
	public void onInit(InitEvent event){
		registerEvents(new EditListener());
	}
}