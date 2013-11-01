package net.lr.tutorial.karaf.vote.model;

import org.junit.Assert;
import org.junit.Test;

public class VotingTest {
	private static final String TOPIC = "How do you like Camel ?";

	@Test
	public void testAverage() {
		Voting voting = createVoting();
		Assert.assertEquals(1.5, voting.getAverage(), 0.001);
	}

	private Voting createVoting() {
		Voting voting = new Voting(TOPIC);
		voting.addVote(new Vote(TOPIC, 1));
		voting.addVote(new Vote(TOPIC, 2));
		voting.addVote(new Vote(TOPIC, 1));
		voting.addVote(new Vote(TOPIC, 2));
		return voting;
	}
	
	@Test
	public void testStats() {
		Voting voting = createVoting();
		voting.addVote(new Vote(TOPIC, 2));
		String table = voting.getStats();
		Assert.assertEquals("[[\"Vote\",\"Count\"],[\"1\",2],[\"2\",3],[\"3\",0],[\"4\",0],[\"5\",0],[\"6\",0]]", table);
	}
}
