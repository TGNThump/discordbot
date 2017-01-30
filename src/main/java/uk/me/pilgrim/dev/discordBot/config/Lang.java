/**
 * This file is part of DiscordBot.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */
package uk.me.pilgrim.dev.discordBot.config;

import ninja.leaping.configurate.objectmapping.Setting;
import uk.me.pilgrim.dev.core.config.Config;

/**
 * @author Benjamin Pilgrim &lt;ben@pilgrim.me.uk&gt;
 */
public class Lang extends Config {

	public Lang(){
		super("lang.conf");
	}
	
	@Override
	public void setDefaults() {
		blacklist_message_deleted = setDefault(blacklist_message_deleted, "Your message in **#%s** was deleted because it contained the banned word%s %s. \n```%s```");
	}
	
	@Setting
	public String blacklist_message_deleted;
	
}
