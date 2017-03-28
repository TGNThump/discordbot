package uk.me.pilgrim.dev.discordBot.events;

import sx.blah.discord.handle.obj.IMessage;
import uk.me.pilgrim.dev.core.util.Context;
import uk.me.pilgrim.dev.discordBot.models.Channel;
import uk.me.pilgrim.dev.discordBot.models.Guild;
import uk.me.pilgrim.dev.discordBot.models.User;

public class MessageEvent{

	private final Context context;
	
	public MessageEvent(Context context) {
		this.context = context;
	}
	
	public boolean isPrivate(){
		return getChannel().getDiscordChannel().isPrivate();
	}
	
	public Context getContext(){
		return context;
	}
	
	public IMessage getMessage(){
		return this.context.get(IMessage.class);
	}
	
	public User getAuthor(){
		return this.context.get(User.class);
	}
	
	public Channel getChannel(){
		return this.context.get(Channel.class);
	}
	
	public Guild getGuild(){
		return this.context.get(Guild.class);
	}
	
	public String getContent(){
		return this.getMessage().getContent();
	}
}