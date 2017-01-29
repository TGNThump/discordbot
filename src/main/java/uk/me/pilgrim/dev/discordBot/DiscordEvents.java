/**
 * This file is part of coreTest.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */
package uk.me.pilgrim.dev.discordBot;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventDispatcher;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import uk.me.pilgrim.dev.core.events.InitEvent;
import uk.me.pilgrim.dev.core.foundation.GuiceModule;


/**
 * @author Benjamin Pilgrim &lt;ben@pilgrim.me.uk&gt;
 */
public class DiscordEvents extends GuiceModule{
	
	@Inject
	IDiscordClient client;
	
	@Inject
	EventBus events;
	
	@Subscribe
	public void onInit(InitEvent event){
		EventDispatcher discordEvents = client.getDispatcher();
		discordEvents.registerListener(this);
	}
	
	@EventSubscriber
	public void onReady(ReadyEvent event){
		events.post(event);
	}
	
	@EventSubscriber
	public void onMessageReceivedEvent(MessageReceivedEvent event){
		events.post(event);
	}


	
}
