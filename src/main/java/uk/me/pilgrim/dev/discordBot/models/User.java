package uk.me.pilgrim.dev.discordBot.models;

import java.util.HashMap;

import javax.inject.Singleton;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

import ninja.leaping.configurate.objectmapping.Setting;
import sx.blah.discord.handle.obj.IUser;
import uk.me.pilgrim.dev.core.config.Config;

public class User extends Config{
	
	private IUser user;
	
	@Setting
	String name;
	
	public IUser getDiscordUser(){
		return user;
	}
	
	public boolean hasPermission(String perm) {
		return true;
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
}
