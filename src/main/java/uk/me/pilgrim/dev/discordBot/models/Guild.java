package uk.me.pilgrim.dev.discordBot.models;

import java.util.HashMap;
import java.util.List;

import javax.inject.Singleton;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

import ninja.leaping.configurate.objectmapping.Setting;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import uk.me.pilgrim.dev.core.config.Config;

public class Guild extends Config {

	@Inject
	protected Channel.Factory channelFactory;
	
	protected final IGuild guild;

	@Setting
	List<String> blacklist;
	
	@Setting
	HashMap<String, Channel> channels;
	
	@Inject
	public Guild(@Assisted IGuild guild){
		super("data/servers", guild.getID() + ".conf");
		this.guild = guild;
	}
	
	@Override
	public void setDefaults() {
		blacklist = setDefault(blacklist, Lists.newArrayList());
		channels = setDefault(channels, new HashMap<>());
	}
	
	public Channel getChannel(IChannel channel){ 
		if (!channels.containsKey(channel.getID())){
			channels.put(channel.getID(), channelFactory.create(channel));
		}
		
		return channels.get(channel.getID());
	}
	
	public IGuild getDiscordGuild(){
		return guild;
	}
	
	public List<String> getBlacklist(){
		return blacklist;
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
				guilds.put(guild.getID(), factory.create(guild));
			}
			
			return guilds.get(guild.getID());
		}
	}
}
