package uk.me.pilgrim.dev.discordBot.models;

import java.util.HashMap;
import java.util.List;

import javax.inject.Singleton;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

import ninja.leaping.configurate.objectmapping.Setting;
import sx.blah.discord.handle.obj.IGuild;
import uk.me.pilgrim.dev.core.config.Config;

public class Guild extends Config {
	
//	@Inject
//	protected Channel.Registry channelRegistry;
	
	protected IGuild guild;

	@Setting
	String name;
	
	@Setting
	List<String> blacklist;
	
	@Setting
	List<String> assignableRoles;
	
	public IGuild getDiscordGuild(){
		return guild;
	}
	
	public List<String> getBlacklist(){
		return blacklist;
	}
	
	public List<String> getAssignableRoles(){
		return assignableRoles;
	}
	
	public Guild(){
		registerEvents();
	}
	
	@Inject
	public Guild(@Assisted IGuild guild){
		super("data/servers", guild.getID() + ".conf");
		this.guild = guild;
		this.name = guild.getName();
		registerEvents();
	}
	
	@Override
	public void setDefaults() {
		if (guild != null) name = guild.getName();
		blacklist = setDefault(blacklist, Lists.newArrayList());
		assignableRoles = setDefault(assignableRoles, Lists.newArrayList());
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
