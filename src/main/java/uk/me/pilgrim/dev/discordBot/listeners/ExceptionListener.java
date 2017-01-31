package uk.me.pilgrim.dev.discordBot.listeners;

import com.google.common.eventbus.Subscribe;

import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;
import uk.me.pilgrim.dev.core.events.ExceptionEvent;
import uk.me.pilgrim.dev.discordBot.events.MessageEvent;

public class ExceptionListener {

	@Subscribe
	public void onExceptionEvent(ExceptionEvent ex) throws MissingPermissionsException, RateLimitException, DiscordException{
		if (ex.isCanceled()) return;
		if (!(ex.getEvent() instanceof MessageEvent)) return;
		MessageEvent event = (MessageEvent) ex.getEvent();
		
		String reply = "";
		StackTraceElement[] stackTrace = ex.getException().getStackTrace();
		reply += ex.getException();
		reply += "\n";
		for (StackTraceElement s : stackTrace){
			reply += " at " + s;
			
			reply += "\n";
		}
		
		reply = reply.substring(0, 1900);
		
		event.getChannel().sendMessage("```" + reply + "```");
	}
}
