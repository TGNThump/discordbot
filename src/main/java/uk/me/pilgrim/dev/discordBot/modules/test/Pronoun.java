/**
 * This file is part of DiscordBot.
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */
package uk.me.pilgrim.dev.discordBot.modules.test;


/**
 * @author Benjamin Pilgrim &lt;ben@pilgrim.me.uk&gt;
 */
public enum Pronoun {
	HE("he","him","his"),
	SHE("she","her","hers"),
	THEY("they","them","theirs");
	
	private final String subject, object, possessive;
	
	Pronoun(String subject, String object, String possessive){
		this.subject = subject;
		this.object = object;
		this.possessive = possessive;
	}
}
