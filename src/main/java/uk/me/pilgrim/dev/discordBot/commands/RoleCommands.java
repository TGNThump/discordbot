package uk.me.pilgrim.dev.discordBot.commands;

import java.util.List;

import javax.inject.Inject;

import com.google.common.collect.Lists;

import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;
import uk.me.pilgrim.dev.core.commands.CommandResult;
import uk.me.pilgrim.dev.core.commands.annotations.Command;
import uk.me.pilgrim.dev.core.commands.sources.CommandSource;
import uk.me.pilgrim.dev.core.util.Context;
import uk.me.pilgrim.dev.core.util.text.Text;
import uk.me.pilgrim.dev.discordBot.models.Guild;

public class RoleCommands {
	
	@Inject
	Guild.Registry guildRegistry;
	
	
	@Command("role")
	public CommandResult onRoleList(Context context){
		Guild guild = guildRegistry.get(context.get(IGuild.class));
		String message = "Self Assignable Roles: ";
		
		List<String> roles = Lists.newArrayList();
		for (String id : guild.getAssignableRoles()){
			roles.add(guild.getDiscordGuild().getRoleByID(id).getName());
		}
		
		message += "`" + Text.implodeCommaAnd(roles, "`, `","` and `") + "`";
		
		if (roles.isEmpty()){
			message = "There are currently no self assignable roles.";
		}
		
		context.get(CommandSource.class).sendMessage(message);
		return CommandResult.SUCCESS;
	}
	
	@Command("role")
	public CommandResult onRole(Context context, IRole role) throws MissingPermissionsException, RateLimitException, DiscordException{
		Guild guild = guildRegistry.get(context.get(IGuild.class));
		if (!guild.getAssignableRoles().contains(role.getID())){
			context.get(CommandSource.class).sendMessage("Role `" + role.getName() + "` is not self assignable.");
			return CommandResult.FAILURE;
		}
		
		IUser user = context.get(IUser.class);
		
		if (user.getRolesForGuild(guild.getDiscordGuild()).contains(role)){
			user.removeRole(role);
			context.get(CommandSource.class).sendMessage("Removed role `" + role.getName() + "`.");
		} else {
			user.addRole(role);
			context.get(CommandSource.class).sendMessage("Added role `" + role.getName() + "`.");
		}
		
		return CommandResult.SUCCESS;
	}
	
	@Command("role add")
	public CommandResult onRoleAdd(Context context, IRole role){
		Guild guild = guildRegistry.get(context.get(IGuild.class));
		guild.getAssignableRoles().add(role.getID());
		guild.save();
		context.get(CommandSource.class).sendMessage("Added `" + role.getName() + "` to self assignable role list.");
		return CommandResult.SUCCESS;
	}
	
	@Command("role remove")
	public CommandResult onRoleRemove(Context context, IRole role){
		Guild guild = guildRegistry.get(context.get(IGuild.class));
		guild.getAssignableRoles().remove(role.getID());
		guild.save();
		context.get(CommandSource.class).sendMessage("Removed `" + role.getName() + "` from self assignable role list.");
		return CommandResult.SUCCESS;
	}
}
