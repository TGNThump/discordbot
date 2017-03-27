package uk.me.pilgrim.dev.discordBot.modules.editlogger;

import java.util.List;

import javax.inject.Inject;

import com.google.common.collect.Lists;

import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;
import uk.me.pilgrim.dev.core.commands.CommandResult;
import uk.me.pilgrim.dev.core.commands.annotations.Command;
import uk.me.pilgrim.dev.core.commands.annotations.Perm;
import uk.me.pilgrim.dev.core.util.Context;
import uk.me.pilgrim.dev.core.util.text.Text;
import uk.me.pilgrim.dev.discordBot.models.Channel;
import uk.me.pilgrim.dev.discordBot.models.Guild;

public class EditLoggerCommands {

	@Inject
	Channel.Registry channelRegistry;
	
	@Command("editlogging")
	public CommandResult onEditLoggerList(Context context) throws RateLimitException, DiscordException, MissingPermissionsException{
		Guild guild = context.get(Guild.class);
		String message = "Logging edits for ";
		
		List<String> editloggingchannels = Lists.newArrayList();
		for (IChannel c : guild.getDiscordGuild().getChannels()){
			Channel channel = channelRegistry.get(c);
			if (!channel.isLoggingEdits()){
				continue;
			}
			editloggingchannels.add(channel.mention());
		}
		
		if (editloggingchannels.isEmpty()){
			context.get(Channel.class).info("There are currently no channels with edit logging enabled.");
			return CommandResult.SUCCESS;
		}
		
		message += Text.implodeCommaAnd(editloggingchannels, ", "," and ");
		
		context.get(Channel.class).info(message);
		return CommandResult.SUCCESS;
	}
	
	@Command("editlogging")
	@Perm("admin")
	public CommandResult onSetEditLogging(Context context, Channel channel) throws RateLimitException, DiscordException, MissingPermissionsException{
		channel.setLoggingEdits(!channel.isLoggingEdits());
		channel.save();
		context.get(Channel.class).info((channel.isLoggingEdits() ? "Enabled" : "Disabled") + " edit logging for " + channel.mention() + ".");
		return CommandResult.SUCCESS;
	}
	
}
