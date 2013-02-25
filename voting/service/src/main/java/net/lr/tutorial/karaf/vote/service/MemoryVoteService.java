package net.lr.tutorial.karaf.vote.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.lr.tutorial.karaf.vote.model.Vote;
import net.lr.tutorial.karaf.vote.model.VoteService;
import net.lr.tutorial.karaf.vote.model.Voting;

public class MemoryVoteService implements VoteService {
	Map<String, Voting> votingMap;
	
	public MemoryVoteService() {
		this.votingMap = new HashMap<String, Voting>();
		addVote("test1", 1);
	}

	@Override
	public Voting getVoting(String topic) {
		if (this.votingMap.containsKey(topic)) {
			return votingMap.get(topic);
		} else {
			Voting voting = new Voting(topic);
			this.votingMap.put(topic, voting);
			return voting;
		}
	}

	@Override
	public Voting removeVoting(String topic) {
		return this.votingMap.remove(topic);
	}

	@Override
	public void addVote(String topic, int vote) {
		getVoting(topic).addVote(new Vote(vote));
	}

	@Override
	public Collection<Voting> getVotings() {
		return votingMap.values();
	}

	@Override
	public void addVote(Vote vote) {
		if (vote != null) {
			getVoting(vote.getTopic()).addVote(vote);
		}
	}

	@Override
	public String getStats(String topic) {
		return getVoting(topic).getStats();
	}

}
