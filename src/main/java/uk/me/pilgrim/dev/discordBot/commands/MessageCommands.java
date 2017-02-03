package uk.me.pilgrim.dev.discordBot.commands;

import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IEmbed;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;
import uk.me.pilgrim.dev.core.commands.CommandResult;
import uk.me.pilgrim.dev.core.commands.annotations.Command;
import uk.me.pilgrim.dev.core.commands.sources.CommandSource;
import uk.me.pilgrim.dev.core.util.Context;

public class MessageCommands {
	
	@Command("message")
	public CommandResult onMessage(Context context){
		return CommandResult.SUCCESS;
	}
	
	@Command("message say")
	public CommandResult onMessageSay(Context context, IChannel channel, String content) throws RateLimitException, DiscordException, MissingPermissionsException{
		EmbedObject embed = new EmbedBuilder()
			.withColor(137, 204, 240)
			.withDescription(content)
			.build();

		channel.sendMessage("", embed, true);
		return CommandResult.SUCCESS;
	}
	
	@Command("message show")
	public CommandResult onMessageShow(Context context, IMessage message){
		if ((message.getContent() == null || message.getContent().equals("")) && message.getEmbedded().size() > 0){
			IEmbed embed = message.getEmbedded().get(0);
			context.get(CommandSource.class).sendMessage("```" + embed.getDescription() + "```");
		} else {
			context.get(CommandSource.class).sendMessage("```" + message.getContent() + "```");
		}
		return CommandResult.SUCCESS;
	}
	
	@Command("message edit")
	public CommandResult onMessageEdit(Context context, IMessage message, String content) throws MissingPermissionsException, RateLimitException, DiscordException{
		if (message.isDeleted()){
			context.get(CommandSource.class).sendMessage("Cannot edit deleted message.");
			return CommandResult.FAILURE;
		}
		
		if ((message.getContent() == null || message.getContent().equals("")) && message.getEmbedded().size() > 0){
			if (message.getEmbedded().size() > 1){
				context.get(CommandSource.class).sendMessage("Cannot edit message with more than one embed.");
				return CommandResult.FAILURE;
			}
			IEmbed embed = message.getEmbedded().get(0);
			EmbedBuilder builder = new EmbedBuilder();
			
			if (embed.getAuthor() != null)
			builder = builder.withAuthorIcon(embed.getAuthor().getIconUrl())
				.withAuthorName(embed.getAuthor().getName())
				.withAuthorUrl(embed.getAuthor().getUrl());
			
			builder = builder.withColor(embed.getColor())
				.withDesc(content);
			
			if (embed.getImage() != null)
			builder = builder.withImage(embed.getImage().getUrl());
			
			if (embed.getThumbnail() != null)
			builder = builder.withThumbnail(embed.getThumbnail().getUrl());
			
			builder = builder.withTitle(embed.getTitle())
				.withUrl(embed.getUrl());
			
			if (embed.getFooter() != null)
			builder = builder.withFooterIcon(embed.getFooter().getIconUrl())
				.withFooterText(embed.getFooter().getText());
			
			EmbedObject result = builder.build();
			
			message.edit("", result);
		} else {
			message.edit(content);
		}
		context.get(CommandSource.class).sendMessage("Message in " + message.getChannel().mention() + " edited.");
		return CommandResult.SUCCESS;
	}
	
	@Command("message delete")
	public CommandResult onMessageDelete(Context context, IMessage message) throws MissingPermissionsException, RateLimitException, DiscordException{
		message.delete();
		return CommandResult.SUCCESS;
	}
	
}
