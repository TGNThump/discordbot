package uk.me.pilgrim.dev.discordBot.events;

import sx.blah.discord.handle.obj.IMessage;

public class MessageReceivedEvent extends MessageEvent{

	public MessageReceivedEvent(IMessage message) {
		super(message);
	}

}
