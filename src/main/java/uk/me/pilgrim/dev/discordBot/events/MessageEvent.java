package uk.me.pilgrim.dev.discordBot.events;

import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;

public class MessageEvent{

	private final IMessage message;
	
	public MessageEvent(IMessage message) {
		this.message = message;
	}
	
	public IMessage getMessage(){
		return this.message;
	}
	
	public IUser getAuthor(){
		return this.getMessage().getAuthor();
	}
	
	public IChannel getChannel(){
		return this.getMessage().getChannel();
	}
	
	public IGuild getGuild(){
		return this.getMessage().getGuild();
	}
	
	public String getContent(){
		return this.getMessage().getContent();
	}
}