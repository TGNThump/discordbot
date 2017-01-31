package uk.me.pilgrim.dev.discordBot.events;

import java.util.List;

import sx.blah.discord.handle.obj.IMessage;

public class MessageBlacklistedEvent extends MessageEvent {
	private List<String> words; // Words contained in the message that trigger the blacklist event.
	
	public MessageBlacklistedEvent(IMessage message, List<String> words) {
		super(message);
		this.words = words;
	}
	
	public List<String> getWords(){
		return words;
	}

}
