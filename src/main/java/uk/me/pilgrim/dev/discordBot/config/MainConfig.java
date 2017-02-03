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
public class MainConfig extends Config{

	public MainConfig(){
		super("config.conf");
	}
	
	/* (non-Javadoc)
	 * @see uk.me.pilgrim.dev.core.config.Config#setDefaults()
	 */
	@Override
	public void setDefaults() {
		apiKey = setDefault(apiKey, "");
		adminId = setDefault(adminId, "");
	}
	
	@Setting
	public String apiKey;
	
	@Setting
	public String adminId;
	
}
