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
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import uk.me.pilgrim.dev.core.events.InitEvent;
import uk.me.pilgrim.dev.core.foundation.GuiceModule;
import uk.me.pilgrim.dev.core.util.Context;
import uk.me.pilgrim.dev.discordBot.models.Channel;
import uk.me.pilgrim.dev.discordBot.models.Guild;
import uk.me.pilgrim.dev.discordBot.models.User;


/**
 * @author Benjamin Pilgrim &lt;ben@pilgrim.me.uk&gt;
 */
public class DiscordEvents extends GuiceModule{
	
	@Inject
	IDiscordClient client;
	
	@Inject
	EventBus events;
	
	@Inject
	User.Registry userRegistry;
	
	@Inject
	Guild.Registry guildRegistry;
	
	@Inject
	Channel.Registry channelRegistry;
	
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
		IMessage message = event.getMessage();
		IUser author = message.getAuthor();
		IGuild guild = message.getGuild();
		IChannel channel = message.getChannel();
		
		Context context = new Context();
		context.put(IDiscordClient.class, client);
		context.put(IUser.class, author);
		context.put(User.class, userRegistry.get(author));
		if (!channel.isPrivate()){
			context.put(IGuild.class, guild);
			context.put(Guild.class, guildRegistry.get(guild));
		}
		context.put(IChannel.class, channel);
		context.put(Channel.class, channelRegistry.get(channel));
		context.put(IMessage.class, message);
		
		uk.me.pilgrim.dev.discordBot.events.MessageReceivedEvent e = new uk.me.pilgrim.dev.discordBot.events.MessageReceivedEvent(context);
		events.post(e);
	}
}
