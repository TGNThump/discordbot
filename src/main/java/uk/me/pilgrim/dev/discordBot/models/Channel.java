package uk.me.pilgrim.dev.discordBot.models;

import java.awt.Color;
import java.util.HashMap;

import javax.inject.Singleton;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

import ninja.leaping.configurate.objectmapping.Setting;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;
import uk.me.pilgrim.dev.core.config.Config;
import uk.me.pilgrim.dev.core.util.logger.TerraLogger;
import uk.me.pilgrim.dev.core.util.text.ConsoleColor;

public class Channel extends Config{
	
	@Inject
	IDiscordClient client;
	
	private IChannel channel;
	
	@Setting
	String name;
	
	
	public IChannel getDiscordChannel(){
		return channel;
	}
	
	public String getName() {
		return channel.getName();
	}
	
	public void sendMessage(String message, Color color) throws RateLimitException, DiscordException, MissingPermissionsException{
		boolean block = message.endsWith("```");
		if (message.length() > 1900){
			message = message.substring(0, 1900);
			if (block) message += "```";
		}

		EmbedObject embed = new EmbedBuilder()
			.withColor(color)
			.withDescription(message)
			.build();
		channel.sendMessage("", embed, false);

		IUser author = client.getOurUser();
		IGuild guild = channel.getGuild();
		
		for (IUser user : channel.getUsersHere()){
			message = message.replace("<@!" + user.getID() + ">", ConsoleColor.YELLOW  + "@" + user.getDisplayName(guild) + "<r>");
			message = message.replace("<@" + user.getID() + ">", ConsoleColor.YELLOW  + "@" + user.getDisplayName(guild) + "<r>");
		}
		
		if (channel.isPrivate()){
			TerraLogger.info("<<l>PM<r>><<l>"+ channel.getName() +"<r>> " + message.replaceAll("%", "%%"));
		} else {
			TerraLogger.info("<<l>"+ guild.getName() +"<r>><<l>#"+ channel.getName() +"<r>><<l>" + author.getNicknameForGuild(guild).orElse(author.getName()) + "<r>> " + message.replaceAll("%", "%%"));
		}
		
	}
	
	public void info(String message) throws RateLimitException, DiscordException, MissingPermissionsException{
		sendMessage(message, new Color(137, 204, 240));
	}
	
	public void error(String message) throws RateLimitException, DiscordException, MissingPermissionsException{
		sendMessage(message, new Color(237, 101, 104));
	}
	
	public Channel(){
		registerEvents();
	}
	
	@Inject
	public Channel(@Assisted IChannel channel){
		super("data/channels", channel.getID() + ".conf");
		this.channel = channel;
		this.name = channel.getName();
		registerEvents();
	}
	
	@Override
	public void setDefaults() {
		if (channel != null) name = channel.getName();
	}
	
	// Factory and Registry

	public interface Factory{
		Channel create(IChannel channel);
	}
	
	@Singleton
	public static class Registry{
		
		@Inject
		private Channel.Factory factory;
		
		private HashMap<String, Channel> channels;
		
		public Registry() {
			channels = new HashMap<>();
		}
		
		public Channel get(IChannel channel){ 
			if (!channels.containsKey(channel.getID())){
				Channel c = factory.create(channel);
				channels.put(channel.getID(), c);
				c.save();
			}
			
			return channels.get(channel.getID());
		}
	}

	public String mention() {
		return channel.mention();
	}
}
