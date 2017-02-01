package uk.me.pilgrim.dev.discordBot.models;

import java.util.HashMap;
import java.util.List;

import javax.inject.Singleton;

import com.google.common.collect.Lists;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

import ninja.leaping.configurate.objectmapping.Setting;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import uk.me.pilgrim.dev.core.config.Config;
import uk.me.pilgrim.dev.core.events.ConfigurationReloadEvent;

public class Guild extends Config {
	
	@Inject
	protected Channel.Factory channelFactory;
	
	protected IGuild guild;

	@Setting
	List<String> blacklist;
	
	@Setting
	HashMap<String, Channel> channels;
	
	public Guild(){

	}
	
	@Inject
	public Guild(@Assisted IGuild guild, EventBus events){
		super("data/servers", guild.getID() + ".conf");
		this.guild = guild;
		events.register(this);
	}
	
	@Override
	public void setDefaults() {
		blacklist = setDefault(blacklist, Lists.newArrayList());
		channels = setDefault(channels, new HashMap<>());
	}
	
	public Channel getChannel(IChannel channel){ 
		if (!channels.containsKey(channel.getID())){
			channels.put(channel.getID(), channelFactory.create(channel));
			save();
		}
		
		return channels.get(channel.getID());
	}
	
	public IGuild getDiscordGuild(){
		return guild;
	}
	
	public List<String> getBlacklist(){
		return blacklist;
	}
	
	@Subscribe
	public void onConfigReload(ConfigurationReloadEvent event){
		reload();
	}
	
	// Factory and Registry
	
	public interface Factory{
		Guild create(IGuild guild);
	}
	
	@Singleton
	public static class Registry{
		
		@Inject
		private Guild.Factory factory;
		
		private HashMap<String, Guild> guilds;
		
		public Registry() {
			guilds = new HashMap<>();
		}
		
		public Guild get(IGuild guild){ 
			if (!guilds.containsKey(guild.getID())){
				Guild g = factory.create(guild);
				guilds.put(guild.getID(), g);
				g.save();
			}
			
			return guilds.get(guild.getID());
		}
	}
}
