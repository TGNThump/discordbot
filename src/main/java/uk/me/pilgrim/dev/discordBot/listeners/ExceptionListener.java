package uk.me.pilgrim.dev.discordBot.listeners;

import java.awt.Color;

import javax.inject.Inject;

import com.google.common.eventbus.Subscribe;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;
import uk.me.pilgrim.dev.core.Core;
import uk.me.pilgrim.dev.core.events.ExceptionEvent;
import uk.me.pilgrim.dev.discordBot.config.MainConfig;
import uk.me.pilgrim.dev.discordBot.events.MessageEvent;

public class ExceptionListener {

	@Inject
	MainConfig config;
	
	@Inject
	IDiscordClient client;
	
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
		
		boolean block = reply.endsWith("```");
		if (reply.length() > 1900)
			reply = reply.substring(0, 1900);
		if (block) reply += "```";

		EmbedObject embed = new EmbedBuilder()
			.withColor(new Color(237, 101, 104))
			.withDescription(reply)
			.build();
		
		if (Core.isDevMode()) event.getChannel().getDiscordChannel().sendMessage("", embed, true);
		client.getUserByID(config.adminId).getOrCreatePMChannel().sendMessage("", embed, true);
	}
}
