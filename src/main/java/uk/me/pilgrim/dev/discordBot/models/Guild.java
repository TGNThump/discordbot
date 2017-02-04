package uk.me.pilgrim.dev.discordBot.models;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import javax.inject.Singleton;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

import ninja.leaping.configurate.objectmapping.Setting;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.Permissions;
import uk.me.pilgrim.dev.core.config.Config;

public class Guild extends Config {
	
	@Inject
	IDiscordClient client;
	
	@Inject
	Channel.Registry channelRegistry;
	
	protected IGuild guild;

	@Setting
	String name;
	
	@Setting
	String logChannel;
	
	@Setting
	List<String> blacklist;
	
	@Setting
	List<String> assignableRoles;
	
	@Setting
	List<String> admins;
	
	@Setting
	List<String> mods;
	
	
	public IGuild getDiscordGuild(){
		return guild;
	}
	
	public Optional<Channel> getLogChannel(){
		IChannel channel = client.getChannelByID(logChannel);
		if (channel != null)
			return Optional.of(channelRegistry.get(channel));
		return Optional.empty();
	}
	
	public void setLogChannel(Channel channel){
		this.logChannel = channel.getDiscordChannel().getID();
	}
	
	public List<String> getBlacklist(){
		return blacklist;
	}
	
	public List<String> getAssignableRoles(){
		return assignableRoles;
	}

	public List<String> getAdmins() {
		return this.admins;
	}
	
	public List<String> getMods() {
		return this.mods;
	}
	
	public String getID() {
		return guild.getID();
	}
	
	public boolean isOwner(User user){
		return guild.getOwnerID().equals(user.getID());
	}
	
	public boolean isAdmin(User user){
		return user.getDiscordUser().getPermissionsForGuild(guild).contains(Permissions.ADMINISTRATOR) || admins.contains(user.getID());
	}
	
	public boolean isMod(User user){
		return mods.contains(user.getID());
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
		admins = setDefault(admins, Lists.newArrayList());
		mods = setDefault(mods, Lists.newArrayList());
		logChannel = setDefault(logChannel, "");
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

	public String getName() {
		return guild.getName();
	}
}
