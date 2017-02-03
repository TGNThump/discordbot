/**
 * This file is part of core.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */
package uk.me.pilgrim.dev.discordBot.commands.arguments;

import java.util.List;

import com.google.common.collect.Lists;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IRole;
import uk.me.pilgrim.dev.core.Core;
import uk.me.pilgrim.dev.core.commands.arguments.ArgumentParser;
import uk.me.pilgrim.dev.core.commands.exceptions.ArgumentException;
import uk.me.pilgrim.dev.core.util.Context;

/**
 * @author Benjamin Pilgrim &lt;ben@pilgrim.me.uk&gt;
 */
public class IRoleArgument implements ArgumentParser{

	/* (non-Javadoc)
	 * @see uk.me.pilgrim.dev.core.commands.arguments.ArgumentParser#isTypeSupported(java.lang.Class)
	 */
	@Override
	public boolean isTypeSupported(Class<?> type) {
		return type == IRole.class;
	}

	@SuppressWarnings("unchecked")
	public <T> T parseArgument(Context context, Class<T> type, String arg) throws ArgumentException, IllegalArgumentException {
		checkTypeSupported(type);
		String value = arg;
		
		if (value.startsWith("<@&")){
			value = value.substring(3, value.length()-1);
		
			IRole role = Core.get(IDiscordClient.class).getRoleByID(value);
			if (role != null) return (T) role;
		} else {
			IGuild guild = context.get(IGuild.class);
			List<IRole> roles = guild.getRoles();
			List<IRole> matches = Lists.newArrayList();
			for (IRole role : roles){
				if (role.getName().equalsIgnoreCase(value)) matches.add(role);
			}
			if (matches.size() > 1)
				throw new ArgumentException("Cannot get role by name as multiple roles match argument " + arg + ".", arg, this, type);
		
			if (matches.size() == 1) return (T) matches.get(0);
		}
		throw getArgumentException(type, arg);
	}
		
	@Override
	public String getArgumentTypeName(Class<?> type) throws IllegalArgumentException {
		checkTypeSupported(type);
		return "Role";
	}

	@Override
	public <T> T parseArgument(Class<T> type, String arg) throws ArgumentException, IllegalArgumentException {
		return null;
	}
	
}
