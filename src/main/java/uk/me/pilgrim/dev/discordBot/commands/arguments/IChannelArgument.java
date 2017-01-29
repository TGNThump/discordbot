/**
 * This file is part of core.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */
package uk.me.pilgrim.dev.discordBot.commands.arguments;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IChannel;
import uk.me.pilgrim.dev.core.Core;
import uk.me.pilgrim.dev.core.commands.arguments.ArgumentParser;
import uk.me.pilgrim.dev.core.commands.exceptions.ArgumentException;

/**
 * @author Benjamin Pilgrim &lt;ben@pilgrim.me.uk&gt;
 */
public class IChannelArgument implements ArgumentParser{

	/* (non-Javadoc)
	 * @see uk.me.pilgrim.dev.core.commands.arguments.ArgumentParser#isTypeSupported(java.lang.Class)
	 */
	@Override
	public boolean isTypeSupported(Class<?> type) {
		return type == IChannel.class;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T parseArgument(Class<T> type, String arg) throws ArgumentException, IllegalArgumentException {
		checkTypeSupported(type);
		String value = arg;
		if (!value.startsWith("<#")) throw getArgumentException(type, arg);
		value = value.substring(2, value.length()-1);
		
		IChannel channel = Core.get(IDiscordClient.class).getChannelByID(value);
		if (channel != null) return (T) channel;
		
		throw getArgumentException(type, arg);
	}
	
//	@Override
//	public List<String> getAllSuggestions(Class<?> type, String prefix) throws IllegalArgumentException {
//		checkTypeSupported(type);
//		
//		List<String> suggestions = Lists.newArrayList();
//		
//		
//		return suggestions;
//	}
//	
//	@Override
//	public String getSuggestionText(Class<?> type, String prefix) throws IllegalArgumentException {
//		checkTypeSupported(type);
//		return "";
//	}
	
	@Override
	public String getArgumentTypeName(Class<?> type) throws IllegalArgumentException {
		checkTypeSupported(type);
		return "Channel";
	}
	
}
