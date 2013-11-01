
package net.lr.tutorial.karaf.vote.command.impl;

import net.lr.tutorial.karaf.vote.model.VoteService;
import net.lr.tutorial.karaf.vote.model.Voting;

import org.apache.felix.gogo.commands.Argument;
import org.apache.felix.gogo.commands.Command;
import org.apache.karaf.shell.console.OsgiCommandSupport;


/**
 * Displays the last log entries
 */
@Command(scope = "vote", name = "stats", description = "list existing votes")
public class VoteStats extends OsgiCommandSupport {
	protected VoteService voteService;
	
	@Argument(index = 0, name="topic", description = "Voting topic to show", required = true, multiValued = false)
	private String topic;

    public void setVoteService(VoteService voteService) {
		this.voteService = voteService;
	}

	protected Object doExecute() throws Exception {
         System.out.println("Executing command list");
         Voting voting = voteService.getVoting(topic);
         System.out.println(voting.getStats());
         return null;
    }
}
