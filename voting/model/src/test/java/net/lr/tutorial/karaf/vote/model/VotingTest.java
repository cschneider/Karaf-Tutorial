package net.lr.tutorial.karaf.vote.model;

import org.junit.Assert;
import org.junit.Test;

public class VotingTest {
	@Test
	public void testAverage() {
		Voting voting = createVoting();
		Assert.assertEquals(1.5, voting.getAverage(), 0.001);
	}

	private Voting createVoting() {
		Voting voting = new Voting("Ho do you like Camel ?");
		voting.addVote(new Vote(1));
		voting.addVote(new Vote(2));
		voting.addVote(new Vote(1));
		voting.addVote(new Vote(2));
		return voting;
	}
	
	@Test
	public void testStats() {
		Voting voting = createVoting();
		voting.addVote(new Vote(2));
		String table = voting.getStats();
		Assert.assertEquals("[[\"Vote\",\"Count\"],[\"1\",2],[\"2\",3],[\"3\",0],[\"4\",0],[\"5\",0],[\"6\",0]]", table);
	}
}
