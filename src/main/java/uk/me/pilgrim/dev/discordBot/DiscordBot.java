/**
 * This file is part of core.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */
package uk.me.pilgrim.dev.discordBot;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;

import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.util.DiscordException;
import uk.me.pilgrim.dev.core.commands.MethodCommandService;
import uk.me.pilgrim.dev.core.events.InitEvent;
import uk.me.pilgrim.dev.core.foundation.Project;
import uk.me.pilgrim.dev.discordBot.commands.TestCommand;
import uk.me.pilgrim.dev.discordBot.commands.arguments.IChannelArgument;
import uk.me.pilgrim.dev.discordBot.commands.arguments.IUserArgument;
import uk.me.pilgrim.dev.discordBot.config.MainConfig;
import uk.me.pilgrim.dev.discordBot.listeners.MessageListener;
import uk.me.pilgrim.dev.discordBot.listeners.ReadyListener;

/**
 * @author Benjamin Pilgrim &lt;ben@pilgrim.me.uk&gt;
 */
public class DiscordBot extends Project {
	
	private MainConfig config;
	private IDiscordClient client;
	
	@Inject
	private MethodCommandService commandService;
	
	@Inject
	private EventBus events;
	
	public DiscordBot() {
		super(PomData.GROUP_ID, PomData.ARTIFACT_ID, PomData.NAME, PomData.VERSION);
		config = new MainConfig();
	}
	
	@Subscribe
	public void onInit(InitEvent event){
		events.register(new ReadyListener());
		events.register(new MessageListener(commandService));
		commandService.addArgumentParser(new IUserArgument());
		commandService.addArgumentParser(new IChannelArgument());
		commandService.registerCommands(new TestCommand());
	}
	
	@Override
	protected void configure() {
		try {
			client = new ClientBuilder().withToken(config.apiKey).login();
			bind(IDiscordClient.class).toInstance(client);
		} catch (DiscordException e) {
			e.printStackTrace();
		}
		
		registerChild(new DiscordEvents());
	}
	
}