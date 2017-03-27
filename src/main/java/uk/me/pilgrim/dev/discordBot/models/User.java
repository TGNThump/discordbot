package uk.me.pilgrim.dev.discordBot.models;

import java.util.Date;
import java.util.HashMap;

import javax.inject.Singleton;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

import ninja.leaping.configurate.objectmapping.Setting;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.Permissions;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.RateLimitException;
import uk.me.pilgrim.dev.core.config.Config;

public class User extends Config{
	
	@Inject
	Channel.Registry channelRegistry;
	
	private IUser user;
	
	@Setting
	String name;
	
	@Setting
	Date firstSeen;
	
	@Setting
	Date lastSeen;
	
	@Setting
	Integer messageCount;
	
	public IUser getDiscordUser(){
		return user;
	}
	
	public String getID() {
		return user.getID();
	}
	
	public Channel getPMChannel() throws RateLimitException, DiscordException{
		return channelRegistry.get(user.getOrCreatePMChannel());
	}
	
	public boolean hasPermission(Guild guild, String perm) {
		if (guild.isOwner(this)) return true;
		
		switch(perm){
			case "owner": return false;
			case "admin": return guild.isAdmin(this);
			case "mod": return guild.isAdmin(this) || guild.isMod(this);
		}
		
		try{
			Permissions permission = Permissions.valueOf(perm);
			return (user.getPermissionsForGuild(guild.getDiscordGuild()).contains(permission));
		} catch (IllegalArgumentException ex){
			return false;
		}
		
	}
	
	public User(){
		registerEvents();
	}
	
	@Inject
	public User(@Assisted IUser user){
		super("data/users", user.getID() + ".conf");
		this.user = user;
		this.name = user.getName();
		registerEvents();
	}
	
	@Override
	public void setDefaults() {
		if (user != null) name = user.getName();
		firstSeen = setDefault(firstSeen, new Date());
		lastSeen = setDefault(lastSeen, new Date());
		messageCount = setDefault(messageCount, 0);
	}
	
	// Factory and Registry

	public interface Factory{
		User create(IUser user);
	}
	
	@Singleton
	public static class Registry{
		
		@Inject
		private User.Factory factory;
		
		private HashMap<String, User> users;
		
		public Registry() {
			users = new HashMap<>();
		}
		
		public User get(IUser user){ 
			if (!users.containsKey(user.getID())){
				User c = factory.create(user);
				users.put(user.getID(), c);
				c.save();
			}
			
			return users.get(user.getID());
		}
	}
	
	public String getName() {
		return user.getName();
	}

	public String mention() {
		return user.mention();
	}
}
