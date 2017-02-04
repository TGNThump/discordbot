package uk.me.pilgrim.dev.discordBot.modules.assignableroles;

import java.util.List;

import javax.inject.Inject;

import com.google.common.collect.Lists;

import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.IUser;
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

public class RoleCommands {
	
	@Inject
	Guild.Registry guildRegistry;
	
	
	@Command("role")
	public CommandResult onRoleList(Context context) throws RateLimitException, DiscordException, MissingPermissionsException{
		Guild guild = context.get(Guild.class);
		String message = "Self Assignable Roles: ";
		
		List<String> roles = Lists.newArrayList();
		for (String id : guild.getAssignableRoles()){
			roles.add(guild.getDiscordGuild().getRoleByID(id).getName());
		}
		
		if (roles.isEmpty()){
			context.get(Channel.class).info("There are currently no self assignable roles.");
			return CommandResult.SUCCESS;
		}
		
		message += "`" + Text.implodeCommaAnd(roles, "`, `","` and `") + "`";
		
		context.get(Channel.class).info(message);
		return CommandResult.SUCCESS;
	}
	
	@Command("role")
	public CommandResult onRole(Context context, IRole role) throws MissingPermissionsException, RateLimitException, DiscordException{
		Guild guild = context.get(Guild.class);
		if (!guild.getAssignableRoles().contains(role.getID())){
			context.get(Channel.class).error("Role `" + role.getName() + "` is not self assignable.");
			return CommandResult.FAILURE;
		}
		
		IUser user = context.get(IUser.class);
		
		if (user.getRolesForGuild(guild.getDiscordGuild()).contains(role)){
			user.removeRole(role);
			context.get(Channel.class).info("Removed role `" + role.getName() + "` from " + user.mention() + ".");
		} else {
			user.addRole(role);
			context.get(Channel.class).info("Added role `" + role.getName() + "` to " + user.mention() + ".");
		}
		
		return CommandResult.SUCCESS;
	}
	
	@Command("role add")
	@Perm("MANAGE_ROLES")
	public CommandResult onRoleAdd(Context context, IRole role) throws RateLimitException, DiscordException, MissingPermissionsException{
		Guild guild = context.get(Guild.class);
		if (!guild.getAssignableRoles().contains(role.getID()))
			guild.getAssignableRoles().add(role.getID());
		guild.save();
		context.get(Channel.class).info("Added `" + role.getName() + "` to self assignable role list.");
		return CommandResult.SUCCESS;
	}
	
	@Command("role remove")
	@Perm("MANAGE_ROLES")
	public CommandResult onRoleRemove(Context context, IRole role) throws RateLimitException, DiscordException, MissingPermissionsException{
		Guild guild = context.get(Guild.class);
		guild.getAssignableRoles().remove(role.getID());
		guild.save();
		context.get(Channel.class).info("Removed `" + role.getName() + "` from self assignable role list.");
		return CommandResult.SUCCESS;
	}
}
