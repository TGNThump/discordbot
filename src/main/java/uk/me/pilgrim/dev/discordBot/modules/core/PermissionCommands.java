/**
 * This file is part of DiscordBot.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */
package uk.me.pilgrim.dev.discordBot.modules.core;

import java.util.List;

import com.google.common.collect.Lists;

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
import uk.me.pilgrim.dev.discordBot.models.User;

/**
 * @author Benjamin Pilgrim &lt;ben@pilgrim.me.uk&gt;
 */
public class PermissionCommands {
	
	@Command("staff")
	public CommandResult onStaff(Context context) throws RateLimitException, DiscordException, MissingPermissionsException{
		onAdmins(context);
		onMods(context);
		return CommandResult.SUCCESS;
	}
	
	@Command("admins")
	public CommandResult onAdmins(Context context) throws RateLimitException, DiscordException, MissingPermissionsException{
		Guild guild = context.get(Guild.class);
		String message = "Admins: ";
		
		List<String> admins = Lists.newArrayList();
		for (String id : guild.getAdmins()){
			admins.add(guild.getDiscordGuild().getUserByID(id).mention());
		}
		
		if (admins.isEmpty()){
			context.get(Channel.class).info("There are currently no admins.");
			return CommandResult.SUCCESS;
		}
		
		message += Text.implodeCommaAnd(admins);
		
		context.get(Channel.class).info(message);
		return CommandResult.SUCCESS;
	}
	
	@Command("admins add")
	@Perm("owner")
	public CommandResult onAdminAdd(Context context, User user) throws RateLimitException, DiscordException, MissingPermissionsException{
		Guild guild = context.get(Guild.class);
		guild.getAdmins().add(user.getID());
		guild.save();
		context.get(Channel.class).info("Added " + user.mention() + " to admins list for `" + guild.getName() + "`.");
		return CommandResult.SUCCESS;
	}
	
	@Command("admins remove")
	@Perm("owner")
	public CommandResult onAdminRemove(Context context, User user) throws MissingPermissionsException, RateLimitException, DiscordException{
		Guild guild = context.get(Guild.class);
		guild.getAdmins().remove(user.getID());
		guild.save();
		context.get(Channel.class).info("Removed " + user.mention() + " from admins list for `" + guild.getName() + "`.");
		return CommandResult.SUCCESS;
	}
	
	@Command("mods")
	public CommandResult onMods(Context context) throws RateLimitException, DiscordException, MissingPermissionsException{
		Guild guild = context.get(Guild.class);
		String message = "Moderators: ";
		
		List<String> mods = Lists.newArrayList();
		for (String id : guild.getMods()){
			mods.add(guild.getDiscordGuild().getUserByID(id).mention());
		}
		
		if (mods.isEmpty()){
			context.get(Channel.class).info("There are currently no moderators.");
			return CommandResult.SUCCESS;
		}
		
		message += Text.implodeCommaAnd(mods);
		
		context.get(Channel.class).info(message);
		return CommandResult.SUCCESS;
	}
	
	@Command("mods add")
	@Perm("admin")
	public CommandResult onModAdd(Context context, User user) throws RateLimitException, DiscordException, MissingPermissionsException{
		Guild guild = context.get(Guild.class);
		guild.getMods().add(user.getID());
		guild.save();
		context.get(Channel.class).info("Added " + user.mention() + " to moderators list for `" + guild.getName() + "`.");
		return CommandResult.SUCCESS;
	}
	
	@Command("mods remove")
	@Perm("owner")
	public CommandResult onModRemove(Context context, User user) throws MissingPermissionsException, RateLimitException, DiscordException{
		Guild guild = context.get(Guild.class);
		guild.getMods().remove(user.getID());
		guild.save();
		context.get(Channel.class).info("Removed " + user.mention() + " from moderators list for `" + guild.getName() + "`.");
		return CommandResult.SUCCESS;
	}
}
