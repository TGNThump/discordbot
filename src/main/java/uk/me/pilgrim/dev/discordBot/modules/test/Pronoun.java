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
	HE("he","him","his", "♂"),
	SHE("she","her","hers", "♀"),
	THEY("they","them","theirs", "⚥");
	
	private final String subjective, objective, possessive, unicode;
	
	Pronoun(String subjective, String objective, String possessive){
		this(subjective, objective, possessive, " ");
	}
	
	Pronoun(String subjective, String objective, String possessive, String unicode){
		this.subjective = subjective;
		this.objective = objective;
		this.possessive = possessive;
		this.unicode = unicode;
	}
	
	public String getSubjective(){
		return subjective;
	}
	
	public String getObjective(){
		return objective;
	}
	
	public String getPossessive(){
		return possessive;
	}

	public String getUnicode(){
		return unicode;
	}
}
