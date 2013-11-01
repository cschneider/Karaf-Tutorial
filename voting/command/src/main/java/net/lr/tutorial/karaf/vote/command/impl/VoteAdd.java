
package net.lr.tutorial.karaf.vote.command.impl;

import net.lr.tutorial.karaf.vote.model.VoteService;

import org.apache.felix.gogo.commands.Argument;
import org.apache.felix.gogo.commands.Command;
import org.apache.karaf.shell.console.OsgiCommandSupport;


/**
 * Displays the last log entries
 */
@Command(scope = "vote", name = "add", description = "add a vote")
public class VoteAdd extends OsgiCommandSupport {
	protected VoteService voteService;
	
	@Argument(index = 0, name="topic", description = "Voting topic to show", required = true, multiValued = false)
	private String topic;
	
	@Argument(index = 1, name="vote", description = "Vote to add (1..6)", required = true, multiValued = false)
	private Integer vote;

    public void setVoteService(VoteService voteService) {
		this.voteService = voteService;
	}

	protected Object doExecute() throws Exception {
         System.out.println(String.format("Vote %d added for topic %s", vote, topic));
         voteService.addVote(topic, vote);
         return null;
    }
}
