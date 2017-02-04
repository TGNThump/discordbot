/**
 * This file is part of DiscordBot.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */
package uk.me.pilgrim.dev.discordBot.models;

import com.google.inject.assistedinject.FactoryModuleBuilder;

import uk.me.pilgrim.dev.core.foundation.GuiceModule;

/**
 * @author Benjamin Pilgrim &lt;ben@pilgrim.me.uk&gt;
 */
public class Models extends GuiceModule{
	
	@Override
	protected void configure() {
		install(new FactoryModuleBuilder().build(Guild.Factory.class));
		install(new FactoryModuleBuilder().build(Channel.Factory.class));
		install(new FactoryModuleBuilder().build(User.Factory.class));
	}
}
