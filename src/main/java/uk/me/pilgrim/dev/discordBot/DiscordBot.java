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
import uk.me.pilgrim.dev.core.commands.CommandService;
import uk.me.pilgrim.dev.core.events.InitEvent;
import uk.me.pilgrim.dev.core.events.PreInitEvent;
import uk.me.pilgrim.dev.core.foundation.Project;
import uk.me.pilgrim.dev.discordBot.commands.arguments.IChannelArgument;
import uk.me.pilgrim.dev.discordBot.commands.arguments.IMessageArgument;
import uk.me.pilgrim.dev.discordBot.commands.arguments.IRoleArgument;
import uk.me.pilgrim.dev.discordBot.commands.arguments.IUserArgument;
import uk.me.pilgrim.dev.discordBot.config.Lang;
import uk.me.pilgrim.dev.discordBot.config.MainConfig;
import uk.me.pilgrim.dev.discordBot.listeners.CommandListener;
import uk.me.pilgrim.dev.discordBot.listeners.ExceptionListener;
import uk.me.pilgrim.dev.discordBot.listeners.MessageLoggerListener;
import uk.me.pilgrim.dev.discordBot.listeners.ReadyListener;
import uk.me.pilgrim.dev.discordBot.models.Models;
import uk.me.pilgrim.dev.discordBot.modules.assignableroles.AssignableRoles;
import uk.me.pilgrim.dev.discordBot.modules.core.BotCore;
import uk.me.pilgrim.dev.discordBot.modules.editlogger.EditLogger;
import uk.me.pilgrim.dev.discordBot.modules.filter.MessageFilter;
import uk.me.pilgrim.dev.discordBot.modules.linkaccount.LinkAccount;
import uk.me.pilgrim.dev.discordBot.modules.moderation.Moderation;
import uk.me.pilgrim.dev.discordBot.modules.test.TestModule;

/**
 * @author Benjamin Pilgrim &lt;ben@pilgrim.me.uk&gt;
 */
public class DiscordBot extends Project {
	
	private MainConfig config;
	private Lang lang;
	private IDiscordClient client;
		
	@Inject
	private CommandService commands;
	
	@Inject
	private EventBus events;
	
	public DiscordBot() {
		super(PomData.GROUP_ID, PomData.ARTIFACT_ID, PomData.NAME, PomData.VERSION);
		config = new MainConfig();
		lang = new Lang();
		System.setProperty(org.slf4j.impl.SimpleLogger.DEFAULT_LOG_LEVEL_KEY, "WARN");
	}
	
	public void registerEvents(){
		events.register(new ReadyListener());
		events.register(new ExceptionListener());
		events.register(new MessageLoggerListener());
		events.register(new CommandListener());
	}
	
	public void addCommandArgsParsers(){
		commands.addArgumentParser(new IUserArgument());
		commands.addArgumentParser(new IChannelArgument());
		commands.addArgumentParser(new IMessageArgument());
		commands.addArgumentParser(new IRoleArgument());
	}

	@Subscribe
	public void onPreInit(PreInitEvent event){
		addCommandArgsParsers();
	}
	
	@Subscribe
	public void onInit(InitEvent event){
		registerEvents();
	}
	
	@Override
	protected void configure() {
		try {
			client = new ClientBuilder().withToken(config.apiKey).login();
			bind(IDiscordClient.class).toInstance(client);
			bind(MainConfig.class).toInstance(config);
			bind(Lang.class).toInstance(lang);
		} catch (DiscordException e) {
			e.printStackTrace();
		}
		
		registerChild(new Models());
		registerChild(new DiscordEvents());
		registerChild(new BotCore());
		registerChild(new AssignableRoles());
		registerChild(new MessageFilter());
		registerChild(new TestModule());
		registerChild(new EditLogger());
		registerChild(new Moderation());
		registerChild(new LinkAccount());
	}
}