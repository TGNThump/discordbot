/**
 * This file is part of DiscordBot.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */
package uk.me.pilgrim.dev.discordBot.modules.linkaccount;

import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IUser;
import uk.me.pilgrim.dev.core.commands.CommandResult;
import uk.me.pilgrim.dev.core.commands.annotations.Command;
import uk.me.pilgrim.dev.core.util.Context;
import uk.me.pilgrim.dev.discordBot.models.Channel;

/**
 * @author Benjamin Pilgrim &lt;ben@pilgrim.me.uk&gt;
 */
public class LinkCommands {
	
	
	@Command("link")
	public CommandResult onLink(Context context){
		context.get(Channel.class).info("This feature is not yet implemented.\n\n For now, we have granted you member rights, but you will need to re-use this command in the future.");
		context.get(IUser.class).addRole(context.get(IGuild.class).getRolesByName("Member").get(0));
		return CommandResult.SUCCESS;
	}
}
