package uk.me.pilgrim.dev.discordBot.events;

import java.util.List;

import uk.me.pilgrim.dev.core.util.Context;

public class MessageBlacklistedEvent extends MessageEvent {
	private List<String> words; // Words contained in the message that trigger the blacklist event.
	
	public MessageBlacklistedEvent(Context context, List<String> words) {
		super(context);
		this.words = words;
	}
	
	public List<String> getWords(){
		return words;
	}

}
