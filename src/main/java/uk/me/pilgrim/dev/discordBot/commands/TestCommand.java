/**
 * This file is part of DiscordBot.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */
package uk.me.pilgrim.dev.discordBot.commands;

import java.util.List;
import java.util.Optional;

import com.google.common.collect.Lists;

import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;
import uk.me.pilgrim.dev.core.commands.CommandResult;
import uk.me.pilgrim.dev.core.commands.annotations.Command;
import uk.me.pilgrim.dev.core.commands.annotations.Desc;
import uk.me.pilgrim.dev.core.commands.sources.CommandSource;
import uk.me.pilgrim.dev.core.util.Context;

/**
 * @author Benjamin Pilgrim &lt;ben@pilgrim.me.uk&gt;
 */
public class TestCommand {
	

	@Command("poke")
	public CommandResult onPoke(Context context, IUser user){		
		context.get(CommandSource.class).sendMessage("*pokes " + user.mention() + "*");
		return CommandResult.SUCCESS;
	}
	
	@Command("❤")
	public CommandResult onHeart(Context context) throws MissingPermissionsException, RateLimitException, DiscordException{
		context.get(IChannel.class).sendMessage("❤");
		return CommandResult.SUCCESS;
	}
	
	@Command("clearbotmessages")
	public CommandResult onClearBotMessages(Context context) throws MissingPermissionsException, RateLimitException, DiscordException{
		IChannel channel = context.get(IChannel.class);
		List<IMessage> messages = Lists.newArrayList();
		
		channel.getMessages().forEach((message) -> {
			if (message.getAuthor().isBot()){ messages.add(message); return;}
			message.getMentions().forEach((user) -> {
				if (user.isBot()){ messages.add(message); return;}
			});
		});
		
		if (messages.size() > 2 && messages.size() < 100)
		channel.getMessages().bulkDelete(messages);
		
		return CommandResult.SUCCESS;
	}
	
	@Command("user")
	public CommandResult onUser(Context context, Optional<IUser> user) throws MissingPermissionsException, RateLimitException, DiscordException{
		IUser target = user.orElse(context.get(IUser.class));
		IGuild guild = context.get(IGuild.class);
		IChannel channel = context.get(IChannel.class);
		
		channel.sendMessage(
			"Name: " + target.getDisplayName(guild) + " (@" + target.getName() + ")\n" +
			target.getPresence() + "\n" + target.getID());
		
		return CommandResult.SUCCESS;
	}
	
	@Command("test")
	@Desc("Test Command.")
	public CommandResult onTest(Context context, IUser user){		
		context.get(CommandSource.class).sendMessage(user.getName());
		return CommandResult.SUCCESS;
	}
	
	@Command("test")
	@Desc("Test Command.")
	public CommandResult onTest2(Context context, IChannel channel){
		context.get(CommandSource.class).sendMessage(channel.getName());
		return CommandResult.SUCCESS;
	}
	
}
