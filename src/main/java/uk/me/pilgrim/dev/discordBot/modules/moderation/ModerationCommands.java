package uk.me.pilgrim.dev.discordBot.modules.moderation;

import java.util.Optional;

import sx.blah.discord.handle.obj.IGuild;
import uk.me.pilgrim.dev.core.commands.CommandResult;
import uk.me.pilgrim.dev.core.commands.Flag;
import uk.me.pilgrim.dev.core.commands.annotations.Command;
import uk.me.pilgrim.dev.core.commands.annotations.Perm;
import uk.me.pilgrim.dev.core.util.Context;
import uk.me.pilgrim.dev.discordBot.models.Channel;
import uk.me.pilgrim.dev.discordBot.models.Guild;
import uk.me.pilgrim.dev.discordBot.models.User;

public class ModerationCommands {
	
	@Command("ban")
	@Perm("mod")
	@Perm("BAN")
	public CommandResult onBan(Context context, Flag<Boolean> purge, User user, Optional<String> reason){
		Channel log = context.get(Guild.class).getLogChannel().orElse(context.get(Channel.class));
		
		if (user.getDiscordUser().isBot()){
			context.get(Channel.class).error("Cannot ban a bot user.");
			return CommandResult.FAILURE;
		}
		
		log.info("User `" + user.getName() + "#" + user.getDiscordUser().getDiscriminator() + "` was banned by " + context.get(User.class).mention() +
				(reason.isPresent() ? ("```" + reason.get() + "```") : ""));
				
		user.getPMChannel().info("You have been banned from **" + context.get(Guild.class).getName() + "**"
				+ (reason.isPresent() ? (" for ```" + reason.get() + "```") : ""));
		
		context.get(IGuild.class).banUser(user.getDiscordUser(), purge.isPresent() ? 999 : 0);	
		
		return CommandResult.SUCCESS;
	}
	
}
