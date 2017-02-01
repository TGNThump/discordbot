package uk.me.pilgrim.dev.discordBot.models;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import sx.blah.discord.handle.obj.IChannel;
import uk.me.pilgrim.dev.core.config.Config;

@ConfigSerializable
public class Channel extends Config.Category{

	private IChannel channel;
	
	@Setting
	String name;
	
	public Channel(){
		
	}
	
	@Inject
	public Channel(@Assisted IChannel channel){
		this.channel = channel;
		this.name = channel.getName();
	}
	
	public IChannel getDiscordChannel(){
		return channel;
	}

	public interface Factory{
		Channel create(IChannel channel);
	}
}
