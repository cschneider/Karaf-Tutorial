package net.lr.tutorial.karaf.vote.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.lr.tutorial.karaf.vote.model.Vote;
import net.lr.tutorial.karaf.vote.model.VoteService;
import net.lr.tutorial.karaf.vote.model.Voting;
import net.lr.tutorial.karaf.vote.model.VotingLink;
import net.lr.tutorial.karaf.vote.model.VotingLinks;

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
	public VotingLinks getVotings() {
		VotingLinks result = new VotingLinks();
		List<VotingLink> votingLinks = result.getVotingLinks();
		for (Voting voting : votingMap.values()) {
			votingLinks.add(new VotingLink(voting.getTopic(), voting.getTopic()));
		}
		return result;
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
