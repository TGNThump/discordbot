/**
 * This file is part of DiscordBot.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */
package uk.me.pilgrim.dev.discordBot.events;

import sx.blah.discord.handle.obj.IMessage;
import uk.me.pilgrim.dev.core.util.Context;

/**
 * @author Benjamin Pilgrim &lt;ben@pilgrim.me.uk&gt;
 */
public class MessageEditedEvent extends MessageEvent{
	
	private final IMessage oldMessage;
	private final IMessage newMessage;
	
	public MessageEditedEvent(Context context, IMessage oldMessage, IMessage newMessage){
		super(context);
		this.oldMessage = oldMessage;
		this.newMessage = newMessage;
	}
	
	public IMessage getOld(){
		return oldMessage;
	}
	
	public IMessage getNew(){
		return newMessage;
	}
	
}
