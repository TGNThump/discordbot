/**
 * This file is part of DiscordBot.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */
package uk.me.pilgrim.dev.discordBot.commands;

import java.util.Optional;

import javax.inject.Inject;

import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IEmoji;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;
import uk.me.pilgrim.dev.core.commands.CommandResult;
import uk.me.pilgrim.dev.core.commands.annotations.Command;
import uk.me.pilgrim.dev.core.commands.annotations.Perm;
import uk.me.pilgrim.dev.core.util.Context;
import uk.me.pilgrim.dev.discordBot.PomData;
import uk.me.pilgrim.dev.discordBot.models.Channel;
import uk.me.pilgrim.dev.discordBot.models.Guild;
import uk.me.pilgrim.dev.discordBot.models.User;

/**
 * @author Benjamin Pilgrim &lt;ben@pilgrim.me.uk&gt;
 */
public class TestCommand {
	
	@Inject
	Guild.Registry guildRegistry;
	
	@Inject
	Channel.Registry channelRegistry;
	
	@Command("poke")
	public CommandResult onPoke(Context context, IUser user) throws RateLimitException, DiscordException, MissingPermissionsException{	
		context.get(Channel.class).info("*pokes " + user.mention() + "*");
		return CommandResult.SUCCESS;
	}
	
	@Command("❤")
	public CommandResult onHeart(Context context) throws MissingPermissionsException, RateLimitException, DiscordException{
		context.get(Channel.class).info("❤");
		return CommandResult.SUCCESS;
	}
	
	
	@Command("user")
	@Perm("test")
	public CommandResult onUser(Context context, Optional<User> user) throws RateLimitException, DiscordException, MissingPermissionsException{
		IUser author = user.orElse(context.get(User.class)).getDiscordUser();
		IGuild guild = context.get(IGuild.class);
		IEmoji emoji = guild.getEmojis().get(0);
		EmbedObject embed = new EmbedBuilder()
//			.withTitle("Title")
			.withAuthorName(author.getDisplayName(guild))
			.withAuthorIcon(author.getAvatarURL())
			.withColor(137, 204, 240)
			.withFooterText(guild.getClient().getOurUser().getDisplayName(guild) + " - v" + PomData.VERSION)
			.withDescription("Description **of** " + author.mention() + " " + emoji)
			.build();
		context.get(IChannel.class).sendMessage("", embed, false);
		return CommandResult.SUCCESS;
	}
	
	@Command("test")
	@Perm("test")
	public CommandResult onTest(Context context, User user) throws RateLimitException, DiscordException, MissingPermissionsException{		
		context.get(Channel.class).info(user.getName());
		return CommandResult.SUCCESS;
	}
	
	@Command("test")
	@Perm("test")
	public CommandResult onTest2(Context context, Channel channel) throws RateLimitException, DiscordException, MissingPermissionsException{
		context.get(Channel.class).info(channel.toString());
		
		return CommandResult.SUCCESS;
	}
	
}
